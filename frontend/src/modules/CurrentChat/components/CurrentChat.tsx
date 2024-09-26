import { Box } from "@mui/material";
import { useAppDispatch, useAppSelector } from "hooks/redux";
import React from "react";
import { ChatBlock } from "./ChatBlock";
import { Header } from "./Header";
import { NoChat } from "./NoChat";
import { WriteMessage } from "./WriteMessage";
import { getCurrentChat } from "../store/currentChatSlice";
import { getSecondMember } from "helpers/getSecondMember";

interface CurrentChatProps {}

const CurrentChat: React.FC<CurrentChatProps> = () => {
  const dispatch = useAppDispatch();
  const currentChat = useAppSelector((state) => state.currentChat.currentChat);
  const currentGroupChat = useAppSelector((state) => state.currentChat.currentGroupChat);
  const { currentChatId, currentUser, isCreatedNewChat, currentChatType } = useAppSelector(
    (state) => state.root
  );

  const secondMember = getSecondMember(currentChat?.members, currentUser.user);

  React.useEffect(() => {
    if (isCreatedNewChat) return;

    if (currentChatId) {
      dispatch(getCurrentChat({ id: currentChatId, type: currentChatType }));
    }
  }, [currentChatId]);

  React.useEffect(() => {
    console.log("group: ", currentGroupChat);
    console.log("private: ", currentChat);
  }, [currentChat, currentGroupChat]);

  if (!currentChat && !currentGroupChat) return <NoChat />;

  return (
    <>
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          width: 1,
          bgcolor: "background.paper",
        }}>
        <Header login={secondMember?.user.login} />
        <ChatBlock />
        <WriteMessage chatId={currentChatId} />
      </Box>
    </>
  );
};

export { CurrentChat };
