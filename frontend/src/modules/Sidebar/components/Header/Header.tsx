import React from "react";
import { Box, IconButton, Typography } from "@mui/material";
import SettingsIcon from "@mui/icons-material/SettingsOutlined";
import { CustomIconButton } from "../CustomIconButton";
import { Logo } from "../../../../components/Logo";

interface HeaderProps {}

const Header: React.FC<HeaderProps> = () => {
  return (
    <Box
      sx={{
        display: "flex",
        alignItems: "center",
        gap: 2,
        flexBasis: 96,
        flexShrink: 0,
        px: 3.5,
      }}>
      <Logo size={64} />
      <Typography fontSize={24} flexGrow={1}>
        Login
      </Typography>
      <Box>
        <CustomIconButton fontSize={44}>
          <SettingsIcon />
        </CustomIconButton>
      </Box>
    </Box>
  );
};

export { Header };
