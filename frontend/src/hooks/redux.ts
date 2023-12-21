import React from "react";
import { useDispatch, useSelector, TypedUseSelectorHook } from "react-redux";
import type { AppDispatch, RootState } from "../redux/store";

export const useAppDispatch = () => useDispatch<AppDispatch>();
export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector;
