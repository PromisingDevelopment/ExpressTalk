import { IMessage } from "./IMessage";

export interface PrivateChat {
  id: string;
  messages: IMessage[];
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
