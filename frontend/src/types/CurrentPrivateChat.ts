export interface CurrentPrivateChat {
  id: string;
  messages: IMessage[];
  members: IMember[];
}

export interface IMessage {
  id: string;
  senderId: string;
  receiverId: string;
  content: string;
  createdAt: string;
}
export interface IMember {
  id: string;
  name: string;
  login: string;
  status: "ONLINE" | "OFFLINE";
}
