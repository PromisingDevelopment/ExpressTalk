import { PrivateChat } from "modules/Sidebar/types/PrivateChat";

function findPrivateChatId(chatsList: PrivateChat[], login: string) {
  return chatsList.find((chat) => chat.receiverLogin === login)?.id;
}

export { findPrivateChatId };
