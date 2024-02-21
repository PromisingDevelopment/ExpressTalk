import React from "react";
import { Box } from "@mui/material";
import { CustomIconButton } from "../CustomIconButton";
import ChatIcon from "@mui/icons-material/ChatOutlined";
import PeopleIcon from "@mui/icons-material/PeopleAltOutlined";

interface ChatModesProps {}

const ChatModes: React.FC<ChatModesProps> = () => {
  return (
    <Box
      sx={{
        flexBasis: 56,
        flexShrink: 0,
        display: "flex",
        alignItems: "center",
        borderTop: "1px solid #353F75",
        borderBottom: "1px solid #353F75",
        zIndex: 100,
        position: "relative",
        bgcolor: "#1F274E",
      }}>
      <CustomIconButton borderRight fontSize={32} sx={{ borderRadius: 0 }}>
        <ChatIcon />
      </CustomIconButton>
      <CustomIconButton fontSize={32} sx={{ borderRadius: 0 }}>
        <PeopleIcon />
      </CustomIconButton>
    </Box>
  );
};

export { ChatModes };
