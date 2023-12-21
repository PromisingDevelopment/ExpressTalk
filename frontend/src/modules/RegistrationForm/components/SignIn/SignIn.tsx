import React from "react";
import { Title } from "../../UI/Title";
import { CustomLink } from "../../UI/CustomLink";
import { GoBack } from "../../UI/GoBack";
import { CustomInput } from "../../UI/CustomInput";
import { InputsWrapper } from "../../UI/InputsWrapper";
import { object, string } from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import { type SubmitHandler, useForm } from "react-hook-form";
import type { SignInFields, SignInLabels } from "../../types/SignInFields";

const scheme = object().shape({
  loginOrGmail: string()
    .min(4, "This field length should be more than 4 characters")
    .required(),
  password: string()
    .min(6, "password length should be more than 6 characters")
    .required(),
});

interface SignInProps {}

const SignIn: React.FC<SignInProps> = () => {
  const { register, handleSubmit, formState } = useForm<SignInFields>({
    mode: "onSubmit",
    resolver: yupResolver(scheme),
  });

  const labels = [
    {
      label: "Login or gmail",
      field: "loginOrGmail",
    },
    {
      label: "Password",
      field: "password",
    },
  ];

  const onSubmit: SubmitHandler<SignInFields> = (data) => {
    console.log(data);

    /* pass data to redux and redirect user to home page */
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <Title>Sign in</Title>
      <InputsWrapper>
        {labels.map(({ label, field }) => (
          <CustomInput
            key={label}
            {...register(field as SignInLabels)}
            errorMessage={formState.errors[field as SignInLabels]?.message}
            label={label}
          />
        ))}
      </InputsWrapper>
      <CustomLink submit uppercase>
        done
      </CustomLink>

      <GoBack />
    </form>
  );
};

export { SignIn };
