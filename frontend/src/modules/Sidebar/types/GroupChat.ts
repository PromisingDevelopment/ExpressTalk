interface GroupChatMessage {
  id: string;
  senderId: string;
  content: string;
  createdAt: string;
  groupChat: string;
}
interface GroupChatMember {
  id: string;
  name: string;
  login: string;
  status: "ONLINE" | "OFFLINE";
}
interface GroupChatAdmin {
  id: string;
  name: string;
  login: string;
  status: "ONLINE";
}

export interface GroupChat {
  id: string;
  name: string;
  messages: GroupChatMessage[];
  members: GroupChatMember[];
  admins: GroupChatAdmin[];
}
