import { PayloadAction, createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";
import { chatGetUrls, chatPostUrls } from "config";
import { ChatsListsObj } from "../types/ChatsListsObj";

interface InitialState {
  chatsList: {
    status: "idle" | "loading" | "error" | "fulfilled";
    errorMessage: string | null;
    errorCode: number | null;
    list: ChatsListsObj | null;
  };
  newChat: {
    status: "idle" | "loading" | "error" | "fulfilled";
    errorMessage: string | null;
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
  newChat: {
    status: "idle",
    errorMessage: null,
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
      })

      .addCase(createPrivateChat.fulfilled, (state, action: PayloadAction<any>) => {
        state.newChat.status = "fulfilled";
      })
      .addCase(createPrivateChat.pending, (state) => {
        state.newChat.status = "loading";
        state.newChat.errorMessage = null;
      })
      .addCase(createPrivateChat.rejected, (state, action: PayloadAction<any>) => {
        state.newChat.status = "error";
        state.newChat.errorMessage =
          action.payload?.response?.data?.message || action.payload.message;
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

export const createPrivateChat = createAsyncThunk<void, string>(
  "@@sidebar/createPrivateChat",
  async (secondMemberId, { rejectWithValue }) => {
    try {
      if (!secondMemberId) throw Error("This field is required!");

      await axios.post(
        chatPostUrls.privateChat,
        {
          secondMemberId,
        },
        { withCredentials: true }
      );
    } catch (error) {
      return rejectWithValue(error);
    }
  }
);

export const { setSidebarOpen, resetChatListError } = sidebarSlice.actions;
export default sidebarSlice.reducer;
