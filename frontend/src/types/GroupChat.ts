import { IMessage } from "./IMessage";

export interface GroupChat {
  id: string;
  systemMessages: any; // прибрати
  name: string;
  messages: IMessage[];
  members: GroupChatMember[];
}

export interface GroupChatMember {
  id: string;
  groupChatRole: "ADMIN" | "MEMBER";
  groupMessages: any; // прибрати
  groupChat: string; // прибрати
  user: {
    avatarFile: any; // додати
    id: string;
    name: string;
    login: string;
    status: "ONLINE" | "OFFLINE";
  };
}
