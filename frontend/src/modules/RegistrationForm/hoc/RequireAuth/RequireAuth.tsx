import React from "react";
import { Navigate } from "react-router-dom";

interface RequireAuthProps {
  children: React.ReactElement;
}

const RequireAuth: React.FC<RequireAuthProps> = ({ children }) => {
  const isAuth = false;

  if (!isAuth) {
    return <Navigate to={"/auth/home"} replace />;
  }

  return children;
};

export { RequireAuth };
