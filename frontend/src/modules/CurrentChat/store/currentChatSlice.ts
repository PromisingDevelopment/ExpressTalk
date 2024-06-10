import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import axios from "axios";
import { chatGetUrls } from "config";
import { CurrentChatType } from "types/CurrentChatType";
import { GroupChat } from "types/GroupChat";
import { PrivateChat } from "../../../types/PrivateChat";

interface InitialState {
  currentChat: PrivateChat | null;
  currentGroupChat: GroupChat | null;
  status: "idle" | "loading" | "error" | "fulfilled";
  errorMessage: string | null;
}

const initialState: InitialState = {
  currentChat: null,
  currentGroupChat: null,
  status: "idle",
  errorMessage: null,
};

const narrowChatType = (
  type: CurrentChatType,
  privateCallback: any,
  groupCallback: any
) => {
  switch (type) {
    case "privateChat":
      privateCallback();
      break;
    case "groupChat":
      groupCallback();
      break;
  }
};

const currentChatSlice = createSlice({
  name: "@@currentChat",
  initialState,
  reducers: {
    setCurrentChat: (
      state,
      action: PayloadAction<{ data: any; type: CurrentChatType }>
    ) => {
      const type = action.payload.type;

      narrowChatType(
        type,
        () => (state.currentChat = action.payload.data),
        () => (state.currentGroupChat = action.payload.data)
      );
    },
    updateCurrentChat: (
      state,
      action: PayloadAction<{ data: any; type: CurrentChatType }>
    ) => {
      const type = action.payload.type;

      narrowChatType(
        type,
        () => state.currentChat?.messages.push(action.payload.data),
        () => state.currentGroupChat?.messages.push(action.payload.data)
      );
    },
    resetChats: (state) => {
      state.currentChat = null;
      state.currentGroupChat = null;
    },
  },

  extraReducers: (builder) => {
    builder
      .addCase(
        getCurrentChat.fulfilled,
        (
          state,
          action: PayloadAction<{
            data: PrivateChat | GroupChat;
            type: CurrentChatType;
          }>
        ) => {
          state.status = "fulfilled";

          const type = action.payload.type;

          if (type === "privateChat") {
            state.currentChat = action.payload.data as PrivateChat;
          }
          if (type === "groupChat") {
            state.currentGroupChat = action.payload.data as GroupChat;
          }
        }
      )
      .addCase(getCurrentChat.pending, (state) => {
        state.status = "loading";
        state.errorMessage = null;
      })
      .addCase(getCurrentChat.rejected, (state, action: PayloadAction<any>) => {
        state.status = "error";
        state.errorMessage =
          action.payload.response?.data?.message || action.payload.message;
      });
  },
});

export const getCurrentChat = createAsyncThunk<
  any,
  { id: string; type: CurrentChatType }
>("@@currentChat/getCurrentChat", async ({ id, type }, { rejectWithValue }) => {
  try {
    const { data } = await axios.get(chatGetUrls[type](id), {
      withCredentials: true,
    });

    return { data, type };
  } catch (error) {
    rejectWithValue(error);
  }
});

export const { setCurrentChat, updateCurrentChat, resetChats } = currentChatSlice.actions;

export default currentChatSlice.reducer;
