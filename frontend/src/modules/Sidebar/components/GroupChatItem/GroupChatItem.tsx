import { Box, Button, Typography, useTheme } from "@mui/material";
import React from "react";
import { Logo } from "components/Logo";
import { GroupChatMember, GroupChatMessage } from "types/GroupChat";

interface GroupChatItemProps {
  logoSrc?: string; // change to required
  name: string;
  active?: boolean;
  lastMessage?: string; // change to required
  onClick?: any; // change to required
  members: GroupChatMember[];
  messages: GroupChatMessage[];
}

const GroupChatItem: React.FC<GroupChatItemProps> = (props) => {
  console.log(props);
  const { members, messages, name, active, onClick } = props;

  const getLastMessage = () => {
    console.log(messages.length);
    if (!messages.length) {
      return "Write first message! :3";
    }

    const lastMessageObj = messages[messages.length - 1];
    const user = members.find((member, i) => member.id === lastMessageObj.senderId);

    if (user) {
      return (
        <>
          <Typography fontWeight={500}>{user.login}: </Typography>
          {lastMessageObj.content}
        </>
      );
    }
  };

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
          {getLastMessage()}
        </Typography>
      </Box>
    </Button>
  );
};

export { GroupChatItem };
