import React from "react";
import { Title } from "../../UI/Title";
import { GoBack } from "../../UI/GoBack";
import { CustomLink } from "../../UI/CustomLink";
import { CustomInput } from "../../UI/CustomInput";
import { InputsWrapper } from "../../UI/InputsWrapper";
import { object, string } from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import { type SubmitHandler, useForm } from "react-hook-form";
import type { SignUpFields, SignUpLabels } from "../../types/SignUpFields";

const scheme = object().shape({
  login: string().min(4, "login length should be more than 4 characters").required(),
  name: string().required(),
  gmail: string().email().required(),
  password: string()
    .min(4, "password length should be more than 6 characters")
    .required(),
});

interface SignUpProps {}

const SignUp: React.FC<SignUpProps> = () => {
  const { register, handleSubmit, formState } = useForm<SignUpFields>({
    mode: "onSubmit",
    resolver: yupResolver(scheme),
  });

  //React.useEffect(() => {
  //  console.log(formState.errors);
  //}, [formState]);

  const onSubmit: SubmitHandler<SignUpFields> = (data) => {
    console.log(data);

    /* pass data to redux and redirect user to home page */
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <Title>Sign up</Title>
      <InputsWrapper>
        {["login", "name", "gmail", "password"].map((label) => (
          <CustomInput
            label={label}
            errorMessage={formState.errors[label as SignUpLabels]?.message}
            {...register(label as SignUpLabels)}
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

export { SignUp };
