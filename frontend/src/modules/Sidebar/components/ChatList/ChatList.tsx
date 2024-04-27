import React from "react";
import { Box, Typography } from "@mui/material";
import { useAppDispatch, useAppSelector } from "hooks/redux";
import { getChatsList, setSidebarOpen } from "modules/Sidebar/store/sidebarSlice";
import { useNavigate } from "react-router-dom";
import { GroupChatItem } from "../GroupChatItem";
import { PrivateChatItem } from "../PrivateChatItem";
import { connect } from "wsConfig";
import { findPrivateChatId } from "helpers/findPrivateChatId";
import { getSecondMember, setCurrentChatId } from "redux/rootSlice";
import { PrivateChat } from "modules/Sidebar/types/PrivateChat";
import { GroupChat } from "modules/Sidebar/types/GroupChat";

interface ChatListProps {
  currentChatMode: number;
  filterChatsValue: string;
  subtractedHeight: number;
}

const ChatList: React.FC<ChatListProps> = ({
  currentChatMode,
  filterChatsValue,
  subtractedHeight,
}) => {
  const [listHeight, setListHeight] = React.useState<number | null>(null);
  const [chatId, setChatId] = React.useState<string>();
  const [currentChatIndex, setCurrentChatIndex] = React.useState<number | null>(null);
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const [isPrivateChatsList, setIsPrivateChatsList] = React.useState(true);
  const { errorMessage, list, status, errorCode } = useAppSelector(
    (state) => state.sidebar.chatsList
  );
  const currentUser = useAppSelector((state) => state.root.currentUser);
  const currentChatId = useAppSelector((state) => state.root.currentChatId);
  const getFilteredChats = (type: "private" | "group") => {
    if (type === "private") {
      return list?.privateChats.filter((chat) => {
        return chat.receiverLogin.includes(filterChatsValue);
      });
    } else {
      return list?.groupChats.filter((chat) => {
        return chat.name.includes(filterChatsValue);
      });
    }
  };

  React.useEffect(() => {
    const getListHeight = () => {
      setListHeight(document.body.clientHeight - subtractedHeight);
    };

    window.addEventListener("resize", getListHeight);

    getListHeight();
  }, [subtractedHeight]);

  React.useEffect(() => {
    dispatch(getChatsList());
  }, []);

  React.useEffect(() => {
    if (errorCode === 403) {
      navigate("/auth/home");
    }
  }, [errorCode]);

  React.useEffect(() => {
    setIsPrivateChatsList(currentChatMode === 0);
  }, [currentChatMode]);

  React.useEffect(() => {
    const currentUserId = currentUser.user?.id;

    if (chatId && currentUserId) {
      dispatch(setCurrentChatId(chatId));
      connect(currentUserId, chatId);
    }
  }, [currentUser, chatId]);

  const setEmptyContent = () => {
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

  const onClickChat = async (i: number, login: string) => {
    if (i === currentChatIndex) return;

    setCurrentChatIndex(i);

    if (!list?.privateChats) return;

    const id = findPrivateChatId(list.privateChats, login);
    setChatId(id);

    dispatch(getSecondMember(login));

    dispatch(setSidebarOpen(false));
  };

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

      {setEmptyContent()}

      {isPrivateChatsList
        ? (getFilteredChats("private") as PrivateChat[])?.map((chat, i) => (
            <PrivateChatItem
              onClick={() => onClickChat(i, chat.receiverLogin)}
              key={i}
              {...chat}
              active={currentChatIndex === i || currentChatId === chat.id}
            />
          ))
        : (getFilteredChats("group") as GroupChat[])?.map((chat, i) => (
            <div></div>
            //<GroupChatItem
            //  onClick={() => onClickChat(i)}
            //  key={i}
            //  {...chat}
            //  active={currentChatIndex === i}
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
