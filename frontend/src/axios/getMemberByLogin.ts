import { userGetUrls } from "config";
import { axiosRequest } from "./axiosRequest";

function getMemberByLogin(login: string) {
  const url = userGetUrls.user(login);
  return axiosRequest(url, "get");
}

export { getMemberByLogin };
