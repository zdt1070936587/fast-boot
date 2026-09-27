import axios, { type AxiosInstance, type InternalAxiosRequestConfig } from 'axios'
import { ElMessage } from 'element-plus'

export interface ApiResult<T> {
  code: number
  message?: string
  data: T
}

const TOKEN_KEY = 'fastboot_token'
const TOKEN_HEADER = 'Authorization'

export const http: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE || '/api/v1',
  timeout: 30000,
})

http.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = localStorage.getItem(TOKEN_KEY)
  if (token) {
    config.headers.set(TOKEN_HEADER, `Bearer ${token}`)
  }
  return config
})

http.interceptors.response.use(
  (response) => {
    const body = response.data as ApiResult<unknown>
    if (body && typeof body.code === 'number' && body.code !== 0) {
      ElMessage.error(body.message || '请求失败')
      return Promise.reject(body)
    }
    return response
  },
  (error) => {
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  },
)

export function setAuthToken(token: string | null) {
  if (token) {
    localStorage.setItem(TOKEN_KEY, token)
  } else {
    localStorage.removeItem(TOKEN_KEY)
  }
}

export async function getData<T>(url: string, params?: Record<string, unknown>) {
  const res = await http.get<ApiResult<T>>(url, { params })
  return res.data.data
}

export async function postData<T>(url: string, data?: unknown) {
  const res = await http.post<ApiResult<T>>(url, data)
  return res.data.data
}
