import React from "react";
import { Outlet } from "react-router-dom";
import { CardForm } from "../modules/RegistrationForm";

interface AuthProps {}

const Auth: React.FC<AuthProps> = () => {
  return (
    <CardForm>
      <Outlet />
    </CardForm>
  );
};

export { Auth };
