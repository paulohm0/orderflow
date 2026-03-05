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
import authService from "@/services/authService";

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
    await authService.login({
      username: username.value,
      password: password.value
    })

    router.push('/orders')
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
