import React from "react";
import { Box } from "@mui/material";
import { CustomIconButton } from "../CustomIconButton";
import ChatIcon from "@mui/icons-material/ChatOutlined";
import PeopleIcon from "@mui/icons-material/PeopleAltOutlined";

interface ChatModesProps {
  currentChatMode: number;
  switchChatMode: any;
}

const ChatModes: React.FC<ChatModesProps> = ({ currentChatMode, switchChatMode }) => {
  const chatModesIcons = [ChatIcon, PeopleIcon];

  return (
    <Box
      sx={{
        flexBasis: 56,
        flexShrink: 0,
        display: "flex",
        alignItems: "center",
        borderTop: "1px solid #353F75",
        borderBottom: "2px solid #353675",
        zIndex: 100,
        position: "relative",
        bgcolor: "#1F274E",
      }}>
      {chatModesIcons.map((Icon, i) => (
        <CustomIconButton
          key={i}
          onClick={() => switchChatMode(i)}
          borderRight={i == 0}
          fontSize={32}
          sx={{
            borderRadius: 0,
            bgcolor: currentChatMode === i ? "#353F75" : "",
            ":hover": {
              bgcolor: currentChatMode === i ? "#353F75" : "",
            },
            height: 56,
          }}>
          <Icon />
        </CustomIconButton>
      ))}
    </Box>
  );
};

export { ChatModes };
