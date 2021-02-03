import request from '@/utils/request'

const API_URL = (e) => '/student/' + e

/**
 * 获取我的可选课
 * @param {*} query
 */
export function fetchMySelect(query) {
  return request({
    url: API_URL('list/course'),
    method: 'get',
    params: query
  })
}

/**
 * 获取我的已选课
 * @param {*} query
 */
export function fetchMySelected(query) {
  return request({
    url: API_URL('list/myCourse'),
    method: 'get',
    params: query
  })
}

/**
 * 获取我的成绩单
 * @param {*} query
 */
export function fetchMyScores(query) {
  return request({
    url: API_URL('list/myScore'),
    method: 'get',
    params: query
  })
}

export function fetchMyClass(cs_id) {
  return request({
    url: API_URL('list/classRank'),
    method: 'get',
    params: { id: cs_id }
  })
}

/**
 * 删除我的选课
 * @param {*} query
 */
export function delMySelect(query) {
  return request({
    url: API_URL('del/elective'),
    method: 'post',
    data: query
  })
}

/**
 * 更新我的课程
 * @param {*} data
 */
export function updateMySelect(data) {
  return request({
    url: API_URL('update/elective'),
    method: 'post',
    data: data
  })
}

/**
 * 添加我的选课
 * @param {*} data
 */
export function addMySelect(data) {
  return request({
    url: API_URL('create/elective'),
    method: 'post',
    data: data
  })
}
