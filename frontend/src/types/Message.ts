export interface Message<S, R> {
  isSystemMessages: boolean; //додати
  id: string;
  content: string;
  createdAt: number;
  attachedFile: {
    id: string;
    name: string;
    type: string;
    data: string[];
    message: {
      id: string;
      content: string;
      createdAt: string;
    };
  };
  sender: S;
  receiver: R | null;
}
