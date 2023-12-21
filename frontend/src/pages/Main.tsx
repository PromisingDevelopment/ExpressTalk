import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import {
  RequireAuth,
  SignUp,
  HomeAuth,
  GmailVerification,
  SignIn,
} from "../modules/RegistrationForm";
import { Auth } from "./Auth";
import { NotFoundPage } from "./NotFoundPage";

interface MainProps {}

const Main: React.FC<MainProps> = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/"
          element={
            <RequireAuth>
              {/* Основний додаток */}
              <div>ExpressTalk</div>
            </RequireAuth>
          }
        />
        <Route path="/auth" element={<Auth />}>
          <Route path="home" element={<HomeAuth />} />
          <Route path="sign-in" element={<SignIn />} />
          <Route path="sign-up" element={<SignUp />} />
          <Route path="gmail-verification" element={<GmailVerification />} />
        </Route>
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </BrowserRouter>
  );
};

export { Main };
