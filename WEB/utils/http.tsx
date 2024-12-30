import axios, { AxiosInstance } from "axios";
export const ngrokUrl = "https://467a-42-117-91-44.ngrok-free.app";  

class Http {
  private static instance: AxiosInstance;

  static getInstance() {
    if (!Http.instance) {
      Http.instance = axios.create({
        baseURL: `${ngrokUrl}/`,  
        timeout: 20000,
        headers: {
          "Content-Type": "application/json",
          "ngrok-skip-browser-warning": "1",
        },
      });
    }
    return Http.instance;
  }
}

const http = Http.getInstance();

export default http;
