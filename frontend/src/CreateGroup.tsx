import { Box } from "@mui/material";
import axios from "axios";
import { chatPostUrls } from "config";
import React from "react";
import { addGroupMember, connectGroup, sendGroupMessage } from "wsConfig";

function CreateGroup() {
  const onCreate = async () => {
    try {
      const createGroupData = await axios.post(
        chatPostUrls.groupAdd,
        { groupName: "My group" },
        {
          withCredentials: true,
        }
      );
      console.log(createGroupData);
    } catch (error) {
      console.log("error:", error);
    }
  };
  const onSendMessage = async () => {
    const content = "some message";
    const chatId = "561f4e58-fedd-4da6-9e8a-bc29ecc97a5c";
    const createdAt = new Date().getTime();

    sendGroupMessage(content, chatId, createdAt);
  };

  const onConnect = async () => {
    const groupId = "561f4e58-fedd-4da6-9e8a-bc29ecc97a5c";
    const userId = "d72ea9bd-e16d-4123-8a07-382c98324983";

    connectGroup(userId, groupId);
  };

  const onAddMember = async () => {
    const groupId = "561f4e58-fedd-4da6-9e8a-bc29ecc97a5c";
    const memberId = "311d0ff9-e1e9-4a23-953c-997dfc26daba";
    addGroupMember(groupId, memberId);
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
