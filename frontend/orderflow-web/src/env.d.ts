/// <reference types="vite/client" />

// Define explicitamente as variáveis de ambiente usadas no projeto
interface ImportMetaEnv {
  readonly VITE_API_URL?: string
  // adicione outras VITE_... vars aqui, se necessário
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}

