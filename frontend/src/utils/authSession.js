export const AUTH_EXPIRED_EVENT = 'datahub:auth-expired'

export function clearAuthSession() {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  window.dispatchEvent(new Event(AUTH_EXPIRED_EVENT))
}
