export interface SignUpFields {
  login: string;
  name: string;
  email: string;
  password: string;
}

export type SignUpLabels = keyof SignUpFields;
