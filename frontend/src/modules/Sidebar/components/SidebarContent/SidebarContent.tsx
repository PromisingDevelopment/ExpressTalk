import React from "react";
import { Box } from "@mui/material";
import { Search } from "../Search";
import { ChatModes } from "../ChatModes";
import { ChatList } from "../ChatList";

interface SidebarContentProps {}

const SidebarContent: React.FC<SidebarContentProps> = () => {
  const [currentChatMode, setCurrentChatMode] = React.useState(0);

  const switchChatMode = (i: number) => {
    setCurrentChatMode(i);
  };

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        position: "relative",
        flexGrow: 1,
        bgcolor: "#1F274E",
      }}>
      <ChatModes switchChatMode={switchChatMode} currentChatMode={currentChatMode} />
      <ChatList currentChatMode={currentChatMode} />
      <Search />
    </Box>
  );
};

export { SidebarContent };
