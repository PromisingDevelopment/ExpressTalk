import { Box } from "@mui/material";
import React, { FC } from "react";
import { CustomLink } from "../../UI/CustomLink";
import { Title } from "../../UI/Title";

interface HomeAuthProps {}

const HomeAuth: FC<HomeAuthProps> = () => {
  return (
    <Box
      sx={{
        paddingY: 8.25,
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
      }}>
      <Title size="large">Express Talk</Title>
      <CustomLink to="/auth/sign-in" background="grey" sx={{ mb: 6 }}>
        Sign in
      </CustomLink>
      <CustomLink to="/auth/sign-up" background="transparent" outlined>
        Sign up
      </CustomLink>
    </Box>
  );
};

export { HomeAuth };
