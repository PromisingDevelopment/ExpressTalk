import React from "react";
import { TextField } from "@mui/material";

interface CustomInputProps {
  label: string;
}

const CustomInput: React.FC<CustomInputProps> = ({ label }) => {
  return (
    <TextField
      label={label}
      sx={{
        "& fieldset": { borderColor: "#E9EFFF !important", borderRadius: 3 },
        "& label": { color: "#E9EFFF80 " },
        "& label.Mui-focused": { color: "#fff" },
        width: 1,
      }}
    />
  );
};

export { CustomInput };
