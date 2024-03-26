import React from "react";
import { Box, Typography } from "@mui/material";
import { useAppDispatch, useAppSelector } from "hooks/redux";
import { getChatsList, setSidebarOpen } from "modules/Sidebar/store/sidebarSlice";
import { useNavigate } from "react-router-dom";
import { GroupChatItem } from "../GroupChatItem";
import { PrivateChatItem } from "../PrivateChatItem";

interface ChatListProps {
  currentChatMode: number;
}

const ChatList: React.FC<ChatListProps> = ({ currentChatMode }) => {
  const [listHeight, setListHeight] = React.useState<number | null>(null);
  const [currentChat, setCurrentChat] = React.useState(0);
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { errorMessage, list, status, errorCode } = useAppSelector(
    (state) => state.sidebar.chatsList
  );

  const isPrivateChatsList = currentChatMode === 0;

  const setIsEmptyContent = () => {
    if (isPrivateChatsList) {
      if (list?.privateChats.length === 0) {
        return (
          <AlertMessage>
            Private chats list is empty. It's the best time to create a new chat
          </AlertMessage>
        );
      }
    } else {
      if (list?.groupChats.length === 0) {
        return (
          <AlertMessage>
            Group chats list is empty. It's the best time to create a new chat
          </AlertMessage>
        );
      }
    }
  };

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

    getListHeight();
  }, [listHeight]);

  const onClickChat = (i: number) => {
    setCurrentChat(i);
    dispatch(setSidebarOpen(false));
  };

  React.useEffect(() => {
    dispatch(getChatsList());
  }, []);

  React.useEffect(() => {
    if (errorCode === 403) {
      navigate("/auth/home");
    }
  }, [errorCode]);

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
      {status === "loading" && <AlertMessage>Loading...</AlertMessage>}
      {errorMessage && <AlertMessage>{errorMessage} :(</AlertMessage>}

      {setIsEmptyContent()}

      {isPrivateChatsList
        ? list?.privateChats.map((chat, i) => (
            <PrivateChatItem
              onClick={() => onClickChat(i)}
              key={i}
              {...chat}
              active={i === currentChat}
            />
          ))
        : list?.groupChats.map((chat, i) => (
            <div></div>
            //<GroupChatItem
            //  onClick={() => onClickChat(i)}
            //  key={i}
            //  {...chat}
            //  active={i === currentChat}
            ///>
          ))}
    </Box>
  );
};

const AlertMessage: React.FC<{ children: React.ReactNode }> = ({ children }) => (
  <Typography m={1} sx={{ fontSize: { xs: 16, md: 20 } }}>
    {children}
  </Typography>
);

export { ChatList };
