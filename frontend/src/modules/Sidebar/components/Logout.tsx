import LogoutIcon from "@mui/icons-material/Logout";
import { useAppDispatch } from "hooks/redux";
import { resetChats } from "modules/CurrentChat";
import React from "react";
import { useNavigate } from "react-router-dom";
import { resetChatId } from "redux/rootSlice";
import CustomIconButton from "UI/CustomIconButton";
import { logout } from "../store/sidebarSlice";
import { ModalLayout } from "components/ModalLayout";

const Logout = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const onLogout = () => {
    dispatch(logout());
    dispatch(resetChats());
    dispatch(resetChatId());
    navigate("/auth/home");
  };

  const OpenButton: React.FC<{ onClick: () => void }> = ({ onClick }) => (
    <CustomIconButton Icon={LogoutIcon} label="Logout" onClick={onClick} />
  );

  return <ModalLayout Icon={LogoutIcon} label="Are you sure you want to logout" onSubmit={onLogout} />;
};

export { Logout };
