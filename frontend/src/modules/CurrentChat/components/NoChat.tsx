import { Box, styled, Typography } from "@mui/material";
import ChatImage from "assets/images/no-chat.svg";
import React from "react";
import { BurgerMenu } from "./BurgerMenu";

interface NoChatProps {}

const NoChat: React.FC<NoChatProps> = () => {
  return (
    <>
      <BurgerWrapper>
        <BurgerMenu />
      </BurgerWrapper>

      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          flexDirection: "column",
          flexGrow: 1,
          gap: 2,
          bgcolor: "background.paper",
          height: "100vh",
        }}>
        <Box component="img" src={ChatImage} />
        <Typography sx={{ fontSize: 28, color: "#6A73A6", fontWeight: 300 }}>
          Select a chat
        </Typography>
      </Box>
    </>
  );
};

const BurgerWrapper = styled("div")`
  position: absolute;
  top: 10px;
  left: 10px;
`;

export { NoChat };
