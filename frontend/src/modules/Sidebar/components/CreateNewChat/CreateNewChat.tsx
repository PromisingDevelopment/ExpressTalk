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
import { useAppDispatch, useAppSelector } from "hooks/redux";
import { setCurrentChatId, setIsCreatedNewChat } from "redux/rootSlice";
import { connect } from "wsConfig";
import { setSidebarOpen } from "../../store/sidebarSlice";
import HeaderIconButton from "modules/Sidebar/UI/HeaderIconButton";
import ModalContent from "modules/Sidebar/UI/ModalContent";
import { getSecondMember } from "axios/getSecondMember";
import { createPrivateChat } from "axios/createPrivateChat";
import { setCurrentChat } from "modules/CurrentChat/store/currentChatSlice";

interface CreateNewChatProps {}

const CreateNewChat: React.FC<CreateNewChatProps> = () => {
  const [openModal, setOpenModal] = React.useState(false);
  const [errorMessage, setErrorMessage] = React.useState<any>(null);
  const [isEmptyLogin, setIsEmptyLogin] = React.useState(false);
  const dispatch = useAppDispatch();
  const userIdInputRef = React.useRef<HTMLInputElement | null>(null);
  const { currentUser } = useAppSelector((state) => state.root);

  const { breakpoints } = useTheme();

  const onOpenModal = () => {
    setOpenModal(true);
  };
  const onCloseModal = () => {
    setOpenModal(false);
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setIsEmptyLogin(false);
    setErrorMessage(null);

    const userIdInput = userIdInputRef.current;
    const login = userIdInput?.value;

    if (!login) return setIsEmptyLogin(true);

    try {
      const secondMember = await getSecondMember(login);
      const currentUserId = currentUser.user?.id;

      if (secondMember.id) {
        const createdPrivateChat = await createPrivateChat(secondMember.id);
        const chatId = createdPrivateChat.id;

        if (chatId && currentUserId) {
          connect(currentUserId, chatId);
          console.log("setting is createdNewChat true");
          dispatch(setIsCreatedNewChat(true));
          dispatch(setCurrentChat(createdPrivateChat));
          dispatch(setCurrentChatId(chatId));
          dispatch(setSidebarOpen(false));
          onCloseModal();
        }
      }
    } catch (error) {
      setErrorMessage(error);
    }
  };

  return (
    <>
      <HeaderIconButton
        label="Create a new chat"
        onClick={onOpenModal}
        Icon={CreateIcon}
      />

      <Modal open={openModal} onClose={onCloseModal}>
        <ModalContent title="Creating a new chat" onCloseModal={onCloseModal}>
          <form onSubmit={handleSubmit}>
            <CustomInput
              ref={userIdInputRef}
              label="Input user id or login"
              name="create-new-chat-userid"
            />
            {isEmptyLogin && (
              <Typography variant="body1" color="error.main" mt={0.5}>
                This field is required!
              </Typography>
            )}
            {errorMessage && (
              <Typography variant="body1" color="error.main" mt={0.5}>
                {errorMessage}
              </Typography>
            )}

            <Button
              variant="contained"
              type="submit"
              disableElevation
              //disabled={newChat.status === "loading"}
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
          </form>
        </ModalContent>
      </Modal>
    </>
  );
};

export { CreateNewChat };
