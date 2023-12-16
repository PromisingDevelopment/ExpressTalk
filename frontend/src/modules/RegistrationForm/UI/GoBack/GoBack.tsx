import React from "react";
import ArrowLeftIcon from "@mui/icons-material/KeyboardBackspaceRounded";
import { Button } from "@mui/material";
import { useNavigate } from "react-router-dom";

interface GoBackProps {}

const GoBack: React.FC<GoBackProps> = () => {
  const navigate = useNavigate();
  const handleClick = () => navigate(-1);

  return (
    <Button
      onClick={handleClick}
      sx={{
        color: "#E9EFFF",
        textDecoration: "none",
        display: "flex",
        alignItems: "center",
        fontWeight: 100,
        fontSize: "1.75rem",
        position: "absolute",
        bottom: 0,
        left: 0,
        textTransform: "none",

        ":hover": {
          svg: {
            transform: "translateX(-5px)",
          },
        },
      }}>
      <ArrowLeftIcon sx={{ mr: 1, transition: "all .3s ease 0s" }} />
      Back
    </Button>
  );
};

export { GoBack };
