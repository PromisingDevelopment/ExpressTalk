import { Box, Button, Modal, styled, Typography } from "@mui/material";
import { CustomInput } from "components/CustomInput";
import React from "react";
import CustomIconButton from "UI/CustomIconButton";
import { SubmitButton } from "UI/SubmitButton";
import ModalContent from "../UI/ModalContent";

interface ModalLayoutProps {
  onSubmit: any;
  label: string;
  buttonLabel?: string;
  inputLabel?: string;
  inputName?: string;
  Icon?: any;
  closeMenu?: any;
  stateWithIndex?: {
    value: boolean;
    setValue: any;
  };
}

const ModalLayout: React.FC<ModalLayoutProps> = ({
  onSubmit,
  label,
  buttonLabel,
  inputLabel,
  inputName,
  Icon,
  closeMenu,
  stateWithIndex,
}) => {
  const [openModal, setOpenModal] = React.useState(false);
  const [isEmpty, setIsEmpty] = React.useState(false);
  const [errorMessage, setErrorMessage] = React.useState<any>(null);
  const userIdInputRef = React.useRef<HTMLInputElement | null>(null);
  const [isDisabled, setIsDisabled] = React.useState(false);

  const handleMenuItemClick = (e: React.MouseEvent) => {
    (e.currentTarget as HTMLElement).blur();
  };

  const onOpenModal = () => {
    setOpenModal(true);
  };
  const onCloseModal = () => {
    setOpenModal(false);

    if (stateWithIndex) stateWithIndex.setValue(null);
    if (closeMenu) closeMenu();
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setIsEmpty(false);
    setErrorMessage(null);
    setIsDisabled(true);

    const userIdInput = userIdInputRef.current;
    const input = userIdInput?.value;

    if (!input) {
      setIsDisabled(false);
      return setIsEmpty(true);
    }

    try {
      await onSubmit(input);
      onCloseModal();
    } catch (error) {
      setErrorMessage(error);
    }
    setIsDisabled(false);
  };

  const onConfirm = () => {
    onSubmit();
    onCloseModal();
  };

  const inputLayout = (
    <form onSubmit={handleSubmit}>
      <CustomInput
        autoFocus
        inputId="action-modal-input"
        ref={userIdInputRef}
        label={inputLabel || ""}
        name={inputName || ""}
      />
      {isEmpty && (
        <Typography variant="body1" color="error.main" mt={0.5}>
          This field is required!
        </Typography>
      )}
      {errorMessage && (
        <Typography variant="body1" color="error.main" mt={0.5}>
          {errorMessage.toString()}
        </Typography>
      )}

      <SubmitButton disabled={isDisabled} label={"submit"} />
    </form>
  );

  const confirmLayout = (
    <Box sx={{ display: "flex", gap: 2 }}>
      <StyledButton variant="contained" onClick={onConfirm}>
        confirm
      </StyledButton>
      <StyledButton onClick={onCloseModal} isCancel variant="contained">
        cancel
      </StyledButton>
    </Box>
  );

  const openButton = (
    <Button sx={{ color: "#fff" }} onMouseDown={handleMenuItemClick} onClick={onOpenModal}>
      {buttonLabel}
    </Button>
  );

  const openButtonWithIcon = <CustomIconButton label={label} onClick={onOpenModal} Icon={Icon} />;

  return (
    <>
      {!stateWithIndex && (Icon ? openButtonWithIcon : openButton)}

      <Modal open={stateWithIndex ? stateWithIndex.value : openModal} onClose={onCloseModal}>
        <ModalContent title={label} onCloseModal={onCloseModal}>
          {inputLabel && inputName ? inputLayout : confirmLayout}
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
  }
`;

export { ModalLayout };
