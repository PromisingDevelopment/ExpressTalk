import { SxProps, Typography, useTheme } from "@mui/material";
import React from "react";

interface TitleProps {
  children: React.ReactNode;
  size?: "large" | "normal";
  sx?: SxProps;
}

const Title: React.FC<TitleProps> = ({ children, size = "normal", sx }) => {
  const theme = useTheme();
  const fontSize = size === "normal" ? "3rem" : "3.25rem";

  return (
    <Typography
      sx={{
        fontSize: fontSize,
        fontWeight: 700,
        lineHeight: "1.17",
        [theme.breakpoints.down("sm")]: {
          fontSize: "2.8rem",
        },
        [theme.breakpoints.down(500)]: {
          fontSize: "2.4rem",
        },
        [theme.breakpoints.down(450)]: {
          fontSize: "2.2rem",
        },
        ...sx,
      }}>
      {children}
    </Typography>
  );
};

export { Title };
