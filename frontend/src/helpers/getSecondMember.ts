import { IUser } from "types/IUser";
import { PrivateMember } from "types/PrivateChat";

export function getSecondMember(chatMembers: PrivateMember[] | undefined, user: IUser | null) {
  return chatMembers?.find((member) => member.user.login !== user?.login);
}
