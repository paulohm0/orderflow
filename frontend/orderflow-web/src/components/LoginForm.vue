<script setup lang="ts">
import { ref, computed } from 'vue'

interface LoginForm {
  email: string
  password: string
}

const form = ref<LoginForm>({
  email: '',
  password: ''
})

const errors = ref<Partial<LoginForm>>({})
const isLoading = ref(false)
const showPassword = ref(false)

const isFormValid = computed(() => {
  return form.value.email.trim() !== '' && form.value.password.trim() !== ''
})

const validateForm = (): boolean => {
  errors.value = {}

  if (!form.value.email) {
    errors.value.email = 'Email é obrigatório'
  } else if (!isValidEmail(form.value.email)) {
    errors.value.email = 'Email inválido'
  }

  if (!form.value.password) {
    errors.value.password = 'Senha é obrigatória'
  } else if (form.value.password.length < 6) {
    errors.value.password = 'Senha deve ter pelo menos 6 caracteres'
  }

  return Object.keys(errors.value).length === 0
}

const isValidEmail = (email: string): boolean => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return emailRegex.test(email)
}

const handleSubmit = async (): Promise<void> => {
  if (!validateForm()) {
    return
  }

  isLoading.value = true

  try {
    // TODO: Integrar com API de autenticação
    console.log('Login attempt:', form.value)

    // Simular chamada à API
    await new Promise(resolve => setTimeout(resolve, 1500))

    alert('Login realizado com sucesso!')
  } catch (error) {
    console.error('Erro ao fazer login:', error)
  } finally {
    isLoading.value = false
  }
}

const resetForm = (): void => {
  form.value = { email: '', password: '' }
  errors.value = {}
}
</script>

<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center px-4">
    <div class="w-full max-w-md">
      <!-- Card de Login -->
      <div class="bg-white rounded-lg shadow-lg p-8">
        <!-- Header -->
        <div class="text-center mb-8">
          <h1 class="text-3xl font-bold text-gray-800">OrderFlow</h1>
          <p class="text-gray-600 text-sm mt-2">Acesse sua conta</p>
        </div>

        <!-- Form -->
        <form @submit.prevent="handleSubmit" class="space-y-6">
          <!-- Email Field -->
          <div>
            <label for="email" class="block text-sm font-medium text-gray-700 mb-2">
              Email
            </label>
            <input
              id="email"
              v-model="form.email"
              type="email"
              placeholder="seu.email@example.com"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition"
              :class="{ 'border-red-500 focus:ring-red-500': errors.email }"
            />
            <p v-if="errors.email" class="text-red-500 text-xs mt-1">
              {{ errors.email }}
            </p>
          </div>

          <!-- Password Field -->
          <div>
            <label for="password" class="block text-sm font-medium text-gray-700 mb-2">
              Senha
            </label>
            <div class="relative">
              <input
                id="password"
                v-model="form.password"
                :type="showPassword ? 'text' : 'password'"
                placeholder="••••••••"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none transition pr-10"
                :class="{ 'border-red-500 focus:ring-red-500': errors.password }"
              />
              <button
                type="button"
                @click="showPassword = !showPassword"
                class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-500 hover:text-gray-700 transition"
              >
                <svg
                  v-if="!showPassword"
                  class="w-5 h-5"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
                  />
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"
                  />
                </svg>
                <svg
                  v-else
                  class="w-5 h-5"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-4.803m5.596-3.856a3.375 3.375 0 11-4.753 4.753m7.313-1.172l1.414 1.414M9.172 9.172L7.757 7.757m9.534 9.534l1.414 1.414M3.458 11.632l1.414 1.414"
                  />
                </svg>
              </button>
            </div>
            <p v-if="errors.password" class="text-red-500 text-xs mt-1">
              {{ errors.password }}
            </p>
          </div>

          <!-- Remember Me and Forgot Password -->
          <div class="flex items-center justify-between text-sm">
            <label class="flex items-center">
              <input type="checkbox" class="w-4 h-4 text-indigo-600 border-gray-300 rounded focus:ring-indigo-500" />
              <span class="ml-2 text-gray-700">Lembrar-me</span>
            </label>
            <a href="#" class="text-indigo-600 hover:text-indigo-700 font-medium">
              Esqueceu a senha?
            </a>
          </div>

          <!-- Submit Button -->
          <button
            type="submit"
            :disabled="isLoading"
            class="w-full bg-indigo-600 hover:bg-indigo-700 disabled:bg-indigo-400 text-white font-semibold py-2 px-4 rounded-lg transition duration-200 flex items-center justify-center"
          >
            <svg
              v-if="isLoading"
              class="animate-spin -ml-1 mr-3 h-5 w-5 text-white"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
            >
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" />
              <path
                class="opacity-75"
                fill="currentColor"
                d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
              />
            </svg>
            {{ isLoading ? 'Entrando...' : 'Entrar' }}
          </button>
        </form>

        <!-- Divider -->
        <div class="relative my-6">
          <div class="absolute inset-0 flex items-center">
            <div class="w-full border-t border-gray-300" />
          </div>
          <div class="relative flex justify-center text-sm">
            <span class="px-2 bg-white text-gray-500">ou</span>
          </div>
        </div>

        <!-- Sign Up Link -->
        <div class="text-center">
          <p class="text-gray-600 text-sm">
            Não tem uma conta?
            <a href="#" class="text-indigo-600 hover:text-indigo-700 font-semibold">
              Cadastre-se agora
            </a>
          </p>
        </div>

        <!-- Reset Button (Development) -->
        <button
          type="button"
          @click="resetForm"
          class="w-full mt-4 text-gray-500 hover:text-gray-700 text-xs py-2 px-4 border border-gray-300 rounded-lg hover:bg-gray-50 transition"
        >
          Limpar Formulário
        </button>
      </div>

      <!-- Footer -->
      <div class="text-center mt-8 text-gray-600 text-xs">
        <p>© 2026 OrderFlow. Todos os direitos reservados.</p>
      </div>
    </div>
  </div>
</template>

