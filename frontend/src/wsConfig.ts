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
      client.subscribe(`/private_chat/${chatId}`, (message) => {
        console.log("private_chat: ", JSON.parse(message.body));
        store.dispatch(getCurrentChat(chatId));
      });
      client.subscribe(`/private_chat/${chatId}/errors`, (message) => {
        console.log("private_chat error: ", JSON.parse(message.body));
      });

      client.subscribe(`/chats/${userId}`, (message) => {
        console.log("chats: ", JSON.parse(message.body));
      });
    },
    webSocketFactory: () => {
      return socket;
    },

    debug: (str) => {
      //console.log("debug: ", str);
    },
  });

  client.activate();
}

export function sendMessage(message: string, chatId: string, createdAt: number) {
  client.publish({
    destination: `/app/${chatId}`,
    body: JSON.stringify({
      chatId,
      content: message,
      createdAt: createdAt,
    }),
  });
}
