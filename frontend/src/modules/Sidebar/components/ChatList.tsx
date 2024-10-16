import { Box, Typography } from "@mui/material";
import { useAppDispatch, useAppSelector } from "hooks/redux";
import { PrivateChatListItem } from "modules/Sidebar/types/PrivateChatListItem";
import React from "react";
import { useNavigate } from "react-router-dom";
import { CurrentChatType } from "types/CurrentChatType";
import { connect, disconnect, subscribeLastMessages } from "wsConfig";
import { GroupChatItem } from "./GroupChatItem";
import { PrivateChatItem } from "./PrivateChatItem";
import { getChatsList, resetChatListError, setSidebarOpen } from "../store/sidebarSlice";
import { GroupChatListItem } from "../types/GroupChatListItem";
import { setCurrentChatId, setCurrentChatType, setIsCreatedNewChat } from "redux/rootSlice";

interface ChatListProps {
  currentChatMode: number;
  filterChatsValue: string;
  subtractedHeight: number;
}

const ChatList: React.FC<ChatListProps> = ({ currentChatMode, filterChatsValue, subtractedHeight }) => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const [chatId, setChatId] = React.useState<string>();
  const [listHeight, setListHeight] = React.useState<number | null>(null);
  const [groupIndex, setGroupIndex] = React.useState<number | null>(null);
  const [isPrivateChatsList, setIsPrivateChatsList] = React.useState(true);
  const [isPrivateConnect, setIsPrivateConnect] = React.useState<boolean>(true);
  const [currentChatIndex, setCurrentChatIndex] = React.useState<number | null>(null);

  const { chatsList } = useAppSelector((state) => state.sidebar);
  const { currentChatId, isCreatedNewChat } = useAppSelector((state) => state.root);
  const currentUser = useAppSelector((state) => state.root.currentUser);

  const getFilteredChats = (type: CurrentChatType) => {
    if (type === "privateChat") {
      return chatsList.list?.privateChats.filter((chat) => {
        return chat.receiverLogin.includes(filterChatsValue);
      });
    }
    if (type === "groupChat") {
      return chatsList.list?.groupChats.filter((chat) => {
        return chat.name.includes(filterChatsValue);
      });
    }
  };

  const privateChatsList = getFilteredChats("privateChat") as PrivateChatListItem[];
  const groupChatsList = getFilteredChats("groupChat") as GroupChatListItem[];

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
    if (isCreatedNewChat) {
      setCurrentChatIndex(null);
      setGroupIndex(null);
      dispatch(getChatsList());
      dispatch(setIsCreatedNewChat(false));
    }
  }, [isCreatedNewChat]);

  React.useEffect(() => {
    if (chatsList.errorCode === 403 || chatsList.errorCode === 401) {
      navigate("/auth/home");

      return () => {
        dispatch(resetChatListError());
      };
    }
  }, [chatsList.errorCode]);

  React.useEffect(() => {
    const currentUserId = currentUser.user?.id;

    if (currentUserId) {
      subscribeLastMessages(currentUserId);
    }
  }, [currentUser.user]);

  React.useEffect(() => {
    setIsPrivateChatsList(currentChatMode === 0);
  }, [currentChatMode]);

  React.useEffect(() => {
    if (chatId) {
      dispatch(setCurrentChatId(chatId));
      connect(chatId, isPrivateConnect);
    }
  }, [currentUser.user, chatId]);

  const setEmptyContent = () => {
    if (isPrivateChatsList) {
      if (chatsList.list?.privateChats.length === 0) {
        return (
          <AlertMessage>Private chats list is empty. It's the best time to create a new chat</AlertMessage>
        );
      }
    } else {
      if (chatsList.list?.groupChats.length === 0) {
        return (
          <AlertMessage>Group chats list is empty. It's the best time to create a new chat</AlertMessage>
        );
      }
    }
  };

  const onClickChat = async (i: number, id: string) => {
    if (i === currentChatIndex) return;

    setCurrentChatIndex(i);
    setGroupIndex(null);
    setIsPrivateConnect(true);
    setChatId(id);
    dispatch(setSidebarOpen(false));
    dispatch(setCurrentChatType("privateChat"));

    if (chatId) {
      console.log("private disconnect");
      disconnect();
    }
  };

  const onClickGroup = async (i: number, id: string) => {
    if (i === groupIndex) return;

    setGroupIndex(i);
    setCurrentChatIndex(null);
    setIsPrivateConnect(false);
    setChatId(id);
    dispatch(setSidebarOpen(false));
    dispatch(setCurrentChatType("groupChat"));

    if (chatId) {
      console.log("%c group disconnect", "color: #DC143C");
      disconnect();
    }
  };

  return (
    <Box
      sx={{
        overflowY: "auto",
        overflowX: "hidden",
        flexGrow: 0,
        height: listHeight,
        width: 1,
        "@media(max-width: 767px)": {
          "@media(max-height: 400px)": {
            height: 1,
          },
        },
      }}>
      {chatsList.status === "loading" && <AlertMessage>Loading...</AlertMessage>}
      {chatsList.errorMessage && <AlertMessage>{chatsList.errorMessage} :(</AlertMessage>}

      {setEmptyContent()}

      {isPrivateChatsList
        ? privateChatsList?.map((chat, i) => (
            <PrivateChatItem
              onClick={() => onClickChat(i, chat.id)}
              key={chat.id}
              {...chat}
              active={currentChatIndex === i || currentChatId === chat.id}
            />
          ))
        : groupChatsList?.map((chat, i) => (
            <GroupChatItem
              onClick={() => onClickGroup(i, chat.id)}
              key={chat.id}
              {...chat}
              active={groupIndex === i || currentChatId === chat.id}
            />
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
