import { chatPostUrls } from "config";
import { axiosRequest } from "./axiosRequest";

function createPrivateChat(secondMemberId: string) {
  const data = { secondMemberId: secondMemberId };
  return axiosRequest(chatPostUrls.privateChat, "post", data);
}

export { createPrivateChat };
