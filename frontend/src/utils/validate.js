export const validators = {
  phone(value) {
    const reg = /^1[3-9]\d{9}$/
    return reg.test(value)
  },

  password(value) {
    const reg = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,20}$/
    return reg.test(value)
  },

  nickname(value) {
    return value && value.length >= 2 && value.length <= 20
  }
}

export function validatePhone(phone) {
  if (!phone) return '请输入手机号'
  if (!/^\d+$/.test(phone)) return '手机号只能包含数字'
  if (phone.length !== 11) return '手机号必须为11位'
  if (!/^1[3-9]/.test(phone)) return '手机号必须以13-19开头'
  if (!validators.phone(phone)) return '请输入有效的手机号码'
  return ''
}

export function validatePassword(password) {
  if (!password) return '请输入密码'
  if (password.length < 6) return '密码至少6位'
  if (password.length > 20) return '密码最多20位'
  const reg = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,20}$/
  if (!reg.test(password)) return '密码需包含字母和数字'
  return ''
}

export function validateNickname(nickname) {
  if (!nickname) return '请输入昵称'
  if (nickname.length < 2) return '昵称至少2个字符'
  if (nickname.length > 20) return '昵称最多20个字符'
  return ''
}

export function validateConfirmPassword(password, confirmPassword) {
  if (!confirmPassword) return '请确认密码'
  if (password !== confirmPassword) return '两次密码不一致'
  return ''
}
