import { PayloadAction, createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";
import { chatGetUrls } from "config";
import { CurrentPrivateChat } from "../../../types/CurrentPrivateChat";
import { CurrentChatType } from "types/CurrentChatType";
import { GroupChat } from "types/GroupChat";

interface InitialState {
  currentChat: CurrentPrivateChat | null;
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

const currentChatSlice = createSlice({
  name: "@@currentChat",
  initialState,
  reducers: {
    setCurrentChat: (
      state,
      action: PayloadAction<{ data: any; type: CurrentChatType }>
    ) => {
      const type = action.payload.type;

      switch (type) {
        case "privateChat":
          state.currentChat = action.payload.data;
          break;
        case "groupChat":
          state.currentGroupChat = action.payload.data;
          break;
      }
    },
  },

  extraReducers: (builder) => {
    builder
      .addCase(
        getCurrentChat.fulfilled,
        (
          state,
          action: PayloadAction<{
            data: CurrentPrivateChat | GroupChat;
            type: CurrentChatType;
          }>
        ) => {
          state.status = "fulfilled";

          const type = action.payload.type;

          if (type === "privateChat") {
            state.currentChat = action.payload.data as CurrentPrivateChat;
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

export const { setCurrentChat } = currentChatSlice.actions;

export default currentChatSlice.reducer;
