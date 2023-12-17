export interface SignUpFields {
  login: string;
  name: string;
  gmail: string;
  password: string;
}

export type SignUpLabels = keyof SignUpFields;
