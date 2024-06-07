import { Button, TextField } from "@mui/material";
import axios from "axios";
import { authUrls } from "config";
import { useAppDispatch } from "hooks/redux";
import { resetStatus } from "modules/RegistrationForm/store/authSlice";
import React from "react";
import { useNavigate } from "react-router-dom";

export function SignUpButtons() {
  const inputRef = React.useRef<HTMLInputElement>(null);
  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  const users = [
    { email: "mafioznik445@gmail.com" },
    { email: "litart445@gmail.com" },
    { email: "artem.litvin445@gmail.com" },
  ];

  const onClickUser = (num: number, email: string) => {
    axios.post(
      authUrls.sign_up,
      {
        name: "name" + num,
        login: "login" + num,
        email: email,
        password: "12345678",
      },
      { withCredentials: true }
    );
  };

  const onConfirmCode = (email: string) => {
    const input = inputRef.current;

    if (input && input.value) {
      const data = {
        email: email,
        code: input.value,
      };

      axios.post(authUrls.email, data, { withCredentials: true }).then(() => {
        dispatch(resetStatus("emailVerification"));
        navigate("/");
      });
    }
  };

  const signInUser = (login: string) => {
    axios
      .post(
        authUrls.sign_in,
        {
          login: login,
          password: "12345678",
        },
        { withCredentials: true }
      )
      .then(() => navigate("/"));
  };

  return (
    <div>
      {users.map((user, i) => (
        <Button
          key={i}
          sx={{ color: "#fff" }}
          onClick={() => onClickUser(i + 1, user.email)}>
          reg {i + 1}
        </Button>
      ))}

      <br />

      {users.map((user, i) => (
        <Button key={i} sx={{ color: "#fff" }} onClick={() => onConfirmCode(user.email)}>
          email {i + 1}
        </Button>
      ))}

      <br />

      {users.map((user, i) => (
        <Button key={i} sx={{ color: "#fff" }} onClick={() => signInUser(user.email)}>
          login {i + 1}
        </Button>
      ))}

      <br />

      <TextField sx={{ border: 1 }} inputRef={inputRef} type="text" />
    </div>
  );
}
