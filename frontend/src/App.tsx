import React from "react";
import { Provider } from "react-redux";
import store from "./redux/store";
import { CssBaseline, ThemeProvider } from "@mui/material";
import theme from "./theme";
import { Layout } from "./components/Layout";

function App() {
  return (
    <div className="App">
      {/*<Provider store={store}>*/}
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <Layout />
      </ThemeProvider>
      {/*</Provider>*/}
    </div>
  );
}

export default App;
