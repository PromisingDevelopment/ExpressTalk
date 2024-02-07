import { Box, Button, Typography } from "@mui/material";
import React from "react";
import { Logo } from "../../../../components/Logo";

interface ChatItemProps {
  logoSrc?: string; // change to required
  name: string;
  lastMessage: string;
  active?: boolean;
  onClick: any;
}

const ChatItem: React.FC<ChatItemProps> = ({
  lastMessage,
  logoSrc = "",
  active = false,
  name,
  onClick,
}) => {
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
        px: 3.125,
        py: 2,
        bgcolor: active ? "primary.main" : "",
        ":hover": {
          bgcolor: active ? "primary.main" : "",
        },
      }}>
      <Logo size={48} src={logoSrc} />
      <Box>
        <Typography sx={{ fontSize: 20, lineHeight: 1, mb: 0.5, color: "#fff" }}>
          {name}
        </Typography>
        <Typography sx={{ fontSize: 12, color: "#6A73A6" }}>{lastMessage}</Typography>
      </Box>
    </Button>
  );
};

export { ChatItem };
