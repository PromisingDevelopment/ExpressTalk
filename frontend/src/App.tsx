import { CssBaseline, ThemeProvider } from "@mui/material";
import React from "react";
import { Provider } from "react-redux";
import { PersistGate } from "redux-persist/integration/react";
import { Main } from "./pages/Main";
import { persistor, store } from "./redux/store";
import theme from "./theme";

function App() {
  return (
    <div className="App">
      <Provider store={store}>
        {/*<PersistGate persistor={persistor} loading={null}>*/}
        <ThemeProvider theme={theme}>
          <CssBaseline />
          <Main />
        </ThemeProvider>
        {/*</PersistGate>*/}
      </Provider>
    </div>
  );
}

export default App;
