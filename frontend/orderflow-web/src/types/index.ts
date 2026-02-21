/**
 * Tipos e interfaces globais da aplicação
 */

export interface User {
  id?: string
  email?: string
  name: string
  role?: string
  createdAt?: string
}

export interface LoginCredentials {
  username: string
  password: string
}

export interface AuthResponse {
  accessToken: string
  name?: string
}

export interface ApiError {
  message: string
  code?: string
  details?: Record<string, any>
}

export interface PaginationParams {
  page: number
  limit: number
  sort?: string
  order?: 'asc' | 'desc'
}

export interface PaginatedResponse<T> {
  data: T[]
  total: number
  page: number
  limit: number
  totalPages: number
}
