import { Box, SxProps } from "@mui/material";
import React from "react";
import avatarImage from "assets/images/avatar.png";

interface LogoProps {
  size: number;
  src?: string; // change to required
  isMain?: boolean;
}

const Logo: React.FC<LogoProps> = ({ src = "", size, isMain }) => {
  const mainLogoSize = {
    lg: size,
    md: 45,
    xs: 40,
  };
  const defaultLogoSize = {
    lg: size,
    sm: 40,
    xs: 35,
  };

  return (
    <Box
      sx={[
        {
          borderRadius: "50%",
          overflow: "hidden",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          color: "#fff",
          flex: "0 0 auto",
        },
        isMain
          ? {
              width: mainLogoSize,
              height: mainLogoSize,
            }
          : {
              width: defaultLogoSize,
              height: defaultLogoSize,
            },
      ]}>
      <Box
        sx={{ width: 1, height: 1 }}
        src={src ? src : avatarImage}
        component="img"
        alt="logo"
      />
    </Box>
  );
};

export { Logo };
