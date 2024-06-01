import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import { wsServerURL } from "config";
import { store } from "redux/store";
import { getCurrentChat } from "modules/CurrentChat";

let client: Client;

export function connect(userId: string, chatId: string) {
  const socket: WebSocket = new SockJS(wsServerURL);

  client = new Client();

  client.configure({
    brokerURL: wsServerURL,
    onConnect: () => {
      client.subscribe(`/private_chat/messages/${chatId}`, (message) => {
        console.log("private_chat: ", JSON.parse(message.body));
        store.dispatch(getCurrentChat(chatId));
      });
      client.subscribe(`/private_chat/messages/${chatId}/errors`, (message) => {
        console.log("private_chat error: ", JSON.parse(message.body));
      });
      client.subscribe(`/chats/lastMessage/${userId}`, (message) => {
        console.log("chats: ", JSON.parse(message.body));
      });
    },
    webSocketFactory: () => {
      return socket;
    },

    debug: (str) => {
      console.log("debug: ", str);
    },
  });

  client.activate();
}

export function privateChatSendMessage(
  message: string,
  chatId: string,
  createdAt: number
) {
  client.publish({
    destination: `app/private_chat/sendMessage`,
    body: JSON.stringify({
      chatId,
      content: message,
      createdAt: createdAt,
    }),
  });
}

//======================================================================================
//======================================================================================
//======================================================================================
//======================================================================================
//======================================================================================
//======================================================================================
//======================================================================================
//======================================================================================

export function connectGroup(userId: string, chatId: string) {
  const socket: WebSocket = new SockJS(wsServerURL);

  client = new Client();

  client.configure({
    brokerURL: wsServerURL,
    onConnect: () => {
      client.subscribe(`/chats/lastMessage/${userId}`, (message) => {
        console.log("chats/lastMessage: ", JSON.parse(message.body));
      });
      client.subscribe(`/group_chat/anon_messages/${chatId}`, (message) => {
        console.log("group_chat/anon_messages: ", JSON.parse(message.body));
      });
      client.subscribe(`/group_chats/updatedMembers/${chatId}`, (message) => {
        console.log("group_chats/updatedMembers: ", JSON.parse(message.body));
      });
      client.subscribe(`/group_chat/add/${chatId}/errors`, (message) => {
        console.log("group_chat/add errors: ", JSON.parse(message.body));
      });
    },
    webSocketFactory: () => {
      return socket;
    },

    debug: (str) => {
      console.log("debug: ", str);
    },
  });

  client.activate();
}

export function addGroupMember(chatId: string, memberId: string) {
  client.publish({
    destination: `app/group_chat/add`,
    body: JSON.stringify({
      chatId,
      memberId,
    }),
  });
}
export function sendGroupMessage(content: string, chatId: string, createdAt: number) {
  client.publish({
    destination: `app/group_chat/sendMessage`,
    body: JSON.stringify({
      chatId,
      content: content,
      createdAt: createdAt,
    }),
  });
}
