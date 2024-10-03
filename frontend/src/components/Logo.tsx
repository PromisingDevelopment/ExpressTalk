import ChangeAvatarIcon from "@mui/icons-material/Add";
import { Box, styled } from "@mui/material";
import avatarImage from "assets/images/avatar.png";
import React from "react";
import { ImageFileInput } from "./ImageFileInput";
import { useAppDispatch } from "hooks/redux";
import { editUserAvatar } from "redux/rootSlice";
import { AVATAR_SIZE } from "constants/AVATAR_SIZE";

interface LogoProps {
  size: number;
  ownSize?: object;
  src: string;
  isMain?: boolean;
  isAbleToChange?: boolean;
}

const Logo: React.FC<LogoProps> = ({ src, size, isMain, isAbleToChange, ownSize }) => {
  const dispatch = useAppDispatch();

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

  const onUploadImage = (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = e.target.files;

    if (files) {
      const avatar = files[0];

      if (avatar.size > AVATAR_SIZE) {
        alert("Maximum upload size exceeded. Please, take another picture");
        return;
      }

      const formData = new FormData();
      formData.append("avatarImage", avatar);

      dispatch(editUserAvatar(formData));
    }
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
        ownSize
          ? { width: ownSize, height: ownSize }
          : isMain
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
        sx={{ width: 1, height: 1, objectFit: "cover" }}
        src={src ? src : avatarImage}
        component="img"
        alt="logo"
      />
      {isAbleToChange && (
        <ChangeLogoPlaceholder title="upload avatar" className="change-logo-placeholder">
          <ImageFileInput onUploadImage={onUploadImage} />
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
