import { styled } from "@mui/material";
import React from "react";

interface ImageFileInputProps {
  onUploadImage: any;
}

const ImageFileInput: React.FC<ImageFileInputProps> = ({ onUploadImage }) => {
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
