import { chatPostUrls } from "config";
import { axiosRequest } from "./axiosRequest";

function createGroupChat(groupName: string) {
  return axiosRequest(chatPostUrls.groupAdd, "post", {
    groupName,
  });
}

export { createGroupChat };
