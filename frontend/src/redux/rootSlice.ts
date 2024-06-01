import { PayloadAction, createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";
import { chatPostUrls, userUrls } from "config";
import { IUser } from "types/IUser";

interface InitialState {
  currentUser: {
    status: "idle" | "loading" | "error" | "fulfilled";
    user: IUser | null;
    errorCode: number | null;
  };
  currentChatId: string | null;
  isCreatedNewChat: boolean;
}

const initialState: InitialState = {
  currentUser: {
    status: "idle",
    user: null,
    errorCode: null,
  },
  currentChatId: null,
  isCreatedNewChat: false,
};

const rootSlice = createSlice({
  name: "@@root",
  initialState,
  reducers: {
    setCurrentChatId: (state, action: PayloadAction<string>) => {
      state.currentChatId = action.payload;
    },
    setIsCreatedNewChat: (state, action: PayloadAction<boolean>) => {
      state.isCreatedNewChat = action.payload;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getCurrentUser.fulfilled, (state, action: PayloadAction<any>) => {
        state.currentUser.status = "fulfilled";
        state.currentUser.user = action.payload;
      })
      .addCase(getCurrentUser.pending, (state) => {
        state.currentUser.status = "loading";
      })
      .addCase(getCurrentUser.rejected, (state, action: PayloadAction<any>) => {
        state.currentUser.status = "error";
        state.currentUser.errorCode = action.payload.response.status || null;
      });
  },
});

export const getCurrentUser = createAsyncThunk<any, void>(
  "@@root/getCurrentUser",
  async (_, { rejectWithValue }) => {
    try {
      const { data } = await axios.get(userUrls.self, { withCredentials: true });

      return data;
    } catch (error) {
      return rejectWithValue(error);
    }
  }
);

export const { setCurrentChatId, setIsCreatedNewChat } = rootSlice.actions;

export default rootSlice.reducer;
