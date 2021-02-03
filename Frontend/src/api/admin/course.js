import request from '@/utils/request'

const API_URL = (e) => '/admin/' + e

/**
 * 导出
 * @returns {AxiosPromise}
 */
export function exportCourses() {
  return request({
    url: API_URL('export/course'),
    method: 'get'
  })
}

/**
 * 导入
 * @returns {AxiosPromise}
 */
export function importCourses(data) {
  return request({
    url: API_URL('import/course'),
    method: 'post',
    data: data
  })
}

/**
 * 查
 * @param query
 * @returns {AxiosPromise}
 */

export function fetchCourses(query) {
  return request({
    url: API_URL('list/course'),
    method: 'get',
    params: query
  })
}

export function fetchCourseStudents(cs_id) {
  return request({
    url: API_URL('list/courseStudents'),
    method: 'get',
    params: { id: cs_id }
  })
}

export function delCourseStudent(cId, sId) {
  return request({
    url: API_URL('del/courseStudent'),
    method: 'post',
    data: {
      id: cId,
      id2: sId
    }
  })
}

export function delCourse(query) {
  return request({
    url: API_URL('del/course'),
    method: 'post',
    data: query
  })
}

export function updateCourse(data) {
  return request({
    url: API_URL('update/course'),
    method: 'post',
    data: data
  })
}

/**
 * 增
 * @param data
 * @returns {AxiosPromise}
 */
export function createCourse(data) {
  return request({
    url: API_URL('create/course'),
    method: 'post',
    data: data
  })
}
