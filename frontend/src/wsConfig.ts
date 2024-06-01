import SockJS from "sockjs-client";
import { Client, Stomp } from "@stomp/stompjs";
import { userUrls, wsServerURL } from "config";
import axios from "axios";

let client: Client;

export async function connect() {
  let chatId = "29bdb87b-cce4-4844-985c-e1ace00674fb"
  // try {
  //   console.log("LOG OUT")
  //
  //   await axios.delete("http://localhost:8080/auth/log-out", { withCredentials: true });
  // } catch (error) {
  //   console.log("LOG OUT ERROR: " + error)
  // }

  //const loginOrId = getUserlogin(id);
  const socket: WebSocket = new SockJS(wsServerURL);

  client = new Client();

  client.configure({
    brokerURL: wsServerURL,
    onConnect: () => {
      console.log("onConnect");

      client.publish({
        destination: "/app/private_chat/send_message",
        body: JSON.stringify({
          chatId, content: "hello", createdAt: new Date().getTime()
        })
      })
      client.subscribe(`/private_chat/messages/${chatId}`, (message) => {
        console.log("private_chat response: ", message);
      });
      client.subscribe(`/private_chat/messages/${chatId}/errors`, (message) => {
        console.log("private_chat error: ", message);
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
