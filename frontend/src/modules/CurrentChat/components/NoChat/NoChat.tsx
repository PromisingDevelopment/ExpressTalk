import React from "react";
import { Box, Typography } from "@mui/material";
import ChatImage from "../../../../assets/images/no-chat.svg";

interface NoChatProps {}

const NoChat: React.FC<NoChatProps> = () => {
  return (
    <Box
      sx={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        flexDirection: "column",
        flexGrow: 1,
        gap: 2,
        bgcolor: "background.paper",
      }}>
      <Box component="img" src={ChatImage} />
      <Typography sx={{ fontSize: 28, color: "#6A73A6", fontWeight: 300 }}>
        Select a chat
      </Typography>
    </Box>
  );
};

export { NoChat };
