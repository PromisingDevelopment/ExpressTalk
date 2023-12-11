import { createTheme } from "@mui/material";

const theme = createTheme({
  typography: {
    fontFamily: "",
  },
  palette: {},
  breakpoints: {
    values: {
      xl: 1440,
      lg: 1200,
      md: 900,
      sm: 600,
      xs: 0,
    },
  },
});

console.log(theme.breakpoints.values);

export default theme;
