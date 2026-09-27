import type { App, Directive } from 'vue'
import { useUserStore } from '@/stores/user'

const permDirective: Directive<HTMLElement, string | string[]> = {
  mounted(el, binding) {
    const store = useUserStore()
    const need = binding.value
    const codes = Array.isArray(need) ? need : [need]
    const ok = codes.some((c) => store.hasPerm(c))
    if (!ok && codes.length) {
      el.style.display = 'none'
    }
  },
}

export function setupPermDirective(app: App) {
  app.directive('perm', permDirective)
}
