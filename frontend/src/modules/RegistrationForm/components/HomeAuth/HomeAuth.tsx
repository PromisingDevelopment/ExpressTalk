import React from "react";
import { Box, styled, Typography } from "@mui/material";
import { CustomLink } from "../../UI/CustomLink";

interface HomeAuthProps {}

const HomeAuth: React.FC<HomeAuthProps> = () => {
  return (
    <Box
      sx={{
        pt: { sm: 3.25, xs: 0 },
        pb: { sm: 5, xs: 0 },
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
      }}>
      <StyledH1>Express Talk</StyledH1>
      <StyledP>
        Welcome to ExpressTalk! <br />
        Sign up to start conversations :)
      </StyledP>
      <CustomLink to="/auth/sign-in" background="grey" sx={{ mb: 4 }}>
        Sign in
      </CustomLink>
      <CustomLink to="/auth/sign-up" background="transparent" outlined>
        Sign up
      </CustomLink>
    </Box>
  );
};

const StyledH1 = styled("h1")`
  font-size: 46px;
  font-weight: 700;
  margin-bottom: 16px;

  @media (max-width: 900px) {
    font-size: 40px;
  }
  @media (max-width: 767px) {
    font-size: 36px;
  }
  @media (max-width: 600px) {
    font-size: 32px;
  }
`;

const StyledP = styled("p")`
  margin-bottom: 40px;
`;

export { HomeAuth };
