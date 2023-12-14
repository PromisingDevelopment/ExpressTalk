import React from "react";
import { Button, Link, SxProps } from "@mui/material";
import { Link as RouterLink } from "react-router-dom";

interface CustomLinkProps {
  children: React.ReactNode;
  to: string;
  background?: "white" | "grey" | "transparent";
  outlined?: boolean;
  sx?: SxProps;
}

const CustomLink: React.FC<CustomLinkProps> = ({
  children,
  to,
  background = "white",
  outlined,
  sx,
}) => {
  const backgrounds = { white: "#fff", grey: "#ADB4CD", transparent: "transparent" };
  const bg = backgrounds[background];
  const border = outlined ? "1px solid #A3AAC4" : "none";
  const color = outlined ? "#A3AAC4" : "#2B3464";

  return (
    <Button
      sx={{
        background: bg + " !important",
        border: border,
        color: color,
        borderRadius: 3,
        position: "relative",
        transition: "all 0.3s ease 0s",
        fontSize: "1.75rem",
        lineHeight: 1,
        textTransform: "none",
        p: ({ spacing }) => spacing(2.5, 6.875),
        ":hover": {
          opacity: 0.7,
        },
        ...sx,
      }}>
      <Link
        to={to}
        component={RouterLink}
        sx={{
          position: "absolute",
          top: 0,
          left: 0,
          height: "100%",
          width: "100%",
        }}></Link>
      {children}
    </Button>
  );
};

export { CustomLink };
