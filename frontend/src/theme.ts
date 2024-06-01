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
      paper: "#222B5A",
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

//console.log(theme);

export default theme;
