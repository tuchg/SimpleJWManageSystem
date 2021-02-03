import request from '@/utils/request'

const API_URL = (e) => '/admin/' + e

/**
 * 查
 * @param query
 * @returns {AxiosPromise}
 */
//此处结果会多加一个统计课程字段
export function fetchClassrooms(query) {
  return request({
    url: API_URL('list/classroom'),
    method: 'get',
    params: query
  })
}

export function delClassroom(id) {
  return request({
    url: API_URL('del/classroom'),
    method: 'post',
    data: { id: id }
  })
}

export function updateClassroom(data) {
  return request({
    url: API_URL('update/classroom'),
    method: 'post',
    data: data
  })
}

/**
 * 增
 * @param data
 * @returns {AxiosPromise}
 */
export function createClassroom(data) {
  return request({
    url: API_URL('create/classroom'),
    method: 'post',
    data: data
  })
}
