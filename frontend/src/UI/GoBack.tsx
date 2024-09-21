import React from "react";
import ArrowLeftIcon from "@mui/icons-material/KeyboardBackspaceRounded";
import { Button, SxProps, useTheme } from "@mui/material";
import { useNavigate } from "react-router-dom";

interface GoBackProps {
  onClick?: any;
  sx?: SxProps;
}

const GoBack: React.FC<GoBackProps> = ({ onClick, sx }) => {
  const theme = useTheme();
  const navigate = useNavigate();
  const handleClick = () => navigate(-1);

  return (
    <Button
      onClick={onClick || handleClick}
      sx={{
        color: "#E9EFFF",
        textDecoration: "none",
        display: "flex",
        alignItems: "center",
        fontWeight: 100,
        fontSize: "1.75rem",
        position: "absolute",
        bottom: -20,
        left: 0,
        textTransform: "none",

        ":hover": {
          svg: {
            transform: "translateX(-5px)",
          },
        },

        [theme.breakpoints.down("sm")]: {
          fontSize: "1.4rem",
        },

        ...sx,
      }}>
      <ArrowLeftIcon sx={{ mr: 1, transition: "all .3s ease 0s" }} />
      Back
    </Button>
  );
};

export { GoBack };
