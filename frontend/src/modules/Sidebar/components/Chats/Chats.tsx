import React, { useState } from "react";
import { Box, TextField, Typography } from "@mui/material";
import { CustomIconButton } from "../CustomIconButton";
import { ChatItem } from "../ChatItem";
import ChatIcon from "@mui/icons-material/ChatOutlined";
import PeopleIcon from "@mui/icons-material/PeopleAltOutlined";
import SearchIcon from "@mui/icons-material/SearchOutlined";

interface ChatsProps {}

const chatsList = [
  { name: "Name", lastMessage: "Last message" },
  { name: "Name", lastMessage: "Last message" },
  { name: "Name", lastMessage: "Last message" },
  { name: "Name", lastMessage: "Last message" },
  { name: "Name", lastMessage: "Last message" },
  { name: "Name", lastMessage: "Last message" },
  { name: "Name", lastMessage: "Last message" },
  { name: "Name", lastMessage: "Last message" },
  { name: "Name", lastMessage: "Last message" },
  { name: "Name", lastMessage: "Last message" },
  { name: "Name", lastMessage: "Last message" },
  { name: "Name", lastMessage: "Last message" },
];

const Chats: React.FC<ChatsProps> = () => {
  const [currentChat, setCurrentChat] = React.useState(0);
  const [listHeight, setListHeight] = React.useState<number | null>(null);

  React.useEffect(() => {
    const getListHeight = () => {
      setListHeight(document.body.clientHeight - 232);
    };

    window.addEventListener("resize", getListHeight);
    getListHeight();
  }, [listHeight]);

  const onClickChat = (i: number) => {
    setCurrentChat(i);
  };

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        position: "relative",
        flexGrow: 1,
      }}>
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

      <Box
        sx={{
          overflowY: "auto",
          flexGrow: 0,
          height: listHeight,
          width: 1,
        }}>
        {chatsList.map((chat, i) => (
          <ChatItem
            onClick={() => onClickChat(i)}
            key={i}
            {...chat}
            active={i === currentChat}
          />
        ))}
      </Box>
      <Box
        sx={{
          position: "relative",
          width: 1,
          maxWidth: 464,
          bottom: 0,
          left: 0,
          bgcolor: "#1F274E",
          px: 5.5,
          borderTop: "1px solid #353F75",
          flexBasis: 80,
          flexShrink: 0,
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
        }}>
        <Box
          component="input"
          sx={{
            borderRadius: 3,
            width: 1,
            height: 36,
            bgcolor: "primary.main",
            px: 1,
            fontSize: 16,
            color: "#fff",
            ":focus ~ p": {
              opacity: 0,
            },
          }}
        />
        <Typography
          sx={{
            position: "absolute",
            top: "50%",
            left: "50%",
            display: "flex",
            gap: 0.8125,
            justifyContent: "center",
            alignItems: "center",
            transform: "translate(-50%, -50%)",
            pointerEvents: "none",
            color: "#6A73A6",
            transition: "all 0.2s ease 0s",
          }}>
          <SearchIcon /> Search
        </Typography>
      </Box>
    </Box>
  );
};

export { Chats };
