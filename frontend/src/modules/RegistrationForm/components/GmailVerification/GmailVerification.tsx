import React from "react";
import { Title } from "../../UI/Title";
import { CustomInput } from "../../UI/CustomInput";
import { CustomLink } from "../../UI/CustomLink";
import { GoBack } from "../../UI/GoBack";

interface GmailVerificationProps {}

const GmailVerification: React.FC<GmailVerificationProps> = () => {
  return (
    <>
      <Title>Gmail verification</Title>
      <CustomInput label="Code" />
      <CustomLink sx={{ mt: 6 }} uppercase to="/">
        done
      </CustomLink>
      <GoBack />
    </>
  );
};

export { GmailVerification };
