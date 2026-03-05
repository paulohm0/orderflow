<template>
  <div class="users-page">
    <div class="users-header">
      <h2>Usuários</h2>
      <button @click="goToRegister" class="create-btn">Novo Usuário</button>
    </div>

    <div v-if="isLoading">Carregando usuários...</div>
    <div v-else-if="error">Erro ao carregar usuários.</div>
    <table v-else class="users-table">
      <thead>
        <tr>
          <th>Usuário</th>
          <th>E-mail</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id">
          <td>{{ user.username }}</td>
          <td>{{ user.email }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import authService from '../services/authService'

interface User {
  id?: string
  username: string
  email?: string
}

const users = ref<User[]>([])
const isLoading = ref(false)
const error = ref<string | null>(null)
const router = useRouter()

function goToRegister() {
  router.push('/register')
}

async function loadUsers() {
  isLoading.value = true
  error.value = null

  try {
    // adiciona header Authorization: <token>
    const res = await fetch('http://localhost:8080/auth/users-list', {
      headers: {
        'Content-Type': 'application/json',
        ...authService.getAuthHeader()
      }
    })
    const data = await res.json()
    // espera-se um array de usuários com campos id, username, email
    users.value = Array.isArray(data) ? data : []
  } catch (e) {
    console.error('Falha ao carregar usuários', e)
    error.value = 'Falha ao carregar usuários'
    // se retornar 401/403, redirecionar para login
    try {
      const { status } = e as any
      if (status === 401 || status === 403) {
        authService.logout()
        router.push('/')
      }
    } catch {}
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  if (!authService.isAuthenticated()) {
    router.push('/')
    return
  }
  loadUsers()
})
</script>

<style scoped>
.users-page { padding: 24px; }
.users-header { display:flex; gap:12px; align-items:center; margin-bottom:16px }
.create-btn { padding:8px 12px; background:#10b981; color:white; border:none; border-radius:6px; cursor:pointer }
.users-table { width:100%; border-collapse:collapse }
.users-table th, .users-table td { padding:8px 12px; border:1px solid #e6e6e6; text-align:left }
</style>
