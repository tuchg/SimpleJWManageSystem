import request from '@/utils/request'

const API_URL = (e) => '/admin/' + e

/**
 * 导出
 * @returns {AxiosPromise}
 */
export function exportStudents() {
  return request({
    url: API_URL('export/student'),
    method: 'get'
  })
}

/**
 * 导入
 * @returns {AxiosPromise}
 */
export function importStudents(data) {
  console.log(data)
  return request({
    url: API_URL('import/student'),
    method: 'post',
    data: data
  })
}

/**
 * 查
 * @param query
 * @returns {AxiosPromise}
 */

export function fetchStudents(query) {
  return request({
    url: API_URL('list/student'),
    method: 'get',
    params: query
  })
}

export function delStudent(query) {
  return request({
    url: API_URL('del/student'),
    method: 'post',
    data: query
  })
}

export function updateStudent(data) {
  return request({
    url: API_URL('update/student'),
    method: 'post',
    data: data
  })
}

/**
 * 增
 * @param data
 * @returns {AxiosPromise}
 */
export function createStudent(data) {
  return request({
    url: API_URL('create/student'),
    method: 'post',
    data: data
  })
}

/**
 *  修改学生密码
 * @param data
 */
export function updateStudentPassword(data) {
  return request({
    url: API_URL('update/studentPassword'),
    method: 'post',
    data: data
  })
}
