import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { authReducer } from "modules/RegistrationForm";
import { sidebarReducer } from "modules/Sidebar";
import { persistStore, persistReducer } from "redux-persist";
import storage from "redux-persist/lib/storage";

const persistConfig = {
  key: "root",
  storage,
};

const rootReducer = combineReducers({
  auth: authReducer,
  sidebar: sidebarReducer,
});

const persistedReducer = persistReducer(persistConfig, rootReducer);

const store = configureStore({
  reducer: persistedReducer,
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
