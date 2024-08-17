import CreateIcon from "@mui/icons-material/CreateRounded";
import { createPrivateChat } from "axios/createPrivateChat";
import { getMemberByLogin } from "axios/getMemberByLogin";
import { ModalLayout } from "components/ModalLayout";
import { useAppDispatch, useAppSelector } from "hooks/redux";
import { setCurrentChat } from "modules/CurrentChat";
import React from "react";
import { connect } from "wsConfig";
import { setSidebarOpen } from "../store/sidebarSlice";
import {
  setCurrentChatId,
  setCurrentChatType,
  setIsCreatedNewChat,
} from "redux/rootSlice";

interface CreateNewChatProps {
  switchChatMode: any;
}

const CreateNewChat: React.FC<CreateNewChatProps> = ({ switchChatMode }) => {
  const dispatch = useAppDispatch();
  const { currentUser } = useAppSelector((state) => state.root);

  const onSubmit = async (input: string) => {
    const secondMember = await getMemberByLogin(input);
    const currentUserId = currentUser.user?.id;

    if (secondMember.id) {
      const createdPrivateChat = await createPrivateChat(secondMember.id);
      const chatId = createdPrivateChat.id;

      if (chatId && currentUserId) {
        switchChatMode(0);
        connect(chatId, true);
        dispatch(setIsCreatedNewChat(true));
        dispatch(setCurrentChat({ data: createdPrivateChat, type: "privateChat" }));
        dispatch(setCurrentChatId(chatId));
        dispatch(setCurrentChatType("privateChat"));
        dispatch(setSidebarOpen(false));
      }
    }
  };

  return (
    <ModalLayout
      Icon={CreateIcon}
      label="Create a new chat"
      inputLabel="Input user id or login"
      inputName="create-new-chat-userid"
      onSubmit={onSubmit}
    />
  );
};

export { CreateNewChat };
