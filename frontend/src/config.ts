const BASE_URL = "http://localhost:8080";

export const authUrls = {
  sign_up: BASE_URL + "/auth/sign-up",
  sign_in: BASE_URL + "/auth/sign-in",
  email: BASE_URL + "/auth/email-verification",
};

export const chatPostUrls = {
  privateChat: BASE_URL + "/private_chats",
  groupRoles: BASE_URL + "/group_chats/roles",
  groupAdd: BASE_URL + "/group_chats/group",
};

export const chatGetUrls = {
  chatsList: BASE_URL + "/users/chats",
  privateChat: (id: string) => BASE_URL + "/private_chats/" + id,
  groupChat: (id: string) => BASE_URL + "/group_chats/" + id,
};

export const chatDeleteUrls = {
  logOut: BASE_URL + "/auth/log-out",
  removeGroup: BASE_URL + "/chats/group/remove",
};

export const userUrls = {
  user: (login: string) => BASE_URL + "/users/" + login,
  self: BASE_URL + "/users/self",
};

export const navigateUrls = {
  sign_up: "/auth/sign-up",
  sign_in: "/auth/sign-in",
  email: "/auth/email-verification",
};

export const wsServerURL = BASE_URL + "/messaging";
