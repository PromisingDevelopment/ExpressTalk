import React from "react";
import {
  Box,
  Button,
  IconButton,
  Modal,
  Tooltip,
  Typography,
  styled,
  useTheme,
} from "@mui/material";
import { CustomInput } from "components/CustomInput";
import CreateIcon from "@mui/icons-material/CreateRounded";
import CloseIcon from "@mui/icons-material/CloseRounded";
import { useAppDispatch, useAppSelector } from "hooks/redux";
import { createPrivateChat } from "../../store/sidebarSlice";

interface CreateNewChatProps {}

const CreateNewChat: React.FC<CreateNewChatProps> = () => {
  const [openModal, setOpenModal] = React.useState(false);
  const dispatch = useAppDispatch();
  const userIdInputRef = React.useRef<HTMLInputElement>();
  const { errorMessage, status } = useAppSelector((state) => state.sidebar.newChat);
  const { shape, spacing, breakpoints } = useTheme();

  const onOpenModal = () => {
    setOpenModal(true);
  };
  const onCloseModal = () => {
    setOpenModal(false);
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const userIdInput = userIdInputRef.current;
    if (!userIdInput) return;
    const id = userIdInput.value;

    //await dispatch(createPrivateChat(id));
  };

  React.useEffect(() => {
    if (status === "fulfilled") {
      onCloseModal();
    }
  }, [status]);

  return (
    <>
      <Box>
        <Tooltip enterDelay={500} leaveDelay={200} title="Create a new chat">
          <IconButton
            onClick={onOpenModal}
            sx={{
              svg: {
                fill: "#6A73A6",
                fontSize: { md: 40, sm: 35, xs: 30 },
              },
            }}>
            <CreateIcon />
          </IconButton>
        </Tooltip>
      </Box>
      <Modal open={openModal} onClose={onCloseModal}>
        <CreateChatForm onSubmit={handleSubmit}>
          <Box
            sx={{
              px: 2,
              bgcolor: "primary.dark",
              position: "relative",
              padding: spacing(4, 8, 8),
              borderRadius: shape.borderRadius * 3,
              [breakpoints.down("sm")]: {
                padding: spacing(4, 2, 5),
                borderRadius: shape.borderRadius * 1,
              },
              [breakpoints.down(450)]: {
                padding: spacing(4, 1, 5),
              },
            }}>
            <Box
              sx={{
                px: 2,

                [breakpoints.down(450)]: {
                  px: 1,
                },
              }}>
              <Typography
                variant="h4"
                sx={{
                  mb: 6,
                  [breakpoints.down("sm")]: {
                    fontSize: 30,
                    mb: 4,
                  },
                  [breakpoints.down(450)]: {
                    fontSize: 24,
                  },
                  [breakpoints.down(400)]: {
                    fontSize: 20,
                  },
                }}>
                Creating a new chat
              </Typography>
              <CustomInput
                {...{ ref: userIdInputRef }}
                label="Input user id or login"
                name="create-new-chat-userid"
              />
              {errorMessage && (
                <Typography variant="body1" color="error.main" mt={0.5}>
                  {errorMessage}
                </Typography>
              )}
              <Button
                variant="contained"
                type="submit"
                disableElevation
                disabled={status === "loading"}
                sx={{
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
                  [breakpoints.down("sm")]: {
                    fontSize: 16,
                    mt: 3,
                    py: 1.5,
                  },
                }}>
                Create a new chat
              </Button>
              <CloseButton onClick={onCloseModal}>
                <CloseIcon sx={{ color: "#fff", fontSize: 40 }} />
              </CloseButton>
            </Box>
          </Box>
        </CreateChatForm>
      </Modal>
    </>
  );
};

const CreateChatForm = styled("form")(
  ({ theme: { palette, spacing, shape, breakpoints } }) => ({
    position: "absolute",
    top: "50%",
    left: "50%",
    maxWidth: 600,
    width: "100%",
    transform: "translate(-50%, -50%)",
    padding: spacing(0, 2),
    "@media (max-height: 400px)": {
      position: "static",
      maxWidth: "100%",
      paddingTop: 30,
      paddingBottom: 30,
      transform: "translate(0, 0)",
      overflowY: "auto",
      height: "100%",
      display: "flex",
      justifyContent: "center",
      alignItems: "start",
    },
  })
);
const CloseButton = styled(IconButton)`
  position: absolute;
  top: 20px;
  right: 20px;
  @media (max-width: 400px) {
    right: 5px;
    top: 15px;

    svg {
      font-size: 35px;
    }
  }
`;

export { CreateNewChat };
