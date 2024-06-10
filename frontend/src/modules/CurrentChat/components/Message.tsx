import { Box, Typography, useTheme } from "@mui/material";
import LeftMessageTailImage from "assets/images/left-message-tail.svg";
import RightMessageTailImage from "assets/images/right-message-tail.svg";
import { getCurrentTimeString } from "helpers/getCurrentTimeString";
import React from "react";

interface MessageProps {
  createdAt: string;
  content: string;
  isMine?: boolean;
  login?: string;
  senderId?: string;
  isGroupMessage?: boolean;
}

const Message: React.FC<MessageProps> = ({
  isMine,
  createdAt,
  content,
  isGroupMessage,
}) => {
  const theme = useTheme();
  const isLowContent = content.length < 20;
  const isShownLogin = isGroupMessage && !isMine;

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
            minWidth: 120,
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
                  right: -1,
                  background: `url(${RightMessageTailImage}) 0 0 / auto no-repeat`,
                },
              }
            : {
                borderRadius: "10px 10px 10px 0",
                mr: 3,
                pt: ({ spacing }) => ({
                  md: spacing(2),
                  xs: spacing(1),
                }),

                "::before": {
                  left: 0,
                  background: `url(${LeftMessageTailImage}) 0 0 / auto no-repeat`,
                },
              },
          isLowContent
            ? {
                p: ({ spacing }) => ({
                  md: spacing(1, 6.25, 1, 2.5),
                  xs: spacing(1, 6, 1, 2),
                }),
              }
            : {},
          isShownLogin
            ? {
                pt: {
                  md: 3,
                  xs: 3,
                },
              }
            : {},
        ]}>
        {isShownLogin && (
          <Box
            sx={{
              position: "absolute",
              top: 4,
              fontWeight: 500,
              fontSize: 14,
              color: "#9fa8da",
            }}
            component="span">
            {"login1"}
          </Box>
        )}
        <Typography
          sx={{
            fontSize: { md: 18, xs: 16 },
          }}>
          {content}
        </Typography>
        <Box
          sx={[
            {
              fontSize: 12,
              color: isMine ? "#7a85c2" : "#6A73A6",
              position: "absolute",
              right: 10,
              bottom: 5,
              pointerEvents: "none",
            },
            isLowContent && {
              right: 10,
              bottom: 9,
            },
          ]}
          component="span">
          {getCurrentTimeString(createdAt)}
        </Box>
      </Box>
    </Box>
  );
};

export { Message };
