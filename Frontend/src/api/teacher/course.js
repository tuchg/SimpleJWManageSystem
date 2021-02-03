import request from '@/utils/request'

const API_URL = (e) => '/teacher/' + e

/**
 * 获取我的课程
 * @param {*} query
 */
export function fetchMyCourses(query) {
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

/**
 * 删除我的课
 * @param {*} query
 */
export function delMyCourse(query) {
  return request({
    url: API_URL('del/course'),
    method: 'post',
    data: query
  })
}

/**
 * 更新我的课程
 * @param {*} data
 */
export function updateMyCourse(data) {
  return request({
    url: API_URL('update/course'),
    method: 'post',
    data: data
  })
}

/**
 * 添加我的课程
 * @param {*} data
 */
export function addMyCourse(data) {
  return request({
    url: API_URL('create/course'),
    method: 'post',
    data: data
  })
}
