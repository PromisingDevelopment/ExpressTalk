import { styled } from "@mui/material";
import { useAppDispatch } from "hooks/redux";
import React from "react";
import { editUserAvatar } from "redux/rootSlice";

interface ImageFileInputProps {}

const ImageFileInput: React.FC<ImageFileInputProps> = ({}) => {
  const dispatch = useAppDispatch();

  const onUploadImage = (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = e.target.files;

    if (files) {
      //* sending new avatar

      const avatar = files[0];

      const formData = new FormData();
      formData.append("avatarImage", avatar);

      dispatch(editUserAvatar(formData));
    }
  };

  return <StyledInput onChange={onUploadImage} type="file" accept=".jpg, .jpeg, .png, .svg" />;
};

const StyledInput = styled("input")`
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  opacity: 0;
  z-index: 1000;
  cursor: pointer;
`;

export { ImageFileInput };
