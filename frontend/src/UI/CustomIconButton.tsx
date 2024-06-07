import { IconButton, Tooltip } from "@mui/material";
import React from "react";

interface CustomIconButtonProps {
  Icon: React.FC;
  label: string;
  onClick: any;
}

const CustomIconButton: React.FC<CustomIconButtonProps> = ({ Icon, label, onClick }) => {
  return (
    <Tooltip enterDelay={500} leaveDelay={200} title={label}>
      <IconButton
        onClick={onClick}
        sx={{
          svg: {
            fill: "#6A73A6",
            fontSize: { lg: 40, md: 32, sm: 30, xs: 28 },
          },
        }}>
        <Icon />
      </IconButton>
    </Tooltip>
  );
};

export default CustomIconButton;
