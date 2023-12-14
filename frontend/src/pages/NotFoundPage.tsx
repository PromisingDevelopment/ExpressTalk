import React from "react";
import { Link as RouterLink } from "react-router-dom";
import { Box, Link, Typography } from "@mui/material";

interface NotFoundPageProps {}

const NotFoundPage: React.FC<NotFoundPageProps> = () => {
  return (
    <Box
      sx={{
        height: "100vh",
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
      }}>
      <Typography mb={3} variant="h3">
        This page doesn't exist :(
      </Typography>
      <Link to="/" sx={{ color: "#fff", fontSize: 30 }} component={RouterLink}>
        Go home
      </Link>
    </Box>
  );
};

export { NotFoundPage };
