import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { RequireAuth } from "../modules/RegistrationForm";
import { HomeAuth } from "../modules/RegistrationForm";
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
        </Route>
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </BrowserRouter>
  );
};

export { Main };
