import AttachFileIcon from "@mui/icons-material/AttachFile";
import SendIcon from "@mui/icons-material/Send";
import { Box, IconButton } from "@mui/material";
import { CustomInput } from "components/CustomInput";
import { ImageFileInput } from "components/ImageFileInput";
import { useAppDispatch, useAppSelector } from "hooks/redux";
import React from "react";
import { privateChatSendMessage, sendGroupMessage } from "wsConfig";

interface WriteMessageProps {
  chatId: string | null;
}

const WriteMessage: React.FC<WriteMessageProps> = ({ chatId }) => {
  const [isHiddenAttachFile, setIsHiddenAttachFile] = React.useState(false);
  const writeMessageInputRef = React.useRef<HTMLInputElement>(null);
  const currentChatType = useAppSelector((state) => state.root.currentChatType);
  const [uploadedImage, setUploadedImage] = React.useState<FormData | null>(null);
  const [previewImageSrc, setPreviewImageSrc] = React.useState<string | null>(null);

  const onSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const writeMessageInput = writeMessageInputRef.current;
    const lastMessage = writeMessageInput?.value;

    if (lastMessage && chatId) {
      const createdAt = new Date().getTime();

      if (currentChatType === "privateChat") {
        privateChatSendMessage(lastMessage, chatId, createdAt, uploadedImage || undefined);
      } else {
        sendGroupMessage(lastMessage, chatId, createdAt, uploadedImage || undefined);
      }

      writeMessageInput.value = "";
      setIsHiddenAttachFile(false);
    }
  };
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setIsHiddenAttachFile(e.target.value !== "");
  };

  const onUploadImage = (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = e.target.files;

    if (files) {
      const image = files[0];

      const formData = new FormData();
      formData.append("sendFileDto", image);

      setUploadedImage(formData);

      const reader = new FileReader();

      reader.onload = (e) => {
        if (e.target) {
          setPreviewImageSrc(e.target.result as string);
        }
      };
      reader.onerror = (err) => {
        console.log(err);
      };

      if (image) {
        reader.readAsDataURL(image);
      }
    }
  };

  const onRemoveImage = () => {
    setPreviewImageSrc(null);
    setUploadedImage(null);
  };

  React.useEffect(() => {
    onRemoveImage();
  }, [chatId]);

  return (
    <Box
      onSubmit={onSubmit}
      component="form"
      sx={{
        bgcolor: "#1F274E",
        height: 80,
        position: "relative",
        display: "flex",
        alignItems: "center",
        px: { lg: 4.5, xs: 1.5 },
        gap: 2,
      }}>
      {previewImageSrc && (
        <Box
          sx={{
            position: "absolute",
            top: -10,
            left: 20,
            width: 80,
            height: 80,
            borderRadius: 2,
            overflow: "hidden",
            background: "#222B5A",
            transform: "translateY(-100%)",
            img: {
              width: 1,
              height: 1,
              objectFit: "cover",
            },
          }}>
          <Box
            component="button"
            onClick={onRemoveImage}
            sx={{
              background: "#fff",
              color: "primary.main",
              position: "absolute",
              top: 0,
              right: 0,
              width: 25,
              height: 25,
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              borderRadius: 1,
            }}>
            x
          </Box>
          <img src={previewImageSrc} alt="preview photo" />
        </Box>
      )}
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
          inputId="write-message-input"
        />

        <IconButton
          title="Upload image"
          sx={[
            {
              color: "#6A73A6",
              position: "absolute",
              top: "50%",
              right: 10,
              transform: "translateY(-50%)",
              transition: "opacity 0.3s ease 0s",
            },
            isHiddenAttachFile && {
              opacity: 0,
              pointerEvents: "none",
            },
          ]}>
          <ImageFileInput onUploadImage={onUploadImage} />
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
