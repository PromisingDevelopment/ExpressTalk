import ChatIcon from "@mui/icons-material/ChatOutlined";
import PeopleIcon from "@mui/icons-material/PeopleAltOutlined";
import { Box } from "@mui/material";
import React from "react";
import { ModeButton } from "./ModeButton";

interface ChatModesProps {
  currentChatMode: number;
  switchChatMode: any;
}

const ChatModes = React.forwardRef<HTMLDivElement, ChatModesProps>(
  ({ currentChatMode, switchChatMode }, ref) => {
    const chatModesIcons = [ChatIcon, PeopleIcon];

    return (
      <Box
        ref={ref}
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
          <ModeButton
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
          </ModeButton>
        ))}
      </Box>
    );
  }
);

export { ChatModes };
