import { Button, Modal, styled, Typography } from "@mui/material";
import { CustomInput } from "components/CustomInput";
import { error } from "console";
import React, { useEffect } from "react";
import CustomIconButton from "UI/CustomIconButton";
import { SubmitButton } from "UI/SubmitButton";
import ModalContent from "../UI/ModalContent";

interface ModalLayoutProps {
  onSubmit: any;
  label: string;
  inputLabel: string;
  inputName: string;
  Icon?: any;
  withoutIcon?: boolean;
  closeMenu?: any;
}

const ModalLayout: React.FC<ModalLayoutProps> = ({
  onSubmit,
  label,
  inputLabel,
  inputName,
  Icon,
  withoutIcon,
  closeMenu,
}) => {
  const [openModal, setOpenModal] = React.useState(false);
  const [isEmpty, setIsEmpty] = React.useState(false);
  const [errorMessage, setErrorMessage] = React.useState<any>(null);
  const userIdInputRef = React.useRef<HTMLInputElement | null>(null);
  const [isDisabled, setIsDisabled] = React.useState(false);

  const hideFocus = () => {
    (document.activeElement as HTMLElement).blur();
  };

  const onOpenModal = () => {
    setOpenModal(true);
    setTimeout(() => {
      userIdInputRef.current?.focus();
    }, 0);
  };
  const onCloseModal = () => {
    setOpenModal(false);
    closeMenu();
    setTimeout(hideFocus, 300);
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

  return (
    <>
      {withoutIcon ? (
        <Typography fontSize={16} onClick={onOpenModal}>
          {label}
        </Typography>
      ) : (
        <CustomIconButton label={label} onClick={onOpenModal} Icon={Icon} />
      )}

      <Modal open={openModal} onClose={onCloseModal}>
        <ModalContent title={label} onCloseModal={onCloseModal}>
          <form onSubmit={handleSubmit}>
            <CustomInput ref={userIdInputRef} label={inputLabel} name={inputName} />
            {isEmpty && (
              <Typography variant="body1" color="error.main" mt={0.5}>
                This field is required!
              </Typography>
            )}
            {errorMessage && (
              <Typography variant="body1" color="error.main" mt={0.5}>
                {errorMessage}
              </Typography>
            )}

            <SubmitButton disabled={isDisabled} label={label} />
          </form>
        </ModalContent>
      </Modal>
    </>
  );
};

export { ModalLayout };
