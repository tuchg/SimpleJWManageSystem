import request from '@/utils/request'

const API_URL = (e) => '/admin/' + e

/**
 *  更新管理员
 * @param data
 */
export function updateAdmin(data) {
  return request({
    url: API_URL('update/admin'),
    method: 'post',
    data: data
  })
}

/**
 *  修改管理员密码
 * @param data
 */
export function updateAdminPassword(data) {
  return request({
    url: API_URL('update/adminPassword'),
    method: 'post',
    data: data
  })
}

/**
 * 获取首页数据
 * @param data
 */
export function fetchHomeData() {
  return request({
    url: API_URL('home'),
    method: 'get'
  })
}
