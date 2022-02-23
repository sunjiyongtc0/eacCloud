import request from '@/utils/request'

export function login(data) {
  data['scope'] = 'ui'
  data['grant_type'] = 'password'
  data['client_id'] = 'browser'
  data['type'] = 'account'
  return request({
    url: '/api/uaa/oauth/token',
    method: 'post',
    headers: { 'Authorization': 'Basic YnJvd3Nlcjo=', 'Content-Type': 'application/x-www-form-urlencoded' },
    params: data
  })
}

export function loginMobile(data) {
  data['scope'] = 'ui'
  data['grant_type'] = 'mobile'
  data['client_id'] = 'browser'
  data['type'] = 'mobile'
  return request({
    url: '/api/uaa/oauth/token',
    method: 'post',
    headers: { 'Authorization': 'Basic YnJvd3Nlcjo=', 'Content-Type': 'application/x-www-form-urlencoded' },
    params: data
  })
}

export function queryCurrentAuthority(data) {
  return request({
    url: '/api/account/sysuserdetail/queryCurrentAuthority',
    method: 'post',
    params: data
  })
}

export function getInfo(token) {
  return request({
    url: '/api/uaa/users/getSysUser',
    method: 'get'
  })
}

export function compareCaptcha(data,key) {
  return request({
    url: '/api/account/sysuserdetail/compareCaptcha',
    method: 'post',
    params: {
      charCaptcha: data,
      key: key
    }
  })
}

export function registerNewSession() {
  return request({
    url: '/api/admin/sysuser/registerNewSession',
    method: 'post'
  })
}

export function getSmsCaptcha(data) {
  return request({
    url: '/api/account/captcha/getSmsCaptcha',
    method: 'get',
    params: {
      mobile: data
    }
  })
}

export function retrievePassword(email, charCaptcha) {
  return request({
    url: '/api/account/sysuserdetail/retrievePassword',
    method: 'post',
    params: {
      email: email,
      charCaptcha: charCaptcha
    }
  })
}

export function registerAccount(data) {
  return request({
    url: '/api/account/sysuserdetail/registerAccount',
    method: 'post',
    data
  })
}
