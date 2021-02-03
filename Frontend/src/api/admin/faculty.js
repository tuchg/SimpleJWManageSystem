import request from '@/utils/request'

const API_URL = (e) => '/admin/' + e

/**
 * 院系
 */
/**
 * 查
 * @param query
 * @returns {AxiosPromise}
 */
//此处结果会多加一个统计课程字段
export function fetchFaculties(query) {
  return request({
    url: API_URL('list/faculty'),
    method: 'get',
    params: query
  })
}

export function delFaculty(id) {
  return request({
    url: API_URL('del/faculty'),
    method: 'post',
    data: { id: id }
  })
}

export function updateFaculty(data) {
  return request({
    url: API_URL('update/faculty'),
    method: 'post',
    data: data
  })
}

/**
 * 增
 * @param data
 * @returns {AxiosPromise}
 */
export function createFaculty(data) {
  return request({
    url: API_URL('create/faculty'),
    method: 'post',
    data: data
  })
}
