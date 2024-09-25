import { Box, Button, Modal, Typography } from "@mui/material";
import { CustomInput } from "components/CustomInput";
import { Logo } from "components/Logo";
import { useAppDispatch } from "hooks/redux";
import React, { HTMLInputTypeAttribute } from "react";
import { editUserAvatar, editCurrentUser } from "redux/rootSlice";
import { IUser } from "types/IUser";
import { GoBack } from "UI/GoBack";
import ModalContent from "UI/ModalContent";
import { SubmitButton } from "UI/SubmitButton";

interface InputFieldData {
  name: "name" | "login";
  type: HTMLInputTypeAttribute;
  id: string;
  label: string;
}

const inputFieldsData: InputFieldData[] = [
  {
    name: "name",
    type: "text",
    id: "edit-user-name-input",
    label: "Input name",
  },
  {
    name: "login",
    type: "text",
    id: "edit-user-login-input",
    label: "Input login",
  },
];

interface UserProfileProps {
  openProfile: boolean;
  setOpenProfile: Function;
  userData: { user: IUser; avatar: string };
}

const UserProfile: React.FC<UserProfileProps> = ({ openProfile, setOpenProfile, userData }) => {
  const [userEdit, setUserEdit] = React.useState(false);
  const [inputErrors, setInputErrors] = React.useState<{ [key: string]: string | null }>({
    name: null,
    login: null,
  });

  const dispatch = useAppDispatch();

  const onCloseModal = () => {
    setOpenProfile(false);
  };

  const onEdit = () => {
    setUserEdit(true);
  };

  const onGoBack = () => {
    setUserEdit(false);
  };

  const editUserDataHandler = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const form = e.currentTarget;

    const name = (form.elements[0] as HTMLInputElement).value;
    const login = (form.elements[1] as HTMLInputElement).value;

    //console.log(form.elements[2])
    //const name = formData.get(inputFieldsData[0].id) as string;
    //const login = formData.get(inputFieldsData[1].id) as string;

    //console.log(name);
    //console.log(login);

    if (!name) {
      return setInputErrors({ ...inputErrors, name: "This field is required" });
    }

    if (!login) {
      return setInputErrors({ ...inputErrors, login: "This field is required" });
    }

    dispatch(editCurrentUser({ name, login }));
  };

  const infoLayout = (
    <>
      <Box sx={{ display: "flex", gap: 4, alignItems: "center" }}>
        {/*<Box component="img" src={userData.avatar} sx={{ width: 60, height: 60, borderRadius: "50%" }} />*/}
        <Logo size={0} ownSize={{ lg: 60, xs: 50 }} src={userData.avatar} isAbleToChange />
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            gap: 0.5,
            h4: { fontSize: { lg: 32, md: 28, xs: 24 } },
            p: { fontSize: { lg: 20, md: 18, xs: 16 } },
          }}>
          <h4>{userData.user.name}</h4>
          <p>@{userData.user.login}</p>
        </Box>
      </Box>
      <Button
        onClick={onEdit}
        sx={{
          fontSize: 24,
          color: "#fff",
          border: 3,
          paddingX: 3,
          marginTop: 4,
          ":hover": { background: "rgba(255, 255, 255, .1)" },
        }}>
        Edit your data
      </Button>
    </>
  );

  const editLayout = (
    <>
      <form onSubmit={editUserDataHandler}>
        {inputFieldsData.map((inputData) => (
          <Box key={inputData.id} sx={{ ":not(last-child)": { marginTop: 3 } }}>
            <Box htmlFor={inputData.id} component="label" sx={{ fontSize: 20 }}>
              Edit {inputData.name}:
            </Box>
            <CustomInput
              inputId={inputData.id}
              inputType={inputData.type}
              label={inputData.label}
              name={inputData.id}
              defaultValue={userData.user[inputData.name]}
              sx={{ borderWidth: 3, marginTop: 1, ":focus": { borderColor: "#fff" } }}
            />
            {inputErrors[inputData.name] && (
              <Typography sx={{ color: (theme) => theme.palette.error.main, mt: 1 }}>
                {inputErrors[inputData.name]}
              </Typography>
            )}
          </Box>
        ))}
        <SubmitButton disabled={false} label="Edit user data" />
      </form>
      <GoBack onClick={onGoBack} sx={{ top: 30, position: "relative" }} />
    </>
  );

  return (
    <>
      <Modal open={openProfile} onClose={onCloseModal}>
        <ModalContent onCloseModal={onCloseModal} title="Profile settings">
          {userEdit ? editLayout : infoLayout}
        </ModalContent>
      </Modal>
    </>
  );
};

export { UserProfile };
