export interface GroupChatMessage {
  id: string;
  senderId: string;
  content: string;
  createdAt: string;
  groupChat: string;
}

export interface GroupChatMember {
  id: string;
  groupChat: string;
  groupChatRole: "ADMIN" | "MEMBER";
  groupMessages: GroupChatMessage[];
  user: {
    avatarFile: null;
    id: string;
    login: string;
    name: string;
    status: "ONLINE" | "OFFLINE";
  };
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
