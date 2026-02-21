/**
 * EXEMPLO DE USO DO COMPOSABLE useAuth COM O LOGINFORM
 *
 * Este arquivo mostra como integrar o composable useAuth com o componente LoginForm.vue
 * Descomente e adapte o código do LoginForm.vue para usar esta implementação
 */

/**
 * Código para substituir no LoginForm.vue:
 *
 * <script setup lang="ts">
 * import { ref, computed } from 'vue'
 * import { useAuth } from '@/composables/useAuth'
 * import { useRouter } from 'vue-router'
 *
 * const router = useRouter()
 * const { login, isLoading, error, clearError } = useAuth()
 *
 * interface LoginForm {
 *   email: string
 *   password: string
 * }
 *
 * const form = ref<LoginForm>({
 *   email: '',
 *   password: ''
 * })
 *
 * const formErrors = ref<Partial<LoginForm>>({})
 * const showPassword = ref(false)
 *
 * const isFormValid = computed(() => {
 *   return form.value.email.trim() !== '' && form.value.password.trim() !== ''
 * })
 *
 * const validateForm = (): boolean => {
 *   formErrors.value = {}
 *
 *   if (!form.value.email) {
 *     formErrors.value.email = 'Email é obrigatório'
 *   } else if (!isValidEmail(form.value.email)) {
 *     formErrors.value.email = 'Email inválido'
 *   }
 *
 *   if (!form.value.password) {
 *     formErrors.value.password = 'Senha é obrigatória'
 *   } else if (form.value.password.length < 6) {
 *     formErrors.value.password = 'Senha deve ter pelo menos 6 caracteres'
 *   }
 *
 *   return Object.keys(formErrors.value).length === 0
 * }
 *
 * const isValidEmail = (email: string): boolean => {
 *   const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
 *   return emailRegex.test(email)
 * }
 *
 * const handleSubmit = async (): Promise<void> => {
 *   if (!validateForm()) {
 *     return
 *   }
 *
 *   try {
 *     await login(form.value)
 *     // Redirecionar para dashboard após login bem-sucedido
 *     router.push('/dashboard')
 *   } catch (err) {
 *     // O erro já é tratado pelo composable
 *     console.error('Erro ao fazer login:', err)
 *   }
 * }
 *
 * const resetForm = (): void => {
 *   form.value = { email: '', password: '' }
 *   formErrors.value = {}
 *   clearError()
 * }
 * </script>
 *
 * // No template, substitua referências de 'errors' por 'formErrors'
 * // e use 'error' do composable para mostrar erros de autenticação
 */

export const authComposableExample = () => {
  console.log('Veja o código comentado neste arquivo para integrar com LoginForm.vue')
}

