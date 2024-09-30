import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import axios from "axios";
import { chatDeleteUrls, chatGetUrls } from "config";
import { ChatsListObj } from "../types/ChatsListObj";
import { ChatsListType } from "types/ChatsListType";

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
    updateNameGroupInList: (state, action: PayloadAction<{ chatId: string; groupName: string }>) => {
      const groupChats = state.chatsList.list?.groupChats;
      const { chatId, groupName } = action.payload;

      const groupChat = groupChats?.find((chat: any) => chat.id === chatId);

      if (groupChat && groupName) {
        groupChat.name = groupName;
      }
    },
    updateLastMessage: (
      state,
      action: PayloadAction<{
        lastMessage: string;
        chatId: string;
        chatsType: ChatsListType;
      }>
    ) => {
      const chatsList = state.chatsList.list;
      const lastMessage = action.payload.lastMessage;
      const chatId = action.payload.chatId;
      const chatsType = action.payload.chatsType;

      if (!chatsList) return;

      const findChat = (chat: any) => chat.id === chatId;

      const chat = chatsList[chatsType].find(findChat);

      if (chat) {
        chat.lastMessage = lastMessage;
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
        state.chatsList.errorMessage = action.payload.response?.data?.message || action.payload.message;
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

export const logout = createAsyncThunk<void, void>("@@sidebar/logout", async (_, { rejectWithValue }) => {
  try {
    await axios.delete(chatDeleteUrls.logOut, {
      withCredentials: true,
    });
  } catch (error) {
    return rejectWithValue(error);
  }
});

export const { setSidebarOpen, resetChatListError, updateLastMessage, updateNameGroupInList } =
  sidebarSlice.actions;
export default sidebarSlice.reducer;
