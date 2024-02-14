import React from "react";
import { Box } from "@mui/material";
import { Message } from "../Message";

interface ChatBlockProps {}

const ChatBlock: React.FC<ChatBlockProps> = () => {
  const [height, setHeight] = React.useState<number | null>(null);

  React.useEffect(() => {
    const getListHeight = () => {
      setHeight(document.body.clientHeight - (96 + 92));
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
        gap: 3,
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
