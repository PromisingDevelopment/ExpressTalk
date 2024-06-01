import { userUrls } from "config";
import { axiosRequest } from "./axiosRequest";

function getSecondMember(login: string) {
  const url = userUrls.user(login);
  return axiosRequest(url, "get");
}

export { getSecondMember };
