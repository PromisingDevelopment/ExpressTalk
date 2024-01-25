const BASE_URL = "http://localhost:8080";
const REQUEST_AUTH_URL = BASE_URL + "/auth";

export const requestUrls = {
  sign_up: REQUEST_AUTH_URL + "/sign-up",
  sign_in: REQUEST_AUTH_URL + "/sign-in",
  email: REQUEST_AUTH_URL + "/email-verification",
};

export const navigateUrls = {
  sign_up: "/auth/sign-up",
  sign_in: "/auth/sign-in",
  email: "/auth/email-verification",
};
