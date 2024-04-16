import React from "react";
import { Provider } from "react-redux";
import { store, persistor } from "./redux/store";
import { PersistGate } from "redux-persist/integration/react";
import { CssBaseline, ThemeProvider } from "@mui/material";
import theme from "./theme";
import { Main } from "./pages/Main";
//import {  } from "./axios-test-requests";

function App() {
  return (
    <div className="App">
      <Provider store={store}>
        <PersistGate persistor={persistor} loading={null}>
          <ThemeProvider theme={theme}>
            <CssBaseline />
            <Main />
          </ThemeProvider>
        </PersistGate>
      </Provider>
    </div>
  );
}

export default App;
