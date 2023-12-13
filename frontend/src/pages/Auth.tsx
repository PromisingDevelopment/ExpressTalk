import React, { FC } from "react";
import { Outlet, Route } from "react-router-dom";
import { CardForm } from "../modules/RegistrationForm";
import { Box } from "@mui/material";

interface AuthProps {}

const Auth: FC<AuthProps> = () => {
  return (
    <Box
      sx={{
        height: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
      }}>
      <CardForm>
        <Outlet />
      </CardForm>
    </Box>
  );
};

export { Auth };
