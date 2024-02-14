import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import {
  RequireAuth,
  SignUp,
  HomeAuth,
  EmailVerification,
  SignIn,
} from "../modules/RegistrationForm";
import { NotFoundPage } from "./NotFoundPage";
import { Auth } from "./Auth";
import { Chat } from "./Chat";

interface MainProps {}

const Main: React.FC<MainProps> = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/"
          element={
            <RequireAuth>
              <Chat />
            </RequireAuth>
          }
        />
        <Route path="/auth" element={<Auth />}>
          <Route path="home" element={<HomeAuth />} />
          <Route path="sign-in" element={<SignIn />} />
          <Route path="sign-up" element={<SignUp />} />
          <Route path="email-verification" element={<EmailVerification />} />
        </Route>
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </BrowserRouter>
  );
};

export { Main };
