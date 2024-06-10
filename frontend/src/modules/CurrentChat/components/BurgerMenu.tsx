import MenuIcon from "@mui/icons-material/MenuRounded";
import { IconButton } from "@mui/material";
import { useAppDispatch } from "hooks/redux";
import { useIsMobile } from "hooks/useIsMobile";
import { setSidebarOpen } from "modules/Sidebar/store/sidebarSlice";
import React from "react";

interface BurgerMenuProps {}

const BurgerMenu: React.FC<BurgerMenuProps> = () => {
  const dispatch = useAppDispatch();
  const isMobile = useIsMobile();

  const onClickMenu = () => {
    dispatch(setSidebarOpen(true));
  };

  if (!isMobile) return;

  return (
    <IconButton onClick={onClickMenu} sx={{ mr: 1 }}>
      <MenuIcon
        sx={{
          color: "#6972A5",
          fontSize: 40,
        }}
      />
    </IconButton>
  );
};

export { BurgerMenu };
