import GroupAddIcon from "@mui/icons-material/GroupAdd";
import { createGroupChat } from "axios/createGroupChat";
import { useAppDispatch, useAppSelector } from "hooks/redux";
import { setCurrentChat } from "modules/CurrentChat/store/currentChatSlice";
import { ModalLayout } from "components/ModalLayout";
import { setSidebarOpen } from "modules/Sidebar/store/sidebarSlice";
import React from "react";
import {
  setCurrentChatId,
  setCurrentChatType,
  setIsCreatedNewChat,
} from "redux/rootSlice";
import { connect } from "wsConfig";

interface CreateNewGroupProps {
  switchChatMode: any;
}

const CreateNewGroup: React.FC<CreateNewGroupProps> = ({ switchChatMode }) => {
  const dispatch = useAppDispatch();
  const currentUser = useAppSelector((state) => state.root.currentUser);

  const onSubmit = async (groupName: string) => {
    const newGroupData = await createGroupChat(groupName);
    const currentUserId = currentUser.user?.id;
    const chatId = newGroupData.id;

    if (chatId && currentUserId) {
      switchChatMode(1);
      dispatch(setIsCreatedNewChat(true));
      dispatch(setSidebarOpen(false));
      dispatch(setCurrentChatId(newGroupData.id));
      dispatch(setCurrentChatType("groupChat"));
      dispatch(setCurrentChat({ data: newGroupData, type: "groupChat" }));
      connect(currentUserId, chatId, false);
    }
  };

  return (
    <ModalLayout
      Icon={GroupAddIcon}
      inputLabel="Input name of the group"
      inputName="input-name-new-group"
      label="Create a new group"
      onSubmit={onSubmit}
    />
  );
};

export { CreateNewGroup };
