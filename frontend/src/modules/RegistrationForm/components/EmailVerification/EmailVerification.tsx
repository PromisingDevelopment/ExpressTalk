import React from "react";
import { Title } from "../../UI/Title";
import { CustomInput } from "../../UI/CustomInput";
import { CustomLink } from "../../UI/CustomLink";
import { GoBack } from "../../UI/GoBack";
import { object, string } from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import { type SubmitHandler, useForm } from "react-hook-form";
import type { EmailFields } from "../../types/EmailFields";
import { useAppDispatch, useAppSelector } from "../../../../hooks/redux";
import { emailThunk, resetStatus } from "../../store/authSlice";
import { useNavigate } from "react-router-dom";

const scheme = object().shape({
  code: string().length(6, "The length of the code must be 6 characters").required(),
});

interface EmailVerificationProps {}

const EmailVerification: React.FC<EmailVerificationProps> = () => {
  const { register, handleSubmit, formState } = useForm<EmailFields>({
    mode: "onSubmit",
    resolver: yupResolver(scheme),
  });
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { errorMessage, status } = useAppSelector(
    (state) => state.auth.emailVerification
  );

  React.useEffect(() => {
    if (status === "fulfilled") {
      navigate("/");
      dispatch(resetStatus("emailVerification"));
    }
  }, [navigate, status, dispatch]);

  const onSubmit: SubmitHandler<EmailFields> = (data) => {
    dispatch(emailThunk(data));
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <Title>Email verification</Title>
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

export { EmailVerification };
