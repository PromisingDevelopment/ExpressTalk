import { PrivateChat } from "modules/Sidebar/types/PrivateChat";

function findPrivateChatId(chatsList: PrivateChat[], login: string) {
  const chat = chatsList.find((chat) => {
    return chat.receiverLogin === login;
  });

  return chat?.id;
}

export { findPrivateChatId };
