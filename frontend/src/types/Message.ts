interface MessageDetails {
  attachedFile: AttachedFile;
  senderId: string;
  senderLogin: string;
}

interface AttachedFile {
  id: string;
  name: string;
  type: string;
  data: string[];
  message: {
    id: string;
    content: string;
    createdAt: string;
  };
}

interface PrivateMessage {
  privateMessageDetailsDto: MessageDetails;
  groupMessageDetailsDto?: never;
}

interface GroupMessage {
  groupMessageDetailsDto: MessageDetails & { senderRole: "ADMIN" | "MEMBER" };
  privateMessageDetailsDto?: never;
}

export type Message = (PrivateMessage | GroupMessage) & {
  isSystemMessage: boolean;
  messageDto: {
    content: string;
    createdAt: string;
    messageId: string;
  };
};
