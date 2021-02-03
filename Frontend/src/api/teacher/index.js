import request from '@/utils/request'

const API_URL = (e) => '/teacher/' + e

/**
 *  更新管理员
 * @param data
 */
export function updateTeacher(data) {
  return request({
    url: API_URL('update/teacher'),
    method: 'post',
    data: data
  })
}

/**
 *  修改管理员密码
 * @param data
 */
export function updateTeacherPassword(data) {
  return request({
    url: API_URL('update/teacherPassword'),
    method: 'post',
    data: data
  })
}
