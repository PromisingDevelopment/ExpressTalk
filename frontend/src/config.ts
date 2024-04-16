const BASE_URL = "http://localhost:8080";

export const authUrls = {
  sign_up: BASE_URL + "/auth/sign-up",
  sign_in: BASE_URL + "/auth/sign-in",
  email: BASE_URL + "/auth/email-verification",
};

export const chatPostUrls = {
  privateChat: BASE_URL + "/chats/private",
  groupChat: BASE_URL + "/chats/group",
  groupRoles: BASE_URL + "/chats/group/roles",
  groupAdd: BASE_URL + "/chats/group/add",
};

export const chatGetUrls = {
  chatsList: BASE_URL + "/chats",
  privateChat: (id: string) => BASE_URL + "/chats/private/" + id,
  groupChat: (id: string) => BASE_URL + "/chats/group/" + id,
};

export const chatDeleteUrls = {
  logOut: BASE_URL + "/chats/log-out",
  removeGroup: BASE_URL + "/chats/group/remove",
};

export const userUrls = {
  user: (id: string) => BASE_URL + "/users/" + id,
};

export const navigateUrls = {
  sign_up: "/auth/sign-up",
  sign_in: "/auth/sign-in",
  email: "/auth/email-verification",
};

export const wsServerURL = BASE_URL + "/messaging";
