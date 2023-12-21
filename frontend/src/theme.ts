import { createTheme } from "@mui/material";

const theme = createTheme({
  typography: {
    fontFamily: "Roboto",
  },
  palette: {
    primary: {
      light: "#37427A",
      main: "#353F75",
      dark: "#202957",
    },
    text: {
      primary: "#fff",
      secondary: "#848CAB",
    },
    background: {
      default: "#353F75",
    },
  },
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
