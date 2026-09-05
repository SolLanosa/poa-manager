import CONFIG from "@/config/config";
import axios from "axios";

const server = axios.create({
  baseURL: CONFIG.API_BASE_URL,
});

export default server;
