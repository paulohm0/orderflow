/**
 * Composable para gerenciar autenticação
 * Pode ser reutilizado em múltiplos componentes
 */

import { ref, computed, onMounted } from 'vue'
import authService from '@/services/authService'
import type { LoginCredentials, User, AuthResponse } from '@/types'

interface UseAuthReturn {
  user: import('vue').Ref<User | null>
  isAuthenticated: import('vue').ComputedRef<boolean>
  isLoading: import('vue').Ref<boolean>
  error: import('vue').Ref<string | null>
  login: (credentials: LoginCredentials) => Promise<void>
  logout: () => void
  validateAuth: () => Promise<void>
  clearError: () => void
}

export function useAuth(): UseAuthReturn {
  const user = ref<User | null>(null)
  const isLoading = ref(false)
  const error = ref<string | null>(null)

  const isAuthenticated = computed(() => {
    return authService.isAuthenticated()
  })

  const login = async (credentials: LoginCredentials): Promise<void> => {
    isLoading.value = true
    error.value = null

    try {
      const response: AuthResponse = await authService.login(credentials)
      // O backend retorna { accessToken, name }
      user.value = response.name ? { name: response.name } : null
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Erro ao fazer login'
      error.value = message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const logout = (): void => {
    authService.logout()
    user.value = null
    error.value = null
  }

  const validateAuth = async (): Promise<void> => {
    if (!isAuthenticated.value) {
      user.value = null
      return
    }

    try {
      const isValid = await authService.validateToken()
      if (!isValid) {
        logout()
      } else {
        user.value = authService.getUser()
      }
    } catch (err) {
      console.error('Erro ao validar autenticação:', err)
      logout()
    }
  }

  const clearError = (): void => {
    error.value = null
  }

  // Validar autenticação ao montar o componente
  onMounted(() => {
    validateAuth()
  })

  return {
    user,
    isAuthenticated,
    isLoading,
    error,
    login,
    logout,
    validateAuth,
    clearError,
  }
}
