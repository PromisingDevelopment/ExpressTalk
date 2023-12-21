import React from "react";
import { TextField, useTheme } from "@mui/material";

interface CustomInputProps {
  label: string;
  errorMessage?: string;
}

const CustomInput: React.FC<CustomInputProps> = React.forwardRef(
  ({ label, errorMessage, ...props }, ref) => {
    const theme = useTheme();

    const borderColor = errorMessage ? theme.palette.error.main : "#E9EFFF";
    const labelColor = errorMessage ? theme.palette.error.main : "#E9EFFF80";

    return (
      <TextField
        label={label}
        {...props}
        inputRef={ref}
        helperText={errorMessage}
        error={!!errorMessage}
        sx={{
          "& fieldset": { borderColor: borderColor + " !important", borderRadius: 3 },
          "& label": { color: labelColor + " !important" },
          "& label.Mui-focused": { color: labelColor + " !important" },
          width: 1,
          textTransform: "capitalize",
        }}
      />
    );
  }
);

export { CustomInput };
