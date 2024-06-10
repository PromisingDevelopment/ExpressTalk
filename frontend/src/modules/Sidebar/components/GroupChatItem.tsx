import { Box, Button, Typography, useTheme } from "@mui/material";
import { Logo } from "components/Logo";
import React from "react";

interface GroupChatItemProps {
  logoSrc?: string; // change to required
  name: string;
  active?: boolean;
  lastMessage: string;
  onClick: any;
}

const GroupChatItem: React.FC<GroupChatItemProps> = (props) => {
  const { name, lastMessage, active, onClick } = props;
  const theme = useTheme();

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
        textAlign: "left",

        bgcolor: active ? "primary.main" : "",
        ":hover": {
          bgcolor: active ? "primary.main" : "",
        },
        [theme.breakpoints.down(767)]: {
          height: 60,
        },
      }}>
      <Logo size={48} src={""} />
      <Box>
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
          {name}
        </Typography>
        <Typography sx={{ fontSize: 12, color: "#6A73A6" }}>
          {lastMessage || "Chat is empty so far"}
        </Typography>
      </Box>
    </Button>
  );
};

export { GroupChatItem };
