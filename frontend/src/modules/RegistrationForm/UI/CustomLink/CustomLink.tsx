import React from "react";
import { Button, SxProps, useTheme } from "@mui/material";
import { useNavigate } from "react-router-dom";

interface CustomLinkProps {
  children: React.ReactNode;
  to?: string;
  background?: "white" | "grey" | "transparent";
  outlined?: boolean;
  sx?: SxProps;
  uppercase?: boolean;
  submit?: boolean;
  isLoading?: boolean;
}

const CustomLink: React.FC<CustomLinkProps> = ({
  children,
  isLoading,
  background = "white",
  outlined,
  sx,
  uppercase,
  submit,
  to,
}) => {
  const theme = useTheme();
  const navigate = useNavigate();

  const backgrounds = { white: "#fff", grey: "#ADB4CD", transparent: "transparent" };
  const border = outlined ? "1px solid #A3AAC4" : "none";
  const color = outlined ? "#A3AAC4" : "#2B3464";
  const textTransform = uppercase ? "uppercase" : "none";
  const type = submit ? "submit" : "button";

  const onClick = () => {
    if (!to) return;

    navigate(to);
  };

  return (
    <Button
      type={type}
      onClick={onClick}
      disabled={isLoading}
      sx={{
        background: backgrounds[background] + " !important",
        border: border,
        color: color,
        textTransform: textTransform,
        borderRadius: 3,
        position: "relative",
        transition: "all 0.3s ease 0s",
        fontSize: "1.75rem",
        lineHeight: 1,
        p: ({ spacing }) => spacing(2.5, 6.875),
        ":hover": {
          opacity: 0.7,
        },
        [theme.breakpoints.down("sm")]: {
          p: ({ spacing }) => spacing(1.5, 5),
          fontSize: "1.4rem",
        },
        ...sx,
      }}>
      {children}
    </Button>
  );
};

export { CustomLink };
