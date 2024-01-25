import React from "react";
import { Title } from "../../UI/Title";
import { GoBack } from "../../UI/GoBack";
import { CustomLink } from "../../UI/CustomLink";
import { CustomInput } from "../../UI/CustomInput";
import { InputsWrapper } from "../../UI/InputsWrapper";
import { object, string } from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import { useNavigate } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../../../hooks/redux";
import { resetStatus, signUpThunk } from "../../store/authSlice";
import { navigateUrls } from "../../../../config";
import { AlertError } from "../AlertError";
import { type SubmitHandler, useForm } from "react-hook-form";
import type { SignUpFields, SignUpLabels } from "../../types/SignUpFields";

const scheme = object().shape({
  login: string().min(4, "login length should be more than 4 characters").required(),
  name: string().required(),
  email: string().email().required(),
  password: string()
    .min(6, "password length should be more than 6 characters")
    .required(),
});

interface SignUpProps {}

const SignUp: React.FC<SignUpProps> = () => {
  const { register, handleSubmit, formState } = useForm<SignUpFields>({
    mode: "onSubmit",
    resolver: yupResolver(scheme),
  });
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { errorMessage, status } = useAppSelector((state) => state.auth.signUp);

  React.useEffect(() => {
    if (status === "fulfilled") {
      navigate(navigateUrls.email);
      dispatch(resetStatus("signUp"));
    }
  }, [navigate, status, dispatch]);

  const onSubmit: SubmitHandler<SignUpFields> = async (data) => {
    await dispatch(signUpThunk(data));
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <Title>Sign up</Title>
      <InputsWrapper>
        {["login", "name", "email", "password"].map((label) => (
          <CustomInput
            type={label === "password" ? "password" : "text"}
            key={label}
            label={label}
            errorMessage={formState.errors[label as SignUpLabels]?.message}
            {...register(label as SignUpLabels)}
          />
        ))}
      </InputsWrapper>
      <CustomLink isLoading={status === "loading"} submit uppercase>
        done
      </CustomLink>
      <GoBack />
      <AlertError errorMessage={errorMessage} field="signUp" status={status} />
    </form>
  );
};

export { SignUp };
