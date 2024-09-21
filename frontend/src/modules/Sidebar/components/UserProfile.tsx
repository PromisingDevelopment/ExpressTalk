import { Box, Button, Modal, Typography } from "@mui/material";
import { CustomInput } from "components/CustomInput";
import { Logo } from "components/Logo";
import React from "react";
import { IUser } from "types/IUser";
import { GoBack } from "UI/GoBack";
import ModalContent from "UI/ModalContent";
import { SubmitButton } from "UI/SubmitButton";

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
        {["name", "login", "avatar"].map((inputName) => (
          <Box sx={{ ":not(last-child)": { marginTop: 3 } }}>
            <Box htmlFor={`edit-user-${inputName}-input`} component="label" sx={{ fontSize: 20 }}>
              Edit {inputName}:
            </Box>
            <CustomInput
              inputId={`edit-user-${inputName}-input`}
              inputType={inputName === "avatar" ? "file" : "text"}
              label={`Input ${inputName}`}
              name={`edit-user-${inputName}-input`}
              sx={
                inputName === "avatar"
                  ? { border: 0, padding: 0, marginTop: 1 }
                  : { borderWidth: 3, marginTop: 1, ":focus": { borderColor: "#fff" } }
              }
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
