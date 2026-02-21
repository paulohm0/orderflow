<template>
  <div class="register-page">
    <form class="register-form" @submit.prevent="handleRegister">
      <h2>Registre-se</h2>

      <label for="username">Usuário</label>
      <input id="username" v-model="username" type="text" autocomplete="username" />

      <label for="email">E-mail</label>
      <input id="email" v-model="email" type="email" autocomplete="email" />

      <label for="password">Senha</label>
      <input id="password" v-model="password" type="password" autocomplete="new-password" />

      <button type="submit" :disabled="isLoading">
        {{ isLoading ? 'Registrando...' : 'Registrar' }}
      </button>

      <button type="button" class="link" @click="goToLogin">Voltar para Login</button>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const username = ref('')
const email = ref('')
const password = ref('')
const isLoading = ref(false)
const router = useRouter()

async function handleRegister() {
  if (!username.value.trim() || !password.value.trim() || !email.value.trim()) {
    alert('Por favor, preencha todos os campos.')
    return
  }

  isLoading.value = true

  try {
    const res = await fetch('http://localhost:8080/auth/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username: username.value, password: password.value, email: email.value })
    })

    if (res.status === 201) {
      alert('Usuário criado com sucesso. Faça login agora.')
      router.push('/')
      return
    }

    // qualquer outro status -> erro
    throw new Error('Falha ao registrar')
  } catch (e) {
    console.error('Erro ao registrar:', e)
    alert('Erro ao registrar. Tente novamente.')
  } finally {
    isLoading.value = false
  }
}

function goToLogin() {
  router.push('/')
}
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f6f8fa;
}

.register-form {
  width: 360px;
  padding: 24px;
  border-radius: 8px;
  background: #ffffff;
  box-shadow: 0 6px 18px rgba(0,0,0,0.08);
  display: flex;
  flex-direction: column;
}

.register-form h2 {
  margin: 0 0 16px 0;
  text-align: center;
}

.register-form label { margin-top: 8px; font-size: 14px; }
.register-form input { margin-top: 6px; padding: 8px 10px; border-radius: 4px; border: 1px solid #dcdfe6; }
.register-form button { margin-top: 16px; padding: 10px 12px; background: #10b981; color: white; border: none; border-radius: 6px; cursor: pointer; }
.register-form button.link { background: transparent; color: #2563eb; margin-top: 8px; text-decoration: underline; }
.register-form button:disabled { opacity: 0.6; cursor: not-allowed; }
</style>
