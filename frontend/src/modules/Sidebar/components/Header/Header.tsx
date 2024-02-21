import React from "react";
import { Box, Typography, useTheme } from "@mui/material";
import SettingsIcon from "@mui/icons-material/SettingsOutlined";
import { CustomIconButton } from "../CustomIconButton";
import { Logo } from "../../../../components/Logo";

interface HeaderProps {}

const Header: React.FC<HeaderProps> = () => {
  const theme = useTheme();

  return (
    <Box
      sx={{
        display: "flex",
        alignItems: "center",
        gap: 2,
        flexBasis: 96,
        flexShrink: 0,
        px: { lg: 3.125, xs: 2 },
        bgcolor: "#1F274E",
        [theme.breakpoints.down(767)]: {
          flexBasis: 70,
          px: 2,
        },
      }}>
      <Logo isMain size={64} />
      <Typography
        sx={{
          fontSize: { lg: 24, xs: 20 },
          flexGrow: 1,
        }}>
        Login
      </Typography>
    </Box>
  );
};

export { Header };
