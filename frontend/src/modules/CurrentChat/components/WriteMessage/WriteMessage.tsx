import React from "react";
import { Box, IconButton } from "@mui/material";
import SendIcon from "@mui/icons-material/Send";
import AttachFileIcon from "@mui/icons-material/AttachFile";
import { CustomInput } from "components/CustomInput";
import { privateChatSendMessage, sendGroupMessage } from "wsConfig";
import { useAppDispatch, useAppSelector } from "hooks/redux";
import { updateLastMessage } from "modules/Sidebar";

interface WriteMessageProps {
  chatId: string | null;
}

const WriteMessage: React.FC<WriteMessageProps> = ({ chatId }) => {
  const [isHiddenAttachFile, setIsHiddenAttachFile] = React.useState(false);
  const writeMessageInputRef = React.useRef<HTMLInputElement>(null);
  const dispatch = useAppDispatch();
  const currentChatType = useAppSelector((state) => state.root.currentChatType);

  const onSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const writeMessageInput = writeMessageInputRef.current;
    const lastMessage = writeMessageInput?.value;

    if (lastMessage && chatId) {
      const createdAt = new Date().getTime();

      if (currentChatType === "privateChat") {
        privateChatSendMessage(lastMessage, chatId, createdAt);
      } else {
        sendGroupMessage(lastMessage, chatId, createdAt);
      }
      dispatch(updateLastMessage({ lastMessage, chatId }));
      writeMessageInput.value = "";
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setIsHiddenAttachFile(e.target.value !== "");
  };

  return (
    <Box
      onSubmit={onSubmit}
      component="form"
      sx={{
        bgcolor: "#1F274E",
        height: 80,
        display: "flex",
        alignItems: "center",
        px: { lg: 4.5, xs: 1.5 },
        gap: 2,
      }}>
      <Box
        sx={{
          flexGrow: 1,
          position: "relative",
        }}>
        <CustomInput
          onChange={handleInputChange}
          ref={writeMessageInputRef}
          label="Write a message"
          name="write-message-input"
        />

        <IconButton
          sx={[
            {
              color: "#6A73A6",
              position: "absolute",
              top: "50%",
              right: 10,
              transform: "translateY(-50%)",
              bgcolor: "#1F274E",
              transition: "opacity 0.3s ease 0s",
            },
            isHiddenAttachFile && {
              opacity: 0,
              pointerEvents: "none",
            },
          ]}>
          <AttachFileIcon
            sx={{
              fontSize: 30,
              transform: "rotate(45deg)",
            }}
          />
        </IconButton>
      </Box>
      <IconButton
        type="submit"
        sx={{
          color: "#6A73A6",
        }}>
        <SendIcon
          sx={{
            fontSize: 35,
          }}
        />
      </IconButton>
    </Box>
  );
};

export { WriteMessage };
