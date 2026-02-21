/**
 * Serviço de autenticação para gerenciar login, logout e token
 * Este arquivo deve ser integrado com sua API backend
 */

interface LoginCredentials {
  username: string
  password: string
}

interface AuthResponse {
  accessToken: string
  name?: string
}

interface AuthError {
  message: string
  code?: string
}

class AuthService {
  // o backend do projeto expõe endpoints em http://localhost:8080/auth
  private apiBaseUrl = 'http://localhost:8080'
  // usar a mesma chave que outras partes do frontend (LoginView gravava 'accessToken')
  private tokenKey = 'accessToken'
  private userKey = 'auth_user'

  /**
   * Realiza login do usuário
   * @param credentials - Username e senha do usuário
   * @returns Token de autenticação e dados do usuário (accessToken + name)
   */
  async login(credentials: LoginCredentials): Promise<AuthResponse> {
    try {
      const response = await fetch(`${this.apiBaseUrl}/auth/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(credentials),
      })

      if (!response.ok) {
        // tenta extrair mensagem de erro do backend
        let errorMessage = 'Falha ao fazer login'
        try {
          const error = await response.json()
          if (error && (error.message || error.mensage)) errorMessage = error.message || error.mensage
        } catch {
          // ignore
        }
        throw new Error(errorMessage)
      }

      const data: AuthResponse = await response.json()

      // backend retorna accessToken (não 'token')
      if (!data || !data.accessToken) {
        throw new Error('Resposta inválida do servidor: accessToken ausente')
      }

      this.setToken(data.accessToken)
      // salvar o nome do usuário se existir
      if (data.name) this.setUser({ name: data.name })

      return data
    } catch (error) {
      console.error('Erro na autenticação:', error)
      throw error
    }
  }

  /**
   * Realiza logout do usuário
   */
  logout(): void {
    this.removeToken()
    this.removeUser()
  }

  /**
   * Obtém o token armazenado
   */
  getToken(): string | null {
    return localStorage.getItem(this.tokenKey)
  }

  /**
   * Define o token de autenticação
   */
  private setToken(token: string): void {
    localStorage.setItem(this.tokenKey, token)
  }

  /**
   * Remove o token armazenado
   */
  private removeToken(): void {
    localStorage.removeItem(this.tokenKey)
  }

  /**
   * Obtém o usuário armazenado
   */
  getUser(): any {
    const user = localStorage.getItem(this.userKey)
    return user ? JSON.parse(user) : null
  }

  /**
   * Define os dados do usuário
   */
  private setUser(user: any): void {
    localStorage.setItem(this.userKey, JSON.stringify(user))
  }

  /**
   * Remove os dados do usuário
   */
  private removeUser(): void {
    localStorage.removeItem(this.userKey)
  }

  /**
   * Verifica se o usuário está autenticado
   */
  isAuthenticated(): boolean {
    return this.getToken() !== null
  }

  /**
   * Obtém o header de autorização
   */
  getAuthHeader(): Record<string, string> {
    const token = this.getToken()
    return token ? { Authorization: `Bearer ${token}` } : {}
  }

  /**
   * Valida se o token ainda é válido (chamada à API)
   * Obs: o backend atual pode não ter endpoint /auth/validate; essa chamada pode falhar.
   */
  async validateToken(): Promise<boolean> {
    try {
      const token = this.getToken()
      if (!token) return false

      const response = await fetch(`${this.apiBaseUrl}/auth/validate`, {
        headers: {
          ...this.getAuthHeader(),
        },
      })

      return response.ok
    } catch (error) {
      console.error('Erro ao validar token:', error)
      return false
    }
  }

  /**
   * Realiza refresh do token (se houver endpoint no backend)
   */
  async refreshToken(): Promise<string | null> {
    try {
      const response = await fetch(`${this.apiBaseUrl}/auth/refresh`, {
        method: 'POST',
        headers: {
          ...this.getAuthHeader(),
        },
      })

      if (!response.ok) {
        this.logout()
        return null
      }

      const data = await response.json()
      // esperamos { accessToken: string }
      const newToken = data?.accessToken || data?.token
      if (!newToken) {
        this.logout()
        return null
      }

      this.setToken(newToken)

      return newToken
    } catch (error) {
      console.error('Erro ao renovar token:', error)
      this.logout()
      return null
    }
  }
}

export default new AuthService()
