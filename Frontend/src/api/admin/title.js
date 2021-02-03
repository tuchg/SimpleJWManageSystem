import request from '@/utils/request'

const API_URL = (e) => '/admin/' + e

/**
 * 职称
 */

/**
 * 查
 * @param query
 * @returns {AxiosPromise}
 */
//此处结果会多加一个统计职称人数字段
export function fetchTitles(query) {
  return request({
    url: API_URL('list/title'),
    method: 'get',
    params: query
  })
}

export function delTitle(id) {
  return request({
    url: API_URL('del/title'),
    method: 'post',
    data: { id: id }
  })
}

export function updateTitle(data) {
  return request({
    url: API_URL('update/title'),
    method: 'post',
    data: data
  })
}

/**
 * 增
 * @param data
 * @returns {AxiosPromise}
 */
export function createTitle(data) {
  return request({
    url: API_URL('create/title'),
    method: 'post',
    data: data
  })
}
