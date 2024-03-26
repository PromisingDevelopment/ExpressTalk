import { Box, SxProps, useTheme } from "@mui/material";
import React from "react";

interface CustomInputProps {
  label: string;
  name: string;
  sx?: SxProps;
}

const CustomInput: React.FC<CustomInputProps> = React.forwardRef(
  ({ label, name, sx }, ref) => {
    const theme = useTheme();

    return (
      <Box
        ref={ref}
        component="input"
        placeholder={label}
        name={name}
        sx={{
          width: 1,
          height: 48,
          border: "1px solid #6A73A6",
          borderRadius: 3,
          py: 1,
          px: 3,
          color: "#fff",
          background: "none",
          fontSize: 20,
          ":focus": {
            borderColor: "#7b83b0",
          },
          "::placeholder": {
            color: "#6A73A6",
          },
          [theme.breakpoints.down("sm")]: {
            fontSize: 16,
            px: 2,
          },
          ...sx,
        }}
      />
    );
  }
);

export { CustomInput };
