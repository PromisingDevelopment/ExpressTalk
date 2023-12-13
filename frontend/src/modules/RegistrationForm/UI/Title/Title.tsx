import { Typography } from "@mui/material";
import React, { FC } from "react";

interface TitleProps {
  children: React.ReactNode;
  size: "large" | "normal";
}

const Title: FC<TitleProps> = ({ children, size }) => {
  const fontSize = size === "normal" ? "3rem" : "3.25rem";
  return (
    <Typography
      sx={{
        fontSize: fontSize,
        fontWeight: 700,
        lineHeight: "1.17",
        mb: 10.5,
      }}>
      {children}
    </Typography>
  );
};

export { Title };
