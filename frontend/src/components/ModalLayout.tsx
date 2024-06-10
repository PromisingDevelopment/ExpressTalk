import { Button, Modal, styled, Typography } from "@mui/material";
import { CustomInput } from "components/CustomInput";
import React from "react";
import CustomIconButton from "UI/CustomIconButton";
import ModalContent from "../UI/ModalContent";

interface ModalLayoutProps {
  onSubmit: any;
  label: string;
  inputLabel: string;
  inputName: string;
  Icon?: any;
  withoutIcon?: boolean;
}

const ModalLayout: React.FC<ModalLayoutProps> = ({
  onSubmit,
  label,
  inputLabel,
  inputName,
  Icon,
  withoutIcon,
}) => {
  const [openModal, setOpenModal] = React.useState(false);
  const [isEmpty, setIsEmpty] = React.useState(false);
  const [errorMessage, setErrorMessage] = React.useState<any>(null);
  const userIdInputRef = React.useRef<HTMLInputElement | null>(null);
  const [isDisabled, setIsDisabled] = React.useState(false);

  const onOpenModal = () => {
    setOpenModal(true);
  };
  const onCloseModal = () => {
    setOpenModal(false);
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

            <SubmitButton
              variant="contained"
              type="submit"
              disableElevation
              disabled={isDisabled}>
              {label}
            </SubmitButton>
          </form>
        </ModalContent>
      </Modal>
    </>
  );
};

const SubmitButton = styled(Button)(({ theme }) =>
  theme.unstable_sx({
    bgcolor: "#fff",
    color: "#2B3464",
    transition: "opacity 0.3s ease 0s",
    mt: 4,
    py: 2,
    width: 1,
    fontSize: 18,
    ":hover": { opacity: 0.8, bgcolor: "#fff" },
    ":disabled": {
      opacity: 0.6,
      bgcolor: "#fff",
    },
    [theme.breakpoints.down("sm")]: {
      fontSize: 16,
      mt: 3,
      py: 1.5,
    },
  })
);

export { ModalLayout };
