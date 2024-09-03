const BASE_URL = "http://localhost:8080";

export const authUrls = {
  sign_up: BASE_URL + "/auth/sign-up",
  sign_in: BASE_URL + "/auth/sign-in",
  email: BASE_URL + "/auth/email-verification",
};

export const chatPostUrls = {
  privateChat: BASE_URL + "/private_chats",
  groupAdd: BASE_URL + "/group_chats",
  groupRoles: BASE_URL + "/group_chats/roles",
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

export const userGetUrls = {
  user: (login: string) => BASE_URL + "/users/" + login,
  avatar: (avatar: string) => BASE_URL + "/users/avatar/" + avatar,
  self: BASE_URL + "/users/self",
  chats: BASE_URL + "/users/chats",
};

export const userPostUrls = {
  edit: BASE_URL + "/users/edit",
  avatar: BASE_URL + "/users/avatar",
};

export const navigateUrls = {
  sign_up: "/auth/sign-up",
  sign_in: "/auth/sign-in",
  email: "/auth/email-verification",
};

export const wsServerURL = BASE_URL + "/messaging";
