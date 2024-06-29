import { Button, styled } from "@mui/material";
import React from "react";

interface SubmitButtonProps {
  label: string;
  disabled: boolean;
}

export const SubmitButton: React.FC<SubmitButtonProps> = ({ disabled, label }) => {
  return (
    <StyledButton variant="contained" type="submit" disableElevation disabled={disabled}>
      {label}
    </StyledButton>
  );
};

const StyledButton = styled(Button)(({ theme }) =>
  theme.unstable_sx({
    bgcolor: "#fff",
    color: "#2B3464",
    transition: "opacity 0.3s ease 0s",
    mt: 4,
    py: 2,
    width: 1,
    fontSize: 18,
    ":hover": { opacity: 0.8, bgcolor: "#fff" },
    ":disabled": {
      opacity: 0.6,
      bgcolor: "#fff",
    },
    [theme.breakpoints.down("sm")]: {
      fontSize: 16,
      mt: 3,
      py: 1.5,
    },
  })
);
