import { PayloadAction, createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";
import { chatDeleteUrls, chatGetUrls } from "config";
import { ChatsListObj } from "../types/ChatsListObj";

interface InitialState {
  chatsList: {
    status: "idle" | "loading" | "error" | "fulfilled";
    errorMessage: string | null;
    errorCode: number | null;
    list: ChatsListObj | null;
  };
  sidebarOpen: boolean;
}

const initialState: InitialState = {
  chatsList: {
    status: "idle",
    errorMessage: null,
    errorCode: null,
    list: null,
  },
  sidebarOpen: false,
};

const sidebarSlice = createSlice({
  name: "@@sidebar",
  initialState,
  reducers: {
    setSidebarOpen: (state, action: PayloadAction<boolean>) => {
      state.sidebarOpen = action.payload;
    },
    resetChatListError: (state) => {
      state.chatsList.status = "idle";
      state.chatsList.errorMessage = null;
      state.chatsList.errorCode = null;
    },
    updateLastMessage: (
      state,
      action: PayloadAction<{ lastMessage: string; chatId: string }>
    ) => {
      const list = state.chatsList.list;

      if (list) {
        const chat = list.privateChats.find((chat) => chat.id === action.payload.chatId);

        if (chat) {
          chat.lastMessage = action.payload.lastMessage;
        }
      }
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getChatsList.fulfilled, (state, action: PayloadAction<any>) => {
        state.chatsList.status = "fulfilled";
        state.chatsList.list = action.payload;
      })
      .addCase(getChatsList.pending, (state) => {
        state.chatsList.status = "loading";
        state.chatsList.errorMessage = null;
        state.chatsList.errorCode = null;
      })
      .addCase(getChatsList.rejected, (state, action: PayloadAction<any>) => {
        state.chatsList.status = "error";
        state.chatsList.errorMessage =
          action.payload.response?.data?.message || action.payload.message;
        state.chatsList.errorCode = action.payload.response?.data?.status || 500;
      });
  },
});

export const getChatsList = createAsyncThunk<any, void>(
  "@@sidebar/getChatsList",
  async (_, { rejectWithValue }) => {
    try {
      const res = await axios.get(chatGetUrls.chatsList, {
        withCredentials: true,
      });

      return res.data;
    } catch (error) {
      return rejectWithValue(error);
    }
  }
);

export const logout = createAsyncThunk<void, void>(
  "@@sidebar/logout",
  async (_, { rejectWithValue }) => {
    try {
      await axios.delete(chatDeleteUrls.logOut, {
        withCredentials: true,
      });
    } catch (error) {
      return rejectWithValue(error);
    }
  }
);

export const { setSidebarOpen, resetChatListError, updateLastMessage } =
  sidebarSlice.actions;
export default sidebarSlice.reducer;
