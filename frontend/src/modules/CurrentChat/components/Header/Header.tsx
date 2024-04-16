import { Box, IconButton, Typography, useTheme } from "@mui/material";
import React from "react";
import { Logo } from "../../../../components/Logo";
import MenuIcon from "@mui/icons-material/MenuRounded";
import { useAppDispatch } from "hooks/redux";
import { setSidebarOpen } from "modules/Sidebar/store/sidebarSlice";
import { useIsMobile } from "hooks/useIsMobile";

interface HeaderProps {}

const Header: React.FC<HeaderProps> = () => {
  const theme = useTheme();
  const dispatch = useAppDispatch();
  const isMobile = useIsMobile();

  const onClickMenu = () => {
    dispatch(setSidebarOpen(true));
  };

  return (
    <Box
      sx={{
        display: "flex",
        gap: 1.5,
        alignItems: "center",
        height: 96,
        px: 4.875,
        bgcolor: "#1F274E",
        borderLeft: "1px solid #353F75",
        [theme.breakpoints.down(767)]: {
          height: 70,
          px: 1.5,
        },
      }}>
      {isMobile && (
        <IconButton onClick={onClickMenu} sx={{ mr: 1 }}>
          <MenuIcon
            sx={{
              color: "#6972A5",
              fontSize: 40,
            }}
          />
        </IconButton>
      )}
      <Logo isMain size={52} />
      <Typography fontSize={20}>Login</Typography>
    </Box>
  );
};

export { Header };
