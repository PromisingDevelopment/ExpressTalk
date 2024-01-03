import { PayloadAction, createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import type { IUser } from "../types/IUser";
import axios, { AxiosError } from "axios";
import { requestUrls } from "../../../config";
import { EmailFields } from "../types/EmailFields";

interface AuthScheme {
  status: "idle" | "loading" | "error" | "fulfilled";
  errorMessage: string | null;
}
interface InitialState {
  user: IUser | null;
  signUp: AuthScheme;
  signIn: AuthScheme;
  emailVerification: AuthScheme;
}

const initialState: InitialState = {
  user: null,
  signUp: {
    status: "idle",
    errorMessage: null,
  },
  signIn: {
    status: "idle",
    errorMessage: null,
  },
  emailVerification: {
    status: "idle",
    errorMessage: null,
  },
};

const authSlice = createSlice({
  name: "@@auth/",
  initialState,
  reducers: {
    resetStatus: (
      state,
      action: PayloadAction<"signUp" | "signIn" | "emailVerification">
    ) => {
      state[action.payload].status = "idle";
    },
  },
  extraReducers(builder) {
    builder
      .addCase(signUpThunk.fulfilled, (state, action) => {
        state.user = action.payload;
        state.signUp.status = "fulfilled";
      })
      .addCase(signUpThunk.rejected, (state, action: PayloadAction<any>) => {
        state.signUp.status = "error";
        state.signUp.errorMessage = action.payload?.message;
      })
      .addCase(signUpThunk.pending, (state) => {
        state.signUp.status = "loading";
        state.signUp.errorMessage = null;
      })
      .addCase(emailThunk.fulfilled, (state, action) => {
        state.emailVerification.status = "fulfilled";
      })
      .addCase(emailThunk.rejected, (state, action: PayloadAction<any>) => {
        state.emailVerification.status = "error";
        state.emailVerification.errorMessage = action.payload?.message;
      })
      .addCase(emailThunk.pending, (state) => {
        state.emailVerification.status = "loading";
        state.emailVerification.errorMessage = null;
      });
  },
});

export const signUpThunk = createAsyncThunk<IUser, IUser>(
  "@@auth/signUp",
  async (user, { rejectWithValue }) => {
    try {
      await axios.post(requestUrls.sign_up, user);

      return user;
    } catch (error) {
      return rejectWithValue(error);
    }
  }
);
export const emailThunk = createAsyncThunk<
  void,
  EmailFields,
  { state: { auth: InitialState } }
>("@@auth/email", async ({ code }, { rejectWithValue, getState }) => {
  try {
    const { user } = getState().auth;

    if (!user) {
      throw new AxiosError(
        "Email adress is not found \nPlease complete the registration by clicking the 'Back' button"
      );
    }

    const data = {
      email: user?.email,
      code: code,
    };

    await axios.post(requestUrls.email, data);
  } catch (error) {
    return rejectWithValue(error);
  }
});

export const { resetStatus } = authSlice.actions;
export default authSlice.reducer;
