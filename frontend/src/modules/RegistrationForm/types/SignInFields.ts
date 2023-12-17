export interface SignInFields {
  loginOrGmail: string;
  password: string;
}
export type SignInLabels = keyof SignInFields;
