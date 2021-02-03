import request from '@/utils/request'

const API_URL = (e) => '/teacher/' + e

/**
 * 获取我的课程
 * @param {*} query
 */
export function fetchMyStudents(query) {
  return request({
    url: API_URL('list/student'),
    method: 'get',
    params: query
  })
}

/**
 * 删除我的课
 * @param {*} query
 */
export function delMyStudent(query) {
  return request({
    url: API_URL('del/myStudent'),
    method: 'post',
    data: query
  })
}

/**
 * 更新我的学生成绩
 * @param {*} data
 */
export function updateMyStudent(data) {
  return request({
    url: API_URL('update/myStudent'),
    method: 'post',
    data: data
  })
}

/**
 * 添加我的课程
 * @param {*} data
 */
export function addMyStudent(data) {
  return request({
    url: API_URL('create/myStudent'),
    method: 'post',
    data: data
  })
}
