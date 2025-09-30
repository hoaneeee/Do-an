import axios from "axios"
import { useAuth } from "../store/auth"

export const http = axios.create({ baseURL: "/api" })

http.interceptors.request.use((config) => {
  const token = useAuth.getState().token
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

http.interceptors.response.use(
  (res) => res,
  (error) => {
    const status = error?.response?.status
    if (status === 401) {
      useAuth.getState().setToken(null)
    }
    // rất quan trọng: luôn throw ra để onSubmit bắt được
    return Promise.reject(error)
  }
)
