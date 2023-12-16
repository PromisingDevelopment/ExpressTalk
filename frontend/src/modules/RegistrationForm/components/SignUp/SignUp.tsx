import React from "react";
import { Title } from "../../UI/Title";
import { CustomLink } from "../../UI/CustomLink";
import { GoBack } from "../../UI/GoBack";
import { CustomInput } from "../../UI/CustomInput";
import { InputsWrapper } from "../../UI/InputsWrapper";

interface SignUpProps {}

const SignUp: React.FC<SignUpProps> = () => {
  return (
    <>
      <Title>Sign up</Title>
      <InputsWrapper>
        {["Login", "Name", "Gmail", "Password"].map((label) => (
          <CustomInput label={label} />
        ))}
      </InputsWrapper>
      <CustomLink uppercase to="/auth/gmail-verification">
        done
      </CustomLink>

      <GoBack />
    </>
  );
};

export { SignUp };
