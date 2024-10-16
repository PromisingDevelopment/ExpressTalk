import { Box, SxProps, useTheme } from "@mui/material";
import React, { HTMLInputTypeAttribute } from "react";

interface CustomInputProps {
  label: string;
  name: string;
  inputId: string;
  inputType?: HTMLInputTypeAttribute;
  onChange?: any;
  sx?: SxProps;
  defaultValue?: any;
  autoFocus?: boolean;
}

const CustomInput = React.forwardRef<HTMLInputElement, CustomInputProps>(
  ({ label, name, sx, onChange, inputId, inputType, defaultValue, autoFocus }, ref) => {
    const theme = useTheme();

    return (
      <Box
        onChange={onChange}
        ref={ref}
        component="input"
        defaultValue={defaultValue}
        placeholder={label}
        id={inputId}
        type={inputType}
        accept={inputType === "file" ? ".jpg,.jpeg,.png,.webp" : undefined}
        name={name}
        autoFocus={autoFocus}
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
