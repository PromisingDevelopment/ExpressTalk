import React from "react";
import { Box, IconButton } from "@mui/material";
import SendIcon from "@mui/icons-material/Send";
import AttachFileIcon from "@mui/icons-material/AttachFile";
import { CustomInput } from "components/CustomInput";

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
        <CustomInput label="Write a message" name="write-message-input" />

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
