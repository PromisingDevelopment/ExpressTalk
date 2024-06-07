import { Box } from "@mui/material";
import axios from "axios";
import { chatPostUrls } from "config";
import { useAppSelector } from "hooks/redux";
import React from "react";
import { addGroupMember, connectGroup, sendGroupMessage } from "wsConfig";

function CreateGroup() {
  const [chatId, setChatId] = React.useState<string | null>(null);
  const { user: currentUser } = useAppSelector((state) => state.root.currentUser);

  const onCreate = async () => {
    try {
      const { data: newGroup } = await axios.post(
        chatPostUrls.groupAdd,
        { groupName: "My group" },
        {
          withCredentials: true,
        }
      );
      setChatId(newGroup.id);
    } catch (error) {
      console.log("error:", error);
    }
  };
  const onSendMessage = async () => {
    if (!chatId) {
      console.log("there's no chatId");
      return;
    }

    const content = "some message";
    const createdAt = new Date().getTime();

    sendGroupMessage(content, chatId, createdAt);
  };

  const onConnect = async () => {
    const userId = currentUser?.id;

    if (!chatId) {
      console.log("there's no chatId");
      return;
    }
    if (!userId) {
      console.log("there's no userId");
      return;
    }

    connectGroup(userId, chatId);
  };

  const onAddMember = async () => {
    //const groupId = "561f4e58-fedd-4da6-9e8a-bc29ecc97a5c";
    //const memberId = "311d0ff9-e1e9-4a23-953c-997dfc26daba";
    //addGroupMember(groupId, memberId);
  };

  return (
    <Box display="flex" gap={3}>
      <button onClick={onCreate}>Create group</button>
      <button onClick={onConnect}>Connect group</button>
      <button onClick={onAddMember}>Add member</button>
      <button onClick={onSendMessage}>Send message</button>
    </Box>
  );
}

export default CreateGroup;
