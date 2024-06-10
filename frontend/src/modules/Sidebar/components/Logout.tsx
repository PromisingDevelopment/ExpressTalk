import LogoutIcon from "@mui/icons-material/Logout";
import { Box, Button, Modal, styled } from "@mui/material";
import { useAppDispatch } from "hooks/redux";
import React from "react";
import { useNavigate } from "react-router-dom";
import CustomIconButton from "UI/CustomIconButton";
import ModalContent from "UI/ModalContent";
import { logout } from "../store/sidebarSlice";

interface LogoutProps {}

const Logout: React.FC<LogoutProps> = () => {
  const [openModal, setOpenModal] = React.useState(false);
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const onOpenModal = () => {
    setOpenModal(true);
  };
  const onCloseModal = () => {
    setOpenModal(false);
  };

  const onClickLogout = () => {
    onOpenModal();
  };

  const onLogout = () => {
    dispatch(logout());
    navigate("/auth/home");
  };

  return (
    <>
      <CustomIconButton Icon={LogoutIcon} label="Logout" onClick={onClickLogout} />
      <Modal open={openModal} onClose={onCloseModal}>
        <ModalContent onCloseModal={onCloseModal} title="Are you sure you want to logout">
          <Box sx={{ display: "flex", gap: 2 }}>
            <StyledButton variant="contained" onClick={onLogout}>
              logout
            </StyledButton>
            <StyledButton onClick={onCloseModal} isCancel variant="contained">
              cancel
            </StyledButton>
          </Box>
        </ModalContent>
      </Modal>
    </>
  );
};

const StyledButton = styled(Button, {
  shouldForwardProp: (props) => props !== "isCancel",
})<{ isCancel?: boolean }>`
  box-shadow: none;
  font-size: 24px;
  padding-top: 10px;
  padding-bottom: 10px;
  flex: 1 1 50%;
  background: ${({ isCancel }) => (isCancel ? "#fff" : "#f44336")};
  color: ${({ isCancel }) => (isCancel ? "#2B3464" : "#fff")};
  transition: opacity 0.3s ease 0s;
  &:hover {
    opacity: 0.8;
    background: ${({ isCancel }) => (isCancel ? "#fff" : "#f44336")};
    color: ${({ isCancel }) => (isCancel ? "#2B3464" : "#fff")};
  }
  @media (max-width: 600px) {
    font-size: 16px;
    margin-top: 16px;
    padding-top: 12px;
    padding-top: 12px;
  },
`;

export { Logout };
