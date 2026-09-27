import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getData, postData, setAuthToken } from '@/api/http'

export interface AuthInfo {
  userId: number
  username: string
  displayName: string
  orgUnitId?: number
}

export const useUserStore = defineStore('user', () => {
  const token = ref<string | null>(localStorage.getItem('fastboot_token'))
  const profile = ref<AuthInfo | null>(null)

  async function login(username: string, password: string) {
    const data = await postData<{ tokenName: string; tokenValue: string }>('/auth/login', {
      username,
      password,
    })
    token.value = data.tokenValue
    setAuthToken(data.tokenValue)
    await fetchProfile()
  }

  async function fetchProfile() {
    profile.value = await getData<AuthInfo>('/auth/info')
  }

  async function logout() {
    try {
      await postData('/auth/logout')
    } finally {
      token.value = null
      profile.value = null
      setAuthToken(null)
    }
  }

  return { token, profile, login, fetchProfile, logout }
})
