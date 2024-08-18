import { styled } from "@mui/material";
import React from "react";

interface ImageFileInputProps {}

const ImageFileInput: React.FC<ImageFileInputProps> = ({}) => {
  const onUploadImage = (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = e.target.files;

    if (files) {
      const file = files[0];

      const formData = new FormData();
      formData.append("image", file);

      console.log(file, formData);
      //* sending new avatar
    }
  };

  return (
    <StyledInput onChange={onUploadImage} type="file" accept=".jpg, .jpeg, .png, .svg" />
  );
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
