import { Box, Button, Typography, useTheme } from "@mui/material";
import { Logo } from "components/Logo";
import React from "react";

interface PrivateChatItemProps {
  onClick: any;
  logoSrc?: string;
  receiverLogin: string;
  lastMessage: string;
  active?: boolean;
}

const PrivateChatItem: React.FC<PrivateChatItemProps> = ({
  lastMessage,
  receiverLogin,
  onClick,
  logoSrc = "",
  active = false,
}) => {
  const theme = useTheme();

  if (!receiverLogin) return;

  return (
    <Button
      onClick={onClick}
      sx={{
        borderBottom: "1px solid #353F75",
        borderRadius: 0,
        height: 80,
        width: 1,
        display: "flex",
        justifyContent: "flex-start",
        textTransform: "none",
        gap: 1.5,
        px: { lg: 3.125, xs: 2 },
        py: 5,

        bgcolor: active ? "primary.main" : "",
        ":hover": {
          bgcolor: active ? "primary.main" : "",
        },
        [theme.breakpoints.down(767)]: {
          height: 60,
        },
      }}>
      <Logo size={48} src={logoSrc} />
      <Box textAlign="start">
        <Typography
          sx={{
            fontSize: 20,
            lineHeight: 1,
            mb: 0.5,
            color: "#fff",
            textAlign: "left",
            [theme.breakpoints.down(767)]: {
              fontSize: 16,
            },
          }}>
          {receiverLogin}
        </Typography>
        <Typography
          sx={{
            fontSize: 12,
            color: "#6A73A6",
            width: { lg: 370, md: 280, sm: 230, xs: 180 },
            textOverflow: "ellipsis",
            whiteSpace: "nowrap",
            overflow: "hidden",
          }}>
          {lastMessage || "Chat is empty so far"}
        </Typography>
      </Box>
    </Button>
  );
};

export { PrivateChatItem };
