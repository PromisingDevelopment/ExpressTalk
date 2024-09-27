import { IMessage } from "./IMessage";

export interface GroupChat {
  id: string;
  name: string;
  messages: IMessage[];
  members: GroupChatMember[];
}

export interface GroupChatMember {
  groupChat: GroupChat;
  groupChatRole: "ADMIN" | "MEMBER";
  id: string;
  user: {
    id: string;
    login: string;
    name: string;
    status: "ONLINE" | "OFFLINE";
  };
}
