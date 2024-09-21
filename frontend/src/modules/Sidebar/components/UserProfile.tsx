import { Box, Button, Modal, Typography } from "@mui/material";
import { CustomInput } from "components/CustomInput";
import { Logo } from "components/Logo";
import React, { HTMLInputTypeAttribute, StyleHTMLAttributes } from "react";
import { IUser } from "types/IUser";
import { GoBack } from "UI/GoBack";
import ModalContent from "UI/ModalContent";
import { SubmitButton } from "UI/SubmitButton";

interface InputFieldData {
  name: "name" | "login" | "avatar";
  type: HTMLInputTypeAttribute;
  id: string;
  label: string;
  style: any;
}

const inputFieldsData: InputFieldData[] = [
  {
    name: "name",
    type: "text",
    id: "edit-user-name-input",
    label: "Input name",
    style: { borderWidth: 3, marginTop: 1, ":focus": { borderColor: "#fff" } },
  },
  {
    name: "login",
    type: "text",
    id: "edit-user-login-input",
    label: "Input login",
    style: { borderWidth: 3, marginTop: 1, ":focus": { borderColor: "#fff" } },
  },
  {
    name: "avatar",
    type: "file",
    id: "edit-user-avatar-input",
    label: "",
    style: { border: 0, padding: 0, marginTop: 1 },
  },
];

interface UserProfileProps {
  openProfile: boolean;
  setOpenProfile: Function;
  userData: { user: IUser; avatar: string };
}

const UserProfile: React.FC<UserProfileProps> = ({ openProfile, setOpenProfile, userData }) => {
  const [userEdit, setUserEdit] = React.useState(false);

  const onCloseModal = () => {
    setOpenProfile(false);
  };

  const onEdit = () => {
    setUserEdit(true);
  };

  const onGoBack = () => {
    setUserEdit(false);
  };

  const infoLayout = (
    <>
      <Box sx={{ display: "flex", gap: 4, alignItems: "center" }}>
        <Box component="img" src={userData.avatar} sx={{ width: 60, height: 60, borderRadius: "50%" }} />
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            gap: 0.5,
            h4: { fontSize: 32 },
            p: { fontSize: 20 },
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
      <form>
        {inputFieldsData.map((inputData) => (
          <Box sx={{ ":not(last-child)": { marginTop: 3 } }}>
            <Box htmlFor={inputData.id} component="label" sx={{ fontSize: 20 }}>
              Edit {inputData.name}:
            </Box>
            <CustomInput
              inputId={inputData.id}
              inputType={inputData.type}
              label={inputData.label}
              name={inputData.id}
              defaultValue={inputData.name !== "avatar" && userData.user[inputData.name]}
              sx={inputData.style}
            />
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
