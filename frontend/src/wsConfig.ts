import SockJS from "sockjs-client";
import { Client, Stomp } from "@stomp/stompjs";
import { userUrls, wsServerURL } from "config";
import axios from "axios";

let client: Client;

export function connect(id: string, chatId: string) {
  //const loginOrId = getUserlogin(id);
  const socket: WebSocket = new SockJS(wsServerURL);

  client = new Client();

  client.configure({
    brokerURL: wsServerURL,
    onConnect: () => {
      console.log("onConnect");

      client.subscribe(`/private_chats/${id}`, (message) => {
        console.log("private_chat: ", message);
      });
      client.subscribe(`/private_chats/${id}/errors`, (message) => {
        console.log("private_chat: ", message);
      });

      client.subscribe(`/chats/${chatId}`, (message) => {
        console.log("chat: ", message);
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

async function getUserlogin(str: string) {
  if (str.length < 36) {
    const res = await axios.get(userUrls.user(str));

    return str;
  }

  return str;
}
