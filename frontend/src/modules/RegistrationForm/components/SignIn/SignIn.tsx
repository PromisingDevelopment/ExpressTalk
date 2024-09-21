import React from "react";
import { Title } from "../../UI/Title";
import { CustomLink } from "../../UI/CustomLink";
import { GoBack } from "UI/GoBack";
import { CustomInput } from "../../UI/CustomInput";
import { InputsWrapper } from "../../UI/InputsWrapper";
import { object, string } from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import { type SubmitHandler, useForm } from "react-hook-form";
import type { SignInFields, SignInLabels } from "../../types/SignInFields";
import { resetStatus, signInThunk } from "../../store/authSlice";
import { useAppDispatch, useAppSelector } from "../../../../hooks/redux";
import { useNavigate } from "react-router-dom";
import { AlertError } from "../AlertError";
import { Box } from "@mui/material";

const scheme = object().shape({
  loginOrEmail: string().min(4, "This field length should be more than 4 characters").required(),
  password: string().min(6, "password length should be more than 6 characters").required(),
});

interface SignInProps {}

const SignIn: React.FC<SignInProps> = () => {
  const { register, handleSubmit, formState } = useForm<SignInFields>({
    mode: "onSubmit",
    resolver: yupResolver(scheme),
  });
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { errorMessage, status } = useAppSelector((state) => state.auth.signIn);

  React.useEffect(() => {
    if (status === "fulfilled") {
      navigate("/");
      dispatch(resetStatus("signIn"));
    }
  }, [navigate, status, dispatch]);

  const labels = [
    {
      label: "Login or email",
      field: "loginOrEmail",
    },
    {
      label: "Password",
      field: "password",
    },
  ];

  const onSubmit: SubmitHandler<SignInFields> = (data) => {
    dispatch(signInThunk(data));
  };

  return (
    <Box component="form" sx={{ pb: 7.5 }} onSubmit={handleSubmit(onSubmit)}>
      <Title>Sign in</Title>
      <InputsWrapper>
        {labels.map(({ label, field }) => (
          <CustomInput
            key={label}
            type={label.toLowerCase() === "password" ? "password" : "text"}
            {...register(field as SignInLabels)}
            errorMessage={formState.errors[field as SignInLabels]?.message}
            label={label}
          />
        ))}
      </InputsWrapper>
      <CustomLink isLoading={status === "loading"} submit uppercase>
        done
      </CustomLink>
      <GoBack />
      <AlertError errorMessage={errorMessage} field="signIn" status={status} />
    </Box>
  );
};

export { SignIn };
