import { Box, SxProps } from "@mui/material";
import React from "react";

interface LogoProps {
  size: number;
  src?: string; // change to required
  isMain?: boolean;
}

const Logo: React.FC<LogoProps> = ({ src = "", size, isMain }) => {
  const mainLogoSize = {
    lg: size,
    sm: 52,
    xs: 45,
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
          bgcolor: "#fff", // remove bg
          borderRadius: "50%",
          overflow: "hidden",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          color: "#fff",
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
      <Box src={src} component="img" alt="logo" />
    </Box>
  );
};

export { Logo };
