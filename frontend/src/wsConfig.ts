import { Client } from "@stomp/stompjs";
import { wsServerURL } from "config";
import { getCurrentChat } from "modules/CurrentChat";
import { store } from "redux/store";
import SockJS from "sockjs-client";
import {
  updateCurrentChat,
  updateGroupMembers,
} from "modules/CurrentChat/store/currentChatSlice";

let client: Client;

export function connect(userId: string, chatId: string, isPrivate: boolean) {
  const socket: WebSocket = new SockJS(wsServerURL);
  client = new Client();

  const onConnectGroup = () => {
    client.subscribe(`/group_chat/messages/${chatId}`, (data) => {
      const json = JSON.parse(data.body);

      store.dispatch(updateCurrentChat({ data: json, type: "groupChat" }));
    });
    client.subscribe(`/group_chat/updated_members/${chatId}`, (data) => {
    });
    client.subscribe(`/chats/last_message/${userId}`, (message) => {
      console.log("chats/lastMessage: ", JSON.parse(message.body));
    });
    client.subscribe(`/group_chat/add/${chatId}/errors`, (message) => {
      console.log("group_chat/add errors: ", JSON.parse(message.body));
    });
    client.subscribe(`/group_chat/messages/${chatId}/errors`, (message) => {
      console.log("/group_chat/messages errors: ", JSON.parse(message.body));
    });
  };

  const onConnectPrivate = () => {
    client.subscribe(`/private_chat/messages/${chatId}`, (message) => {
      const data = JSON.parse(message.body);

      store.dispatch(updateCurrentChat({ data, type: "privateChat" }));
    });
    client.subscribe(`/private_chat/messages/${chatId}/errors`, (message) => {
      console.log("private_chat error: ", JSON.parse(message.body));
    });
    client.subscribe(`/chats/last_message/${userId}`, (message) => {
      console.log("chats: ", JSON.parse(message.body));
    });
  };

  client.configure({
    brokerURL: wsServerURL,
    onConnect: isPrivate ? onConnectPrivate : onConnectGroup,
    webSocketFactory: () => {
      return socket;
    },

    debug: (str) => {
      //console.log("debug: ", str);
    },
  });

  client.activate();
}

// SEND REQUESTS

export function privateChatSendMessage(
  message: string,
  chatId: string,
  createdAt: number
) {
  client.publish({
    destination: `/app/private_chat/send_message`,
    body: JSON.stringify({
      chatId,
      content: message,
      createdAt: createdAt,
    }),
  });
}

export function addGroupMember(chatId: string, memberId: string) {
  client.publish({
    destination: `/app/group_chat/add`,
    body: JSON.stringify({
      chatId,
      memberId,
    }),
  });
}

export function removeGroupMember(chatId: string, memberId: string) {
  client.publish({
    destination: `/app/group_chat/remove`,
    body: JSON.stringify({
      chatId,
      memberId,
    }),
  });
}

export function sendGroupMessage(content: string, chatId: string, createdAt: number) {
  client.publish({
    destination: `/app/group_chat/send_message`,
    body: JSON.stringify({
      chatId,
      content: content,
      createdAt: createdAt,
    }),
  });
}
