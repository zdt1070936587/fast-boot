import { onMounted, ref } from 'vue'
import http from '@/api/http'
import { ElMessage } from 'element-plus'

export function useCrudList<T extends Record<string, unknown>>(
  apiPath: string,
  idKey: keyof T & string = 'id' as keyof T & string,
) {
  const loading = ref(false)
  const rows = ref<T[]>([])
  const keyword = ref('')

  async function load() {
    loading.value = true
    try {
      const data = await http.get<T[]>(apiPath)
      rows.value = Array.isArray(data) ? data : []
    } catch {
      rows.value = []
    } finally {
      loading.value = false
    }
  }

  const filtered = () => {
    const k = keyword.value.trim().toLowerCase()
    if (!k) return rows.value
    return rows.value.filter((r) => JSON.stringify(r).toLowerCase().includes(k))
  }

  async function remove(id: unknown) {
    try {
      await http.delete(`${apiPath}/${id}`)
      ElMessage.success('已删除')
      await load()
    } catch {
      /* http 已提示 */
    }
  }

  return { loading, rows, keyword, load, filtered, remove, idKey }
}

export function useCrudListOnMount<T extends Record<string, unknown>>(apiPath: string) {
  const crud = useCrudList<T>(apiPath)
  onMounted(() => crud.load())
  return crud
}
