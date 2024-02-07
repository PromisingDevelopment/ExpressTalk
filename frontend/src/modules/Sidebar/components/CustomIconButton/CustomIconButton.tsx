import { IconButton, SxProps } from "@mui/material";
import React from "react";

interface CustomIconButtonProps {
  children: React.ReactNode;
  sx?: SxProps;
  fontSize?: number;
  borderRight?: boolean;
}

const CustomIconButton: React.FC<CustomIconButtonProps> = ({
  children,
  sx,
  fontSize = 24,
  borderRight = false,
}) => {
  return (
    <IconButton
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

export { CustomIconButton };
