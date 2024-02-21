import React from "react";
import { Box } from "@mui/material";
import { ChatItem } from "../ChatItem";
import { useAppDispatch, useAppSelector } from "hooks/redux";
import { getChatsList, setSidebarOpen } from "modules/Sidebar/store/sidebarSlice";
import { useNavigate } from "react-router-dom";

interface ChatListProps {}

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

const ChatList: React.FC<ChatListProps> = () => {
  const [listHeight, setListHeight] = React.useState<number | null>(null);
  const [currentChat, setCurrentChat] = React.useState(0);
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { errorMessage } = useAppSelector((state) => state.sidebar);

  React.useEffect(() => {
    const getListHeight = () => {
      const bodyWidth = document.body.clientWidth;

      if (bodyWidth > 767) {
        setListHeight(document.body.clientHeight - (96 + 56 + 80));
      } else {
        setListHeight(document.body.clientHeight - (71 + 56 + 80));
      }
    };

    window.addEventListener("resize", getListHeight);
    if (errorMessage) {
      return navigate("/auth/home");
    }

    getListHeight();
  }, [listHeight, errorMessage]);

  const onClickChat = (i: number) => {
    setCurrentChat(i);
    dispatch(setSidebarOpen(false));
  };

  React.useEffect(() => {
    dispatch(getChatsList());
  }, []);

  return (
    <Box
      sx={{
        overflowY: "auto",
        flexGrow: 0,
        height: listHeight,
        width: 1,
        "@media(max-width: 767px)": {
          "@media(max-height: 400px)": {
            height: 1,
          },
        },
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
  );
};

export { ChatList };
