import React from "react";
import { Alert, Snackbar } from "@mui/material";
import { useAppDispatch } from "../../../../hooks/redux";
import { resetStatus } from "../../store/authSlice";

interface AlertErrorProps {
  errorMessage: string | null;
  status: "idle" | "loading" | "error" | "fulfilled";
  field: "signUp" | "signIn" | "emailVerification";
}

const AlertError: React.FC<AlertErrorProps> = ({ errorMessage, field, status }) => {
  const dispatch = useAppDispatch();

  const onClose = () => {
    dispatch(resetStatus(field));
  };

  return (
    <Snackbar
      open={status === "error"}
      anchorOrigin={{ horizontal: "center", vertical: "bottom" }}
      autoHideDuration={4000}
      onClose={onClose}>
      <Alert severity="error">
        {errorMessage ? errorMessage : "Something went wrong :("}
      </Alert>
    </Snackbar>
  );
};

export { AlertError };
