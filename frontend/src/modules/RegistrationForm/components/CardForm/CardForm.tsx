import { Box } from "@mui/material";
import React, { FC } from "react";

interface CardFormProps {
  children: React.ReactNode;
}

const CardForm: FC<CardFormProps> = ({ children }) => {
  return (
    <Box
      sx={{
        borderRadius: 6,
        background: (theme) => theme.palette.primary.dark,
      }}>
      {children}
    </Box>
  );
};

export { CardForm };
