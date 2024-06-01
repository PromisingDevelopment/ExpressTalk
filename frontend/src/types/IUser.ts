export interface IUser {
  id: string;
  name: string;
  login: string;
  status: "ONLINE" | "OFFLINE";
}
