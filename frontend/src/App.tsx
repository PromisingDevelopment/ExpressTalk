import React from "react";
import { Provider } from "react-redux";
import store from "./redux/store";
import { CssBaseline, ThemeProvider } from "@mui/material";
import theme from "./theme";
import { Main } from "./pages/Main";

function App() {
  return (
    <div className="App">
      {/*<Provider store={store}>*/}
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <Main />
      </ThemeProvider>
      {/*</Provider>*/}
    </div>
  );
}

export default App;
