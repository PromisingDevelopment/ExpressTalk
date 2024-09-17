import { Message } from "./Message";

export interface PrivateChat {
  id: string;
  systemMessages: any; // прибрати
  messages: Message<PrivateMember, PrivateMember>[];
  members: PrivateMember[];
}

export interface PrivateMember {
  id: string;
  sentMessages: string[]; // прибрати
  receivedMessages: string[]; // прибрати
  user: {
    id: string;
    name: string;
    login: string;
    status: "ONLINE" | "OFFLINE";
  };
}
