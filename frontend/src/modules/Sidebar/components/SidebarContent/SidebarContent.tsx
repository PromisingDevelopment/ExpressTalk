import React from "react";
import { Box } from "@mui/material";
import { Search } from "../Search";
import { ChatModes } from "../ChatModes";
import { ChatList } from "../ChatList";

interface SidebarContentProps {}

const SidebarContent: React.FC<SidebarContentProps> = () => {
  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        position: "relative",
        flexGrow: 1,
        bgcolor: "#1F274E",
      }}>
      <ChatModes />
      <ChatList />
      <Search />
    </Box>
  );
};

export { SidebarContent };
