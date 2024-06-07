import { IconButton, SxProps } from "@mui/material";
import React from "react";

interface ModeButtonProps {
  children: React.ReactNode;
  sx?: SxProps;
  onClick?: any;
  fontSize?: number;
  borderRight?: boolean;
}

const ModeButton: React.FC<ModeButtonProps> = ({
  children,
  sx,
  fontSize = 24,
  borderRight = false,
  onClick,
}) => {
  return (
    <IconButton
      onClick={onClick}
      sx={{
        flex: 0.5,
        textAlign: "center",
        borderRight: borderRight ? "1px solid #353F75" : "",
        svg: {
          color: "#6A73A6",
          fontSize: fontSize,
        },
        ...sx,
      }}>
      {children}
    </IconButton>
  );
};

export { ModeButton };
