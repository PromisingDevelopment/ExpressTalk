import { PayloadAction, createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";
import { requestUrls } from "config";
import { ChatsListsObj } from "../types/ChatsListsObj";

interface InitialState {
  chatsList: ChatsListsObj | null;
  status: "idle" | "loading" | "error" | "fulfilled";
  errorMessage: string | null;
  sidebarOpen: boolean;
}

const initialState: InitialState = {
  chatsList: null,
  status: "idle",
  errorMessage: null,
  sidebarOpen: false,
};

const sidebarSlice = createSlice({
  name: "@@sidebar",
  initialState,
  reducers: {
    setSidebarOpen: (state, action: PayloadAction<boolean>) => {
      state.sidebarOpen = action.payload;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getChatsList.fulfilled, (state, action: PayloadAction<any>) => {
        state.status = "fulfilled";
        state.chatsList = action.payload;
      })
      .addCase(getChatsList.pending, (state, action: PayloadAction<any>) => {
        state.status = "loading";
        state.errorMessage = null;
      })

      .addCase(getChatsList.rejected, (state, action: PayloadAction<any>) => {
        state.status = "error";
        state.errorMessage =
          action.payload.response?.data?.message || action.payload.message;
      });
  },
});

export const getChatsList = createAsyncThunk<void, void>(
  "@@sidebar/getChatsList",
  async (_, { rejectWithValue }) => {
    try {
      const res = await axios.get(requestUrls.chatsList, {
        withCredentials: true,
      });

      //console.log(res);

      //return res;
    } catch (error) {
      //console.log("res", error);
      return rejectWithValue(error);
    }
  }
);

export const { setSidebarOpen } = sidebarSlice.actions;
export default sidebarSlice.reducer;
