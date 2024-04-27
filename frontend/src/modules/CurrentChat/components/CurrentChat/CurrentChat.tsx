import React from "react";
import { NoChat } from "../NoChat";
import { Box } from "@mui/material";
import { Header } from "../Header";
import { ChatBlock } from "../ChatBlock";
import { WriteMessage } from "../WriteMessage";
import { useAppDispatch, useAppSelector } from "hooks/redux";
import { getCurrentChat } from "../../store/currentChatSlice";

interface CurrentChatProps {}

const CurrentChat: React.FC<CurrentChatProps> = () => {
  const dispatch = useAppDispatch();
  const { data } = useAppSelector((state) => state.currentChat);
  const { currentChatId, currentUser } = useAppSelector((state) => state.root);
  const secondMember = data?.members.find(
    (member) => member.login !== currentUser.user?.login
  );

  React.useEffect(() => {
    if (currentChatId) {
      dispatch(getCurrentChat(currentChatId));
    }
  }, [currentChatId]);

  if (!data) return <NoChat />;

  return (
    <>
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          width: 1,
          bgcolor: "background.paper",
        }}>
        <Header secondMemberLogin={secondMember?.login} />
        <ChatBlock />
        <WriteMessage chatId={currentChatId} />
      </Box>
    </>
  );
};

export { CurrentChat };
