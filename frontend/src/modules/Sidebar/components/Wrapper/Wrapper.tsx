import { Box } from "@mui/material";
import React from "react";

interface WrapperProps {
  children: React.ReactNode;
}

const Wrapper: React.FC<WrapperProps> = ({ children }) => {
  return (
    <Box
      sx={{
        flexBasis: 464,
        flexShrink: 0,
        height: "100vh",
        bgcolor: "#1F274E",
        display: "flex",
        flexDirection: "column",
      }}>
      {children}
    </Box>
  );
};

export { Wrapper };
