import { GroupChat } from "./GroupChat";
import { PrivateChat } from "./PrivateChat";

export interface ChatsListsObj {
  privateChats: PrivateChat[];
  groupChats: GroupChat[];
}
