import request from '@/utils/request'

const API_URL = (e) => '/admin/' + e

/**
 * 导出
 * @returns {AxiosPromise}
 */
export function exportTeachers() {
  return request({
    url: API_URL('export/teacher'),
    method: 'get'
  })
}

/**
 * 导入
 * @returns {AxiosPromise}
 */
export function importTeachers(data) {
  return request({
    url: API_URL('import/teacher'),
    method: 'post',
    data: data
  })
}

/**
 * 查
 * @param query
 * @returns {AxiosPromise}
 */
export function fetchTeachers(query) {
  return request({
    url: API_URL('list/teacher'),
    method: 'get',
    params: query
  })
}

export function delTeacher(query) {
  return request({
    url: API_URL('del/teacher'),
    method: 'post',
    data: query
  })
}

export function updateTeacher(data) {
  return request({
    url: API_URL('update/teacher'),
    method: 'post',
    data: data
  })
}

/**
 * 增
 * @param data
 * @returns {AxiosPromise}
 */
export function createTeacher(data) {
  return request({
    url: API_URL('create/teacher'),
    method: 'post',
    data: data
  })
}

/**
 *  修改教师密码
 * @param data
 */
export function updateTeacherPassword(data) {
  return request({
    url: API_URL('update/teacherPassword'),
    method: 'post',
    data: data
  })
}
