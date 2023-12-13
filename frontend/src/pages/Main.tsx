import React, { FC } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { RequireAuth } from "../hoc/RequireAuth";
import { Auth } from "./Auth";
import { HomeAuth } from "../modules/RegistrationForm/components/HomeAuth";
import { NotFoundPage } from "./NotFoundPage";

interface MainProps {}

const Main: FC<MainProps> = () => {
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
