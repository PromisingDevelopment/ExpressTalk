import React from "react";
import { Title } from "../../UI/Title";
import { CustomLink } from "../../UI/CustomLink";
import { GoBack } from "../../UI/GoBack";
import { CustomInput } from "../../UI/CustomInput";
import { InputsWrapper } from "../../UI/InputsWrapper";

interface SignInProps {}

const SignIn: React.FC<SignInProps> = () => {
  return (
    <>
      <Title>Sign in</Title>
      <InputsWrapper>
        {["Login or gmail", "Password"].map((label) => (
          <CustomInput label={label} />
        ))}
      </InputsWrapper>
      <CustomLink uppercase to="/">
        done
      </CustomLink>

      <GoBack />
    </>
  );
};

export { SignIn };
