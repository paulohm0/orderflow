<template>
  <div class="orders-page">
    <header class="orders-header">
      <div>
        <h2>{{ displayName }}</h2>
      </div>
      <button @click="logout" class="logout-btn">Sair</button>
    </header>

    <section class="create-order">
      <h3>Novo Pedido</h3>
      <form @submit.prevent="createOrder">
        <label for="description">Descrição</label>
        <input id="description" v-model="description" type="text" />

        <label for="amount">Valor</label>
        <input id="amount" v-model.number="amount" type="number" step="0.01" />

        <button type="submit" :disabled="isSubmitting">{{ isSubmitting ? 'Enviando...' : 'Criar Pedido' }}</button>
      </form>
      <div v-if="createError" class="error">{{ createError }}</div>
    </section>

    <section class="orders-list">
      <h3>Meus Pedidos</h3>
      <div v-if="isLoading">Carregando pedidos...</div>
      <div v-else-if="error">Erro ao carregar pedidos.</div>
      <div v-else>
        <div v-if="orders.length === 0">Nenhum pedido encontrado.</div>
        <ul v-else>
          <li v-for="order in orders" :key="order.id" class="order-item">
            <div class="order-main">
              <div class="order-desc">{{ order.description }}</div>
              <div class="order-amount">R$ {{ formatAmount(order.amount) }}</div>
            </div>
            <div class="order-meta">Criado: {{ formatDate(order.createdAt) }} - Status: {{ order.status }}</div>
          </li>
        </ul>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import authService from '../services/authService'

interface OrderResponse {
  id: string
  userId: string
  username: string
  description: string
  amount: number
  createdAt: string
  status: string
}

const router = useRouter()
const orders = ref<OrderResponse[]>([])
const isLoading = ref(false)
const error = ref<string | null>(null)

// Form
const description = ref('')
const amount = ref<number | null>(null)
const isSubmitting = ref(false)
const createError = ref<string | null>(null)

// User display
const displayName = ref('')

function formatAmount(value: number) {
  return Number(value).toFixed(2)
}

function formatDate(iso: string) {
  try {
    return new Date(iso).toLocaleString()
  } catch {
    return iso
  }
}

function logout() {
  authService.logout()
  router.push('/')
}

async function loadOrders() {
  isLoading.value = true
  error.value = null

  try {
    const res = await fetch('http://localhost:8080/orders/list', {
      headers: {
        'Content-Type': 'application/json',
        ...authService.getAuthHeader()
      }
    })
    if (!res.ok) throw new Error('Erro ao buscar pedidos')
    const data = await res.json()
    orders.value = Array.isArray(data) ? data : []
  } catch (e) {
    console.error('Falha ao carregar pedidos', e)
    error.value = 'Falha ao carregar pedidos'
  } finally {
    isLoading.value = false
  }
}

async function createOrder() {
  createError.value = null
  if (!description.value.trim() || amount.value === null || amount.value <= 0) {
    createError.value = 'Preencha descrição e valor válido.'
    return
  }

  isSubmitting.value = true
  try {
    const body = { description: description.value, amount: amount.value }
    const res = await fetch('http://localhost:8080/orders', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        ...authService.getAuthHeader()
      },
      body: JSON.stringify(body)
    })

    if (res.status === 201) {
      description.value = ''
      amount.value = null
      await loadOrders()
      return
    }

    let errMsg = 'Falha ao criar pedido.'
    try {
      const err = await res.json()
      errMsg = err?.message || errMsg
    } catch {}
    createError.value = errMsg
  } catch (e) {
    console.error('Erro ao criar pedido', e)
    createError.value = 'Erro ao criar pedido'
  } finally {
    isSubmitting.value = false
  }
}

onMounted(async () => {
  if (!authService.isAuthenticated()) {
    router.push('/')
    return
  }
  // obtém nome do usuário salvo (se existir) e carrega pedidos
  const user = authService.getUser()
  displayName.value = user?.name || 'Usuário'
  await loadOrders()
})
</script>

<style scoped>
.orders-page { padding: 24px; max-width: 800px; margin: 0 auto }
.orders-header { display:flex; justify-content:space-between; align-items:center; margin-bottom:16px }
.logout-btn { padding:8px 12px; background:#ef4444; color:white; border:none; border-radius:6px; cursor:pointer }
.create-order { margin-bottom:24px }
.create-order form { display:flex; flex-direction:column; gap:8px; max-width:400px }
.create-order input { padding:8px; border-radius:6px; border:1px solid #e5e7eb }
.create-order button { padding:10px; border:none; border-radius:6px; background:#10b981; color:white; cursor:pointer }
.orders-list .order-item { padding:12px; border:1px solid #e6e6e6; border-radius:6px; margin-bottom:8px }
.order-main { display:flex; justify-content:space-between; align-items:center }
.order-desc { font-weight:600 }
.order-amount { color:#065f46 }
.order-meta { margin-top:8px; font-size:12px; color:#6b7280 }
.error { color:#b91c1c; margin-top:8px }
</style>
