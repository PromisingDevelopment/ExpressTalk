import { Box, useTheme } from "@mui/material";
import React from "react";

interface CardFormProps {
  children: React.ReactNode;
}

const CardForm: React.FC<CardFormProps> = ({ children }) => {
  const theme = useTheme();

  return (
    <Box
      sx={{
        height: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        px: 2,
        "@media (max-height: 650px)": {
          alignItems: "flex-start",
          py: 3,
          height: 1,
        },
      }}>
      <Box
        sx={{
          borderRadius: 6,
          background: (theme) => theme.palette.primary.dark,
          maxWidth: 588,
          width: 1,
          p: 8,
          textAlign: "center",
          [theme.breakpoints.down("sm")]: {
            maxWidth: 1,
            py: 8,
            px: 4,
          },
        }}>
        {children}
      </Box>
    </Box>
  );
};

export { CardForm };
