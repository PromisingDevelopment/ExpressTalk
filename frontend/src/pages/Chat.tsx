import React from "react";
import { Outlet } from "react-router-dom";
import { Sidebar } from "../modules/Sidebar";
import { CurrentChat } from "../modules/CurrentChat";
import { Box } from "@mui/material";

interface ChatProps {}

const Chat: React.FC<ChatProps> = () => {
  return (
    <Box sx={{ display: "flex" }}>
      <Sidebar />
      <CurrentChat />
    </Box>
  );
};

export { Chat };
