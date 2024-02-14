import { Box, Typography } from "@mui/material";
import React from "react";
import { Logo } from "../../../../components/Logo";

interface HeaderProps {}

const Header: React.FC<HeaderProps> = () => {
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
      }}>
      <Logo size={52} />
      <Typography fontSize={20}>Login</Typography>
    </Box>
  );
};

export { Header };
