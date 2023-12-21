import React from "react";
import { Title } from "../../UI/Title";
import { CustomInput } from "../../UI/CustomInput";
import { CustomLink } from "../../UI/CustomLink";
import { GoBack } from "../../UI/GoBack";
import { object, number } from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import { type SubmitHandler, useForm } from "react-hook-form";
import type { GmailFields } from "../../types/GmailFields";

const scheme = object().shape({
  code: number().required(),
});

interface GmailVerificationProps {}

const GmailVerification: React.FC<GmailVerificationProps> = () => {
  const { register, handleSubmit, formState } = useForm<GmailFields>({
    mode: "onSubmit",
    resolver: yupResolver(scheme),
  });

  console.log(formState.errors.code);

  const onSubmit: SubmitHandler<GmailFields> = (data) => {
    console.log(data);

    /* pass data to redux and redirect user to home page */
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <Title>Gmail verification</Title>
      <CustomInput
        {...register("code")}
        errorMessage={formState.errors.code?.message}
        label="Code"
      />
      <CustomLink sx={{ mt: 6 }} uppercase submit>
        done
      </CustomLink>
      <GoBack />
    </form>
  );
};

export { GmailVerification };
