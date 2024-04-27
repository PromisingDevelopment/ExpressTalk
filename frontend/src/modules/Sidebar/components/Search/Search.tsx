import React from "react";
import { Box, Typography, useTheme } from "@mui/material";
import SearchIcon from "@mui/icons-material/SearchOutlined";

interface SearchProps {
  setValue: any;
}

const Search = React.forwardRef<HTMLFormElement, SearchProps>(({ setValue }, ref) => {
  const theme = useTheme();

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setValue(e.target.value);
  };

  return (
    <Box
      ref={ref}
      component="form"
      sx={{
        position: "relative",
        width: 1,
        maxWidth: 464,
        bottom: 0,
        left: 0,
        bgcolor: "#1F274E",
        px: { lg: 5.5, xs: 2 },
        borderTop: "1px solid #353F75",
        borderRight: "1px solid #353F75",

        flexBasis: 80,
        flexShrink: 0,
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        [theme.breakpoints.down(767)]: {
          px: 2,
          borderRight: 0,
        },
      }}>
      <Box
        onChange={onChange}
        component="input"
        name="search-chat"
        sx={{
          borderRadius: 3,
          width: 1,
          height: 36,
          bgcolor: "primary.main",
          px: 1,
          fontSize: 16,
          color: "#fff",
          ":focus ~ p": {
            opacity: 0,
          },
        }}
      />
      <Typography
        sx={{
          position: "absolute",
          top: "50%",
          left: "50%",
          display: "flex",
          gap: 0.8125,
          justifyContent: "center",
          alignItems: "center",
          transform: "translate(-50%, -50%)",
          pointerEvents: "none",
          color: "#6A73A6",
          transition: "all 0.2s ease 0s",
        }}>
        <SearchIcon /> Search
      </Typography>
    </Box>
  );
});

export { Search };
