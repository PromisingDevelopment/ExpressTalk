import React from "react";
import { Box, Typography, useTheme } from "@mui/material";
import { Message } from "../Message";
import { useAppDispatch, useAppSelector } from "hooks/redux";
import { updateLastMessage } from "modules/Sidebar/store/sidebarSlice";

interface ChatBlockProps {}

const ChatBlock: React.FC<ChatBlockProps> = () => {
  const [height, setHeight] = React.useState<number | null>(null);
  const { data, errorMessage } = useAppSelector((state) => state.currentChat);
  const { user } = useAppSelector((state) => state.root.currentUser);
  const chatBlockRef = React.useRef<HTMLDivElement | null>(null);
  const dispatch = useAppDispatch();
  const { currentChatId } = useAppSelector((state) => state.root);

  React.useEffect(() => {
    const getListHeight = () => {
      const bodyWidth = document.body.clientWidth;

      if (bodyWidth > 767) {
        setHeight(document.body.clientHeight - (96 + 80));
      } else {
        setHeight(document.body.clientHeight - (70 + 80));
      }
    };
    window.addEventListener("resize", getListHeight);
    getListHeight();
  }, [height]);
  React.useEffect(() => {
    const chatBlock = chatBlockRef.current;

    if (chatBlock) {
      chatBlock.scrollTop = chatBlock.scrollHeight;
    }
  }, [chatBlockRef.current, data]);

  React.useEffect(() => {
    if (data && currentChatId) {
      const lastMessage = data.messages[data.messages.length - 1].content;

      dispatch(updateLastMessage({ lastMessage, chatId: currentChatId }));
    }
  }, [data]);

  return (
    <Box
      ref={chatBlockRef}
      sx={{
        paddingY: 4,
        display: "flex",
        flexDirection: "column",
        gap: 2,
        height: height,
        overflowY: "auto",
      }}>
      {errorMessage && <Typography>{errorMessage}</Typography>}

      {user &&
        data?.messages?.map((message, i) => (
          <Message
            key={message.content + message.createdAt + i}
            {...message}
            isMine={message.senderId === user.id}
          />
        ))}
    </Box>
  );
};

export { ChatBlock };
