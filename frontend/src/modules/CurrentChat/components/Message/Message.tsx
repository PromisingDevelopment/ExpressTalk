import React from "react";
import { Box, Typography } from "@mui/material";
import LeftMessageTailImage from "assets/images/left-message-tail.svg";
import RightMessageTailImage from "assets/images/right-message-tail.svg";

interface MessageProps {
  time: string;
  content: string;
  isMine?: boolean;
}

const Message: React.FC<MessageProps> = ({ isMine, time, content }) => {
  return (
    <Box
      sx={[
        {
          paddingX: 5,
        },
        isMine
          ? { display: "flex", flexDirection: "column", alignItems: "flex-end" }
          : {},
      ]}>
      <Box
        sx={[
          {
            bgcolor: "primary.main",
            width: "fit-content",
            maxWidth: 348,
            p: ({ spacing }) => spacing(1, 6.25, 1, 2.5),
            fontSize: 20,
            borderRadius: "10px 10px 10px 0",
            position: "relative",
            "::before": {
              content: "''",
              position: "absolute",
              width: 13,
              height: 10,
              bottom: 1,
              transform: "translateY(100%)",
            },
          },
          isMine
            ? {
                borderRadius: "10px 10px 0 10px",
                "::before": {
                  right: 0,
                  background: `url(${RightMessageTailImage}) 0 0 / auto no-repeat`,
                },
              }
            : {
                borderRadius: "10px 10px 10px 0",
                "::before": {
                  left: 0,
                  background: `url(${LeftMessageTailImage}) 0 0 / auto no-repeat`,
                },
              },
        ]}>
        <Typography fontSize={18}>{content}</Typography>
        <Box
          sx={{
            fontSize: 12,
            color: "#6A73A6",
            position: "absolute",
            right: 10,
            bottom: 5,
            pointerEvents: "none",
          }}
          component="span">
          {time}
        </Box>
      </Box>
    </Box>
  );
};

export { Message };
