<template>
  <div class="login-page">
    <form class="login-form" @submit.prevent="handleLogin">
      <h2>Entrar</h2>

      <label for="username">Usuário</label>
      <input id="username" v-model="username" type="text" autocomplete="username" />

      <label for="password">Senha</label>
      <input id="password" v-model="password" type="password" autocomplete="current-password" />

      <button type="submit" :disabled="isLoading">
        {{ isLoading ? 'Entrando...' : 'Entrar' }}
      </button>

      <button type="button" class="link" @click="goToRegister">Registre-Se</button>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

// Interface para a resposta esperada
interface LoginResponse {
  accessToken: string
  name?: string
}

const username = ref('')
const password = ref('')
const isLoading = ref(false)
const router = useRouter()

function goToRegister() {
  router.push('/register')
}

async function handleLogin() {
  if (!username.value.trim() || !password.value.trim()) {
    alert('Por favor, preencha username e password.')
    return
  }

  isLoading.value = true

  try {
    const res = await fetch('http://localhost:8080/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ username: username.value, password: password.value })
    })

    // Sucesso se for 200 OK ou 201 Created
    if (res.status === 200 || res.status === 201) {
      const data = (await res.json()) as LoginResponse

      if (!data || !data.accessToken) {
        throw new Error('Resposta inválida do servidor')
      }

      try {
        localStorage.setItem('accessToken', data.accessToken)
      } catch (e) {
        // Problema ao acessar localStorage
        console.error('Não foi possível salvar o token no localStorage', e)
        alert('Erro ao salvar informações de autenticação.')
        return
      }

      // Redireciona para /users
      router.push('/users')
      return
    }

    // Qualquer outro status é tratado como erro
    throw new Error('Falha na autenticação')
  } catch (err) {
    console.error('Erro durante o login:', err)
    alert('Erro ao autenticar. Tente novamente.')
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f6f8fa;
}

.login-form {
  width: 320px;
  padding: 24px;
  border-radius: 8px;
  background: #ffffff;
  box-shadow: 0 6px 18px rgba(0,0,0,0.08);
  display: flex;
  flex-direction: column;
}

.login-form h2 {
  margin: 0 0 16px 0;
  text-align: center;
}

.login-form label {
  margin-top: 8px;
  font-size: 14px;
}

.login-form input {
  margin-top: 6px;
  padding: 8px 10px;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
}

.login-form button {
  margin-top: 16px;
  padding: 10px 12px;
  background: #2563eb;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.login-form button.link {
  margin-top: 8px;
  background: transparent;
  color: #2563eb;
  text-decoration: underline;
}

.login-form button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
