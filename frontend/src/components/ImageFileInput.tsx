import { styled } from "@mui/material";
import React from "react";
import { IMAGE_SIZE } from "constants/IMAGE_SIZE";
import { IMAGE_SUPPORTED_FORMATS } from "constants/IMAGE_SUPPORTED_FORMATS";

interface ImageFileInputProps {
  onUploadImage: (file: File) => void;
}

const ImageFileInput: React.FC<ImageFileInputProps> = ({ onUploadImage }) => {
  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = e.target.files;

    if (!files) return;

    const file = files[0];

    if (!IMAGE_SUPPORTED_FORMATS.includes(file.type)) {
      alert("Unsupported file format.");
      return;
    }

    if (file.size > IMAGE_SIZE) {
      alert("Maximum upload size exceeded. Please, take another picture");
      return;
    }

    onUploadImage(file);
  };

  return <StyledInput onChange={onChange} type="file" accept=".jpg, .jpeg, .png, .svg" />;
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
