import React, { FC } from "react";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { RequireAuth } from "../hoc/RequireAuth";
import { Auth } from "./Auth";

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
          {/* Кроки авторизації */}
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export { Main };
