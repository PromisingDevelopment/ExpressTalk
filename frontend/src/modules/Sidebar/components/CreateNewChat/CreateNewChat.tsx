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
import {
  createPrivateChat,
  getSecondMember,
  resetErrorMessages,
  setCurrentChatId,
} from "redux/rootSlice";
import { connect } from "wsConfig";
import { findPrivateChatId } from "helpers/findPrivateChatId";
import { getChatsList, setSidebarOpen } from "modules/Sidebar/store/sidebarSlice";
import HeaderIconButton from "modules/Sidebar/UI/HeaderIconButton";
import ModalContent from "modules/Sidebar/UI/ModalContent";

interface CreateNewChatProps {}

const CreateNewChat: React.FC<CreateNewChatProps> = () => {
  const [openModal, setOpenModal] = React.useState(false);
  const [isCreateChat, setIsCreateChat] = React.useState(false);
  const [isConnect, setIsConnect] = React.useState(false);
  const [isEmptyLogin, setIsEmptyLogin] = React.useState(false);
  const dispatch = useAppDispatch();
  const userIdInputRef = React.useRef<HTMLInputElement | null>(null);
  const { newChat, secondMember, currentUser } = useAppSelector((state) => state.root);
  const privateChats = useAppSelector(
    (state) => state.sidebar.chatsList.list?.privateChats
  );

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
    dispatch(resetErrorMessages());

    const userIdInput = userIdInputRef.current;
    const login = userIdInput?.value;

    if (!login) return setIsEmptyLogin(true);

    await dispatch(getSecondMember(login));

    setIsCreateChat(true);
  };

  React.useEffect(() => {
    const createChat = async () => {
      const secondMemberId = secondMember.user?.id;

      if (secondMemberId && isCreateChat) {
        await dispatch(createPrivateChat(secondMemberId));
        dispatch(getChatsList());

        setIsConnect(true);
      }
      setIsCreateChat(false);
    };

    createChat();
  }, [secondMember, isCreateChat]);

  React.useEffect(() => {
    const secondMemberUser = secondMember.user;
    const currentUserId = currentUser.user?.id;

    if (newChat.status === "fulfilled" && privateChats && secondMemberUser && isConnect) {
      const chatId = findPrivateChatId(privateChats, secondMemberUser.login);

      if (!chatId) return;
      if (!currentUserId) return;

      dispatch(setCurrentChatId(chatId));
      dispatch(setSidebarOpen(false));
      connect(currentUserId, chatId);
      onCloseModal();

      setIsConnect(false);
    }
  }, [newChat.status, privateChats, secondMember.user, isConnect]);

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
            {newChat.errorMessage && (
              <Typography variant="body1" color="error.main" mt={0.5}>
                {newChat.errorMessage}
              </Typography>
            )}
            {secondMember.errorMessage && (
              <Typography variant="body1" color="error.main" mt={0.5}>
                {secondMember.errorMessage}
              </Typography>
            )}
            <Button
              variant="contained"
              type="submit"
              disableElevation
              disabled={newChat.status === "loading"}
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
