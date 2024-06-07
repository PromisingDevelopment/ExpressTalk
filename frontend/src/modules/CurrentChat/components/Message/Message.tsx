import React from "react";
import { Box, Typography, useTheme } from "@mui/material";
import LeftMessageTailImage from "assets/images/left-message-tail.svg";
import RightMessageTailImage from "assets/images/right-message-tail.svg";
import { getCurrentTimeString } from "helpers/getCurrentTimeString";

interface MessageProps {
  createdAt: string;
  content: string;
  isMine?: boolean;
}

const Message: React.FC<MessageProps> = ({ isMine, createdAt, content }) => {
  const theme = useTheme();

  return (
    <Box
      sx={[
        {
          paddingX: 5,
          [theme.breakpoints.down(767)]: {
            paddingX: 3,
          },
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
            p: ({ spacing }) => ({
              md: spacing(1, 6.25, 1, 2.5),
              xs: spacing(1, 1, 3, 2),
            }),
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
                bgcolor: "#424d87",
                ml: 3,
                "::before": {
                  right: 0,
                  background: `url(${RightMessageTailImage}) 0 0 / auto no-repeat`,
                },
              }
            : {
                borderRadius: "10px 10px 10px 0",
                mr: 3,
                "::before": {
                  left: 0,
                  background: `url(${LeftMessageTailImage}) 0 0 / auto no-repeat`,
                },
              },
        ]}>
        <Typography
          sx={{
            fontSize: { md: 18, xs: 16 },
          }}>
          {content}
        </Typography>
        <Box
          sx={{
            fontSize: 12,
            color: isMine ? "#7a85c2" : "#6A73A6",
            position: "absolute",
            right: 10,
            bottom: 5,
            pointerEvents: "none",
          }}
          component="span">
          {getCurrentTimeString(createdAt)}
        </Box>
      </Box>
    </Box>
  );
};

export { Message };
