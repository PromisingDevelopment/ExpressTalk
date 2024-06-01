import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { currentChatReducer } from "modules/CurrentChat";
import { authReducer } from "modules/RegistrationForm";
import { sidebarReducer } from "modules/Sidebar";
import rootReducer from "./rootSlice";
import { persistStore, persistReducer } from "redux-persist";
import storage from "redux-persist/lib/storage";

const persistConfig = {
  key: "root",
  storage,
};

const combinedReducer = combineReducers({
  auth: authReducer,
  sidebar: sidebarReducer,
  currentChat: currentChatReducer,
  root: rootReducer,
});

//const persistedReducer = persistReducer(persistConfig, combinedReducer);

const store = configureStore({
  reducer: combinedReducer,
  middleware(getDefaultMiddleware) {
    return getDefaultMiddleware({
      serializableCheck: false,
    });
  },
  devTools: true,
});

const persistor = persistStore(store);

export { persistor, store };

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
