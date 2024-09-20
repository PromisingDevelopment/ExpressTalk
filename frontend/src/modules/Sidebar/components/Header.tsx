import React from "react";
import { Box, Typography, useTheme } from "@mui/material";
import { Logo } from "components/Logo";
import { useAppDispatch, useAppSelector } from "hooks/redux";
import { getCurrentUser, getUserAvatar } from "redux/rootSlice";
import { CreateNewChat } from "./CreateNewChat";
import { CreateNewGroup } from "./CreateNewGroup";
import { Logout } from "./Logout";
import { UserProfile } from "./UserProfile";

interface HeaderProps {
  switchChatMode: any;
}

const Header = React.forwardRef<HTMLDivElement, HeaderProps>(({ switchChatMode }, ref) => {
  const theme = useTheme();
  const dispatch = useAppDispatch();
  const { status, user } = useAppSelector((state) => state.root.currentUser);
  const [openProfile, setOpenProfile] = React.useState(false);

  const onOpenProfile = () => {
    setOpenProfile(true);
  };

  React.useEffect(() => {
    dispatch(getCurrentUser());
  }, []);

  React.useEffect(() => {
    if (user) {
      dispatch(getUserAvatar(user.id));
    }
  }, [user]);

  return (
    <Box
      ref={ref}
      sx={{
        display: "flex",
        alignItems: "center",
        gap: { md: 2, xs: 1 },

        flexBasis: 96,
        flexShrink: 0,
        px: { xs: 1, lg: 2 },
        py: 1,
        bgcolor: "#1F274E",
        [theme.breakpoints.down(767)]: {
          flexBasis: 70,
          px: 1,
        },
      }}>
      <Box
        sx={{
          cursor: "pointer",
          display: "flex",
          flexGrow: 1,
          alignItems: "center",
          gap: { md: 2, xs: 1 },
        }}
        onClick={onOpenProfile}>
        <Logo isMain size={64} />
        <Typography
          sx={{
            fontSize: { lg: 24, md: 18, sm: 16, xs: 14 },
            lineHeight: 1.3,
            textTransform: "capitalize",
            wordBreak: "break-all",
          }}>
          {status === "error" && "Guest"}
          {status === "loading" && "Loading..."}
          {user && user.login}
        </Typography>
      </Box>

      {user && (
        <UserProfile
          userData={{ user, avatar: "" }}
          setOpenProfile={setOpenProfile}
          openProfile={openProfile}
        />
      )}

      <Box display="flex" gap={{ xs: 0.5, lg: 1 }}>
        <CreateNewGroup switchChatMode={switchChatMode} />
        <CreateNewChat switchChatMode={switchChatMode} />
        <Logout />
      </Box>
    </Box>
  );
});

export { Header };
