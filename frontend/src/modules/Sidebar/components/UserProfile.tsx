import { Box, Modal, Typography } from "@mui/material";
import React from "react";
import { IUser } from "types/IUser";
import ModalContent from "UI/ModalContent";

interface UserProfileProps {
  openProfile: boolean;
  setOpenProfile: Function;
  userData: { user: IUser; avatar: string };
}

const UserProfile: React.FC<UserProfileProps> = ({ openProfile, setOpenProfile, userData }) => {
  const onCloseModal = () => {
    setOpenProfile(false);
  };

  return (
    <>
      <Modal open={openProfile} onClose={onCloseModal}>
        <ModalContent onCloseModal={onCloseModal} title="Profile settings">
          info
        </ModalContent>
      </Modal>
    </>
  );
};

export { UserProfile };
