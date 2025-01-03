import axios, { AxiosInstance } from "axios";
export const ip = "13.250.109.196"
class Http {
  private static instance: AxiosInstance;
  static getInstance() {
    
    if (!Http.instance) {
      Http.instance = axios.create({
        baseURL: `http://${ip}:8080/`, 
        timeout: 20000,
        headers: {
          "Content-Type": "application/json",
        },
      });
    }
    return Http.instance;
  }
}

const http = Http.getInstance();

export default http;
