import axios from "axios";

async function axiosRequest(url: string, method: "get" | "post", data?: any) {
  try {
    const response = await axios[method](url, data, { withCredentials: true });
    return response.data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      throw error?.response?.data?.message || error.message;
    } else {
      throw "Network Error: Unable to connect to the server.";
    }
  }
}

export { axiosRequest };
