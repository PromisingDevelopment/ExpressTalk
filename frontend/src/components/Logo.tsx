import ChangeAvatarIcon from "@mui/icons-material/Add";
import { Box, styled } from "@mui/material";
import avatarImage from "assets/images/avatar.png";
import React from "react";
import { ImageFileInput } from "./ImageFileInput";

interface LogoProps {
  size: number;
  src?: string; // change to required
  isMain?: boolean;
  isAbleToChange?: boolean;
}

const Logo: React.FC<LogoProps> = ({ src = "", size, isMain, isAbleToChange }) => {
  const mainLogoSize = {
    lg: size,
    md: 45,
    xs: 40,
  };
  const defaultLogoSize = {
    lg: size,
    sm: 40,
    xs: 35,
  };

  return (
    <Box
      sx={[
        {
          borderRadius: "50%",
          overflow: "hidden",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          color: "#fff",
          position: "relative",
          flex: "0 0 auto",
          "&:hover .change-logo-placeholder": {
            opacity: 0.7,
          },
        },
        isMain
          ? {
              width: mainLogoSize,
              height: mainLogoSize,
            }
          : {
              width: defaultLogoSize,
              height: defaultLogoSize,
            },
      ]}>
      <Box
        sx={{ width: 1, height: 1 }}
        src={src ? src : avatarImage}
        component="img"
        alt="logo"
      />
      {isAbleToChange && (
        <ChangeLogoPlaceholder title="upload avatar" className="change-logo-placeholder">
          <ImageFileInput />
          <ChangeAvatarIcon />
        </ChangeLogoPlaceholder>
      )}
    </Box>
  );
};

const ChangeLogoPlaceholder = styled("div")`
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  background: #000;
  opacity: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  svg {
    width: 35px;
    height: 35px;
  }
`;

export { Logo };
