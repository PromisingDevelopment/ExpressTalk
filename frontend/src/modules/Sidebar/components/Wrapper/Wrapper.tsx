import React from "react";
import { Box, Drawer } from "@mui/material";
import { useAppDispatch, useAppSelector } from "hooks/redux";
import { useIsMobile } from "hooks/useIsMobile";
import { setSidebarOpen } from "../../store/sidebarSlice";

interface WrapperProps {
  children: React.ReactNode;
}

const Wrapper: React.FC<WrapperProps> = ({ children }) => {
  const dispatch = useAppDispatch();
  const isOpen = useAppSelector((state) => state.sidebar.sidebarOpen);
  const isMobile = useIsMobile();

  const onClose = () => {
    dispatch(setSidebarOpen(false));
  };

  return (
    <Drawer
      open={isOpen}
      onClose={onClose}
      variant={isMobile ? "temporary" : "permanent"}
      ModalProps={{
        keepMounted: true,
      }}
      sx={[
        {
          width: { lg: 464, md: 350, sm: 300, xs: 250 },
          height: "100vh",
        },
        !isMobile && {
          "& > div": {
            width: "inherit",
            overflow: "hidden",
          },
        },
      ]}>
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          width: { lg: 464, md: 350, sm: 300, xs: 250 },
        }}>
        {children}
      </Box>
    </Drawer>
  );
};

export { Wrapper };
