import { GroupChat } from "../../../types/GroupChat";
import { PrivateChat } from "./PrivateChat";

export interface ChatsListsObj {
  privateChats: PrivateChat[];
  groupChats: GroupChat[];
}
