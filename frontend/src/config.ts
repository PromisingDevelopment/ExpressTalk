const BASE_URL = "http://localhost:8080";

export const requestUrls = {
  sign_up: BASE_URL + "/auth/sign-up",
  sign_in: BASE_URL + "/auth/sign-in",
  email: BASE_URL + "/auth/email-verification",
};

export const navigateUrls = {
  sign_up: "/auth/sign-up",
  sign_in: "/auth/sign-in",
  email: "/auth/email-verification",
};
