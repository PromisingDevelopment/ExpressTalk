import React from "react";
import { Box, Typography, useTheme } from "@mui/material";
import { Logo } from "../../../../components/Logo";
import { CreateNewChat } from "../CreateNewChat";
import { useAppDispatch, useAppSelector } from "hooks/redux";
import { getCurrentUser } from "redux/rootSlice";
import { Logout } from "../Logout";

interface HeaderProps {}

const Header = React.forwardRef<HTMLDivElement, HeaderProps>((props, ref) => {
  const theme = useTheme();
  const dispatch = useAppDispatch();
  const { status, user } = useAppSelector((state) => state.root.currentUser);
  const [login, setLogin] = React.useState(user?.login || "");

  React.useEffect(() => {
    const setCorrectedLogin = () => {
      const width = document.body.clientWidth;

      const userLogin = user?.login;
      if (!userLogin) return;

      if (width > 900 && userLogin.length > 28) {
        setLogin(login.slice(0, 28) + "...");
      } else if (width > 600 && userLogin.length > 26) {
        setLogin(login.slice(0, 26) + "...");
      } else if (width > 0 && userLogin.length > 20) {
        setLogin(login.slice(0, 20) + "...");
      }
      setLogin(userLogin);
    };

    window.addEventListener("resize", setCorrectedLogin);
    setCorrectedLogin();
  }, [user]);

  React.useEffect(() => {
    dispatch(getCurrentUser());
  }, []);

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
      <Logo isMain size={64} />
      <Typography
        sx={{
          fontSize: { lg: 24, md: 18, sm: 16, xs: 14 },
          flexGrow: 1,
          lineHeight: 1.3,
          textTransform: "capitalize",
          wordBreak: "break-all",
        }}>
        {status === "error" && "Guest"}
        {status === "loading" && "Loading..."}
        {user && login}
      </Typography>
      <Box display="flex" gap={{ xs: 0.5, lg: 1 }}>
        <CreateNewChat />
        <Logout />
      </Box>
    </Box>
  );
});

export { Header };
