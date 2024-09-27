import { Message } from "./Message";

export interface PrivateChat {
  id: string;
  messages: Message[];
  members: PrivateMember[];
}

export interface PrivateMember {
  id: string;
  user: {
    id: string;
    name: string;
    login: string;
    status: "ONLINE" | "OFFLINE";
  };
}
