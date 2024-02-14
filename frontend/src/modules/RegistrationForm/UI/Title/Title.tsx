import { SxProps, Typography, useTheme } from "@mui/material";
import React from "react";

interface TitleProps {
  children: React.ReactNode;
  size?: "large" | "normal";
}

const Title: React.FC<TitleProps> = ({ children, size = "normal" }) => {
  const theme = useTheme();
  const fontSize = size === "normal" ? "3rem" : "3.25rem";
  const marginBottoms = {
    normal: { sm: 6, xs: 5 },
    large: { sm: 8.5, xs: 8 },
  };

  return (
    <Typography
      sx={{
        fontSize: fontSize,
        fontWeight: 700,
        lineHeight: "1.17",
        mb: marginBottoms[size],
        [theme.breakpoints.down("sm")]: {
          fontSize: "2.8rem",
        },
        [theme.breakpoints.down(500)]: {
          fontSize: "2.4rem",
        },
        [theme.breakpoints.down(450)]: {
          fontSize: "2.2rem",
        },
      }}>
      {children}
    </Typography>
  );
};

export { Title };
