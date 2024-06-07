import React from "react";
import { Outlet } from "react-router-dom";
import { CardForm } from "../modules/RegistrationForm";
import { SignUpButtons } from "SignUpButtons";

interface AuthProps {}

const Auth: React.FC<AuthProps> = () => {
  return (
    <CardForm>
      <SignUpButtons />
      <Outlet />
    </CardForm>
  );
};

export { Auth };
