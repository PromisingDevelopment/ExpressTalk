import { Client } from "@stomp/stompjs";
import { wsServerURL } from "config";
import {
  resetChats,
  updateCurrentChatMessages,
  updateGroupMembers,
  updateGroupName,
} from "modules/CurrentChat";
import { filterChatsList, updateLastMessage, updateNameGroupInList } from "modules/Sidebar";
import { store } from "redux/store";
import SockJS from "sockjs-client";
import { CurrentChatType } from "types/CurrentChatType";
import { MemberRoles } from "types/MemberRoles";

const chatClient = new Client();
const sidebarClient = new Client();

// connect -----------------------------------------------------------
export function connect(chatId: string, isPrivate: boolean) {
  const socket: WebSocket = new SockJS(wsServerURL);

  const onConnectGroup = () => {
    chatClient.subscribe(`/group_chat/messages/${chatId}`, (data) => {
      const json = JSON.parse(data.body);
      const type: CurrentChatType = "groupChat";

      console.log("/group_chat/messages", json);

      const messageData = {
        data: json,
        type: type,
      };

      if (typeof json === "string") {
        const anonMessage = {
          content: json,
        };

        messageData.data = anonMessage;

        store.dispatch(updateCurrentChatMessages(messageData));
        return;
      }

      store.dispatch(updateCurrentChatMessages(messageData));
    });
    chatClient.subscribe(`/group_chat/updated_members/${chatId}`, (data) => {
      const json = JSON.parse(data.body);
      const members = json.members;

      console.log("/group_chat/updated_members", json);

      store.dispatch(updateGroupMembers(members));
    });
    chatClient.subscribe(`/group_chat/messages/${chatId}/errors`, (data) => {
      const error = JSON.parse(data.body);
      console.log("/group_chat/messages/errors: ", error);
    });
    chatClient.subscribe(`/group_chat/set_role/${chatId}/errors`, (data) => {
      const error = JSON.parse(data.body);

      console.log("/group_chat/set_role/errors: ", error);
    });
    chatClient.subscribe(`/group_chat/remove_member/${chatId}/errors`, (error) => {
      console.log(JSON.parse(error.body));
    });
    chatClient.subscribe(`/group_chat/add_member/${chatId}/errors`, (data) => {
      const error = JSON.parse(data.body);
      console.log("group_chat/add errors: ", error);
    });
    chatClient.subscribe(`/group_chat/edited/${chatId}`, (data) => {
      const editedGroup = JSON.parse(data.body);
      const groupName = editedGroup.name;

      store.dispatch(updateGroupName(editedGroup.name));
      store.dispatch(updateNameGroupInList({ chatId, groupName }));
    });
    chatClient.subscribe(`/group_chat/edited/${chatId}/errors`, (data) => {
      const error = JSON.parse(data.body);
      console.log("group_chat/edited errors: ", error);
    });
    chatClient.subscribe(`/group_chat/removed/${chatId}`, (data) => {
      const json = JSON.parse(data.body);

      console.log("group_chat/removed: ", json, chatId);

      store.dispatch(resetChats("currentGroupChat"));
      store.dispatch(filterChatsList(chatId));
    });
    chatClient.subscribe(`/group_chat/removed/${chatId}/errors`, (data) => {
      const error = JSON.parse(data.body);
      console.log("group_chat/removed errors: ", error);
    });
  };

  const onConnectPrivate = () => {
    chatClient.subscribe(`/private_chat/messages/${chatId}`, (message) => {
      const data = JSON.parse(message.body);

      console.log("/private_chat/messages/", data);
      store.dispatch(updateCurrentChatMessages({ data, type: "privateChat" }));
    });
    chatClient.subscribe(`/private_chat/messages/${chatId}/errors`, (message) => {
      console.log("private_chat error: ", JSON.parse(message.body));
    });
  };

  chatClient.configure({
    brokerURL: wsServerURL,
    onConnect: isPrivate ? onConnectPrivate : onConnectGroup,
    webSocketFactory: () => {
      return socket;
    },
    debug: (str) => {
      //console.log("debug: ", str);
    },
  });

  chatClient.activate();
}

// last_message ------------------------------------------------------
export function subscribeLastMessages(userId: string) {
  const socket: WebSocket = new SockJS(wsServerURL);

  const handleLastMessage = (data: any) => {
    const json = JSON.parse(data.body);
    const chatsType = store.getState().root.currentChatType + "s";

    console.log(json);

    store.dispatch(updateLastMessage({ ...json, chatsType }));
  };

  const onConnect = () => sidebarClient.subscribe(`/chat/last_message/${userId}`, handleLastMessage);

  sidebarClient.configure({
    brokerURL: wsServerURL,
    onConnect: onConnect,
    webSocketFactory: () => {
      return socket;
    },
    debug: (str) => {
      //console.log("debug: ", str);
    },
  });

  //console.log("last_message subscription");

  sidebarClient.activate();
}

// disconnect
export function disconnect() {
  chatClient.deactivate();
}

// SEND MESSAGES -----------------------------------------------------
export function privateChatSendMessage(
  content: string,
  chatId: string,
  createdAt: number,
  sendFileDto?: FormData
) {
  chatClient.publish({
    destination: `/app/private_chat/send_message`,
    body: JSON.stringify({
      chatId,
      content,
      createdAt,
      sendFileDto,
    }),
  });
}
export function sendGroupMessage(content: string, chatId: string, createdAt: number, sendFileDto?: FormData) {
  chatClient.publish({
    destination: `/app/group_chat/send_message`,
    body: JSON.stringify({
      chatId,
      content,
      createdAt,
      sendFileDto,
    }),
  });
}
// ADD/REMOVE GROUP MEMBERS ------------------------------------------
export function addGroupMember(chatId: string, memberId: string) {
  chatClient.publish({
    destination: `/app/group_chat/add_member`,
    body: JSON.stringify({
      chatId,
      memberId,
    }),
  });
}
export function removeGroupMember(chatId: string, memberId: string) {
  chatClient.publish({
    destination: `/app/group_chat/remove_member`,
    body: JSON.stringify({
      chatId,
      memberId,
    }),
  });
}
// ROLES ------------------------------------------------------------
export function setMemberRole(chatId: string, userToGiveRoleId: string, groupChatRole: MemberRoles) {
  chatClient.publish({
    destination: `/app/group_chat/set_role`,
    body: JSON.stringify({
      chatId,
      userToGiveRoleId,
      groupChatRole,
    }),
  });
}

export function editGroupName(chatId: string, groupName: string) {
  chatClient.publish({
    destination: "/app/group_chat/edit",
    body: JSON.stringify({
      chatId,
      groupName,
    }),
  });
}

export function leaveGroup(chatId: string) {
  chatClient.publish({
    destination: `/app/group_chat/leave/${chatId}`,
  });
}

export function removeGroup(chatId: string) {
  chatClient.publish({
    destination: `/app/group_chat/remove/${chatId}`,
  });
}
