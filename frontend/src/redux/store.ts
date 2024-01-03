import { configureStore } from "@reduxjs/toolkit";
import { authReducer } from "../modules/RegistrationForm";

const store = configureStore({
  reducer: {
    auth: authReducer,
  },
  middleware(getDefaultMiddleware) {
    return getDefaultMiddleware({
      serializableCheck: false,
    });
  },
  devTools: true,
});

export default store;

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
