export interface SignInFields {
  loginOrEmail: string;
  password: string;
}
export type SignInLabels = keyof SignInFields;
