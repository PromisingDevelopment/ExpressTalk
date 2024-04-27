import { PayloadAction, createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";
import { chatPostUrls, userUrls } from "config";
import { IUser } from "types/IUser";

interface InitialState {
  newChat: {
    status: "idle" | "loading" | "error" | "fulfilled";
    errorMessage: string | null;
  };
  secondMember: {
    status: "idle" | "loading" | "error" | "fulfilled";
    errorMessage: string | null;
    user: IUser | null;
  };
  currentUser: {
    status: "idle" | "loading" | "error" | "fulfilled";
    user: IUser | null;
  };
  currentChatId: string | null;
}

const initialState: InitialState = {
  newChat: {
    status: "idle",
    errorMessage: null,
  },
  secondMember: {
    status: "idle",
    errorMessage: null,
    user: null,
  },
  currentUser: {
    status: "idle",
    user: null,
  },
  currentChatId: null,
};

const rootSlice = createSlice({
  name: "@@root",
  initialState,
  reducers: {
    setCurrentChatId: (state, action: PayloadAction<string>) => {
      state.currentChatId = action.payload;
    },
    resetErrorMessages: (state) => {
      state.newChat.errorMessage = null;
      state.secondMember.errorMessage = null;
    },
  },
  extraReducers(builder) {
    builder
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
      })

      .addCase(getCurrentUser.fulfilled, (state, action: PayloadAction<any>) => {
        state.currentUser.status = "fulfilled";
        state.currentUser.user = action.payload;
      })
      .addCase(getCurrentUser.pending, (state) => {
        state.currentUser.status = "loading";
      })
      .addCase(getCurrentUser.rejected, (state, action: PayloadAction<any>) => {
        state.currentUser.status = "error";
      })

      .addCase(getSecondMember.fulfilled, (state, action: PayloadAction<any>) => {
        state.secondMember.status = "fulfilled";
        state.secondMember.user = action.payload;
      })
      .addCase(getSecondMember.pending, (state) => {
        state.secondMember.status = "loading";
        state.secondMember.user = null;
      })
      .addCase(getSecondMember.rejected, (state, action: PayloadAction<any>) => {
        state.secondMember.status = "error";
        state.secondMember.errorMessage =
          action.payload?.response?.data?.message || action.payload.message;
      });
  },
});

export const createPrivateChat = createAsyncThunk<any, string>(
  "@@root/createPrivateChat",
  async (secondMemberId, { rejectWithValue }) => {
    try {
      await axios.post(
        chatPostUrls.privateChat,
        { secondMemberId: secondMemberId },
        { withCredentials: true }
      );
    } catch (error) {
      return rejectWithValue(error);
    }
  }
);

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

export const getSecondMember = createAsyncThunk<any, string>(
  "@@root/getSecondMember",
  async (login, { rejectWithValue }) => {
    try {
      const { data } = await axios.get(userUrls.user(login), { withCredentials: true });

      return data;
    } catch (error) {
      return rejectWithValue(error);
    }
  }
);

export const { setCurrentChatId, resetErrorMessages } = rootSlice.actions;

export default rootSlice.reducer;
