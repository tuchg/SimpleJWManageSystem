import request from '@/utils/request'

/**
 * 获取院系数据
 * @returns {AxiosPromise}
 */
export function getFaculties() {
  return request({
    url: '/common/facultyList',
    method: 'get'
  })
}

/**
 * 获取教室
 * @returns {AxiosPromise}
 */
export function getClassRooms() {
  return request({
    url: '/common/classRoomList',
    method: 'get'
  })
}

/**
 * 获取职称数据
 * @returns {AxiosPromise}
 */
export function getTitles() {
  return request({
    url: '/common/titleList',
    method: 'get'
  })
}

/**
 * 获取职称数据
 * @returns {AxiosPromise}
 */
export function getTeachers() {
  return request({
    url: '/common/teacherList',
    method: 'get'
  })
}

/**
 * 上传图片
 * @returns {AxiosPromise}
 */
export function putAvatar() {

  return request({
    url: '/common/putImg',
    method: 'post'
  })
}
