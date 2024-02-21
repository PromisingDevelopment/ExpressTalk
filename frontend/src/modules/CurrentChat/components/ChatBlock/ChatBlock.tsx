import React from "react";
import { Box, useTheme } from "@mui/material";
import { Message } from "../Message";

interface ChatBlockProps {}

const ChatBlock: React.FC<ChatBlockProps> = () => {
  const [height, setHeight] = React.useState<number | null>(null);

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

  const messages = [
    {
      content:
        "Lorem ipsum sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd ",
      time: "22:41",
    },
    {
      content: "Lorem ipsum",
      time: "Time",
      isMine: true,
    },
    {
      content:
        "Lorem ipsum sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd ",
      time: "22:41",
    },
    {
      content: "Lorem ipsum",
      time: "Time",
      isMine: true,
    },
    {
      content:
        "Lorem ipsum sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd ",
      time: "22:41",
    },
    {
      content: "Lorem ipsum",
      time: "Time",
      isMine: true,
    },
    {
      content:
        "Lorem ipsum sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd sfdsfd ",
      time: "22:41",
    },
    {
      content: "Lorem ipsum",
      time: "Time",
      isMine: true,
    },
  ];

  return (
    <Box
      sx={{
        paddingY: 4,
        display: "flex",
        flexDirection: "column",
        gap: 2,
        height: height,
        overflowY: "auto",
      }}>
      {messages.map((message, i) => (
        <Message key={message.content + message.time + i} {...message} />
      ))}
    </Box>
  );
};

export { ChatBlock };
