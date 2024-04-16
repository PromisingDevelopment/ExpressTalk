interface GroupChatMessage {
  id: string;
  senderId: string;
  content: string;
  createdAt: string;
  groupChat: string;
}

export interface GroupChat {
  id: string;
  senderId: string;
  messages: GroupChatMessage[];
  name: {
    id: string;
    name: string;
    groupSiblings: string[];
  };
}
