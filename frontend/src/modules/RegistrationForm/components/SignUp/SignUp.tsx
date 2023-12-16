import React from "react";
import { Title } from "../../UI/Title";
import { CustomLink } from "../../UI/CustomLink";
import { GoBack } from "../../UI/GoBack";
import { CustomInput } from "../../UI/CustomInput";

interface SignUpProps {}

const SignUp: React.FC<SignUpProps> = () => {
  return (
    <>
      <Title>Sign up</Title>
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          gap: { sx: 6, xs: 3 },
          mb: { sx: 6, xs: 5 },
        }}>
        {["Login", "Name", "Phone", "Password"].map((name) => (
          <TextField
            label={name}
            sx={{
              "& fieldset": { borderColor: "#E9EFFF !important", borderRadius: 3 },
              "& label": { color: "#E9EFFF80 " },
              "& label.Mui-focused": { color: "#fff" },
            }}
          />
        ))}
      </Box>
      <CustomLink uppercase to="verif">
      <CustomLink uppercase to="/auth/gmail-verification">
        done
      </CustomLink>

      <GoBack />
    </>
  );
};

export { SignUp };
