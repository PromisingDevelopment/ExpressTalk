import { CurrentPrivateChat } from "types/CurrentPrivateChat";
import { GroupChat } from "types/GroupChat";

export function isCurrentGroupChat(
  chat: CurrentPrivateChat | GroupChat | null
): chat is GroupChat {
  return chat !== null && (chat as GroupChat).name !== undefined;
}
