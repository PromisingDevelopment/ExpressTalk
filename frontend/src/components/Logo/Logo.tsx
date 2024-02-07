import { Box, SxProps } from "@mui/material";
import React from "react";

interface LogoProps {
  size: number;
  src?: string; // change to required
  sx?: SxProps;
}

const Logo: React.FC<LogoProps> = ({ src = "", size, sx }) => {
  return (
    <Box
      sx={{
        width: size,
        height: size,
        bgcolor: "#fff", // remove bg
        borderRadius: "50%",
        overflow: "hidden",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        color: "#fff",
        ...sx,
      }}>
      <Box src={src} component="img" alt="logo" />
    </Box>
  );
};

export { Logo };
