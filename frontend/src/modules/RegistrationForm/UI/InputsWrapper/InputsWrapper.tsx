import { Box } from "@mui/material";
import React from "react";

interface InputsWrapperProps {
  children: React.ReactNode[];
}

const InputsWrapper: React.FC<InputsWrapperProps> = ({ children }) => {
  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        gap: { sx: 6, xs: 3 },
        mb: { sx: 6, xs: 5 },
      }}>
      {children}
    </Box>
  );
};

export { InputsWrapper };
