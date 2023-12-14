import React from "react";
import { Box } from "@mui/material";
import { CustomLink } from "../../UI/CustomLink";
import { Title } from "../../UI/Title";

interface HomeAuthProps {}

const HomeAuth: React.FC<HomeAuthProps> = () => {
  return (
    <Box
      sx={{
        paddingY: { sm: 8.25, xs: 0 },
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        "@media (max-height: 650px)": {
          paddingY: 3,
        },
      }}>
      <Title size="large">Express Talk</Title>
      <CustomLink to="/auth/sign-in" background="grey" sx={{ mb: { sm: 6, xs: 4 } }}>
        Sign in
      </CustomLink>
      <CustomLink to="/auth/sign-up" background="transparent" outlined>
        Sign up
      </CustomLink>
    </Box>
  );
};

export { HomeAuth };
