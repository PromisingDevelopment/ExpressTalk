import React from "react";
import { Box, IconButton } from "@mui/material";
import SendIcon from "@mui/icons-material/Send";
import AttachFileIcon from "@mui/icons-material/AttachFile";

interface WriteMessageProps {}

const WriteMessage: React.FC<WriteMessageProps> = () => {
  return (
    <Box
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
        <Box
          component="input"
          placeholder="Write a message"
          name="write-message-input"
          sx={{
            width: 1,
            height: 48,
            border: "1px solid #6A73A6",
            borderRadius: 3,
            py: 1,
            px: 3,
            color: "#6A73A6",
            background: "none",
            fontSize: 20,
            ":focus": {
              borderColor: "#7b83b0",
            },
            "::placeholder": {
              color: "#6A73A6",
            },
          }}
        />

        {/* When input isn't empty hide attach button */}
        <IconButton
          sx={{
            color: "#6A73A6",
            position: "absolute",
            top: "50%",
            right: 10,
            transform: "translateY(-50%)",
            bgcolor: "#1F274E",
          }}>
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
