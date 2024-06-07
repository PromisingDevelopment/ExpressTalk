import { GroupChatListItem } from "./GroupChatListItem";
import { PrivateChatListItem } from "./PrivateChatListItem";

export interface ChatsListObj {
  privateChats: PrivateChatListItem[];
  groupChats: GroupChatListItem[];
}
