import React from "react";
import { NoChat } from "../NoChat";
import { Box } from "@mui/material";
import { Header } from "../Header";
import { ChatBlock } from "../ChatBlock";
import { WriteMessage } from "../WriteMessage";

interface CurrentChatProps {}

const CurrentChat: React.FC<CurrentChatProps> = () => {
  const chat = {};

  if (!chat) return <NoChat />;

  return (
    <>
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          width: 1,
          bgcolor: "background.paper",
        }}>
        <Header />
        <ChatBlock />
        <WriteMessage />
      </Box>
    </>
  );
};

export { CurrentChat };
