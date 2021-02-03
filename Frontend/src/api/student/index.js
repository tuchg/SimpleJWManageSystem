import request from '@/utils/request'

const API_URL = (e) => '/student/' + e

/**
 *  更新管理员
 * @param data
 */
export function updateStudent(data) {
  return request({
    url: API_URL('update/student'),
    method: 'post',
    data: data
  })
}

/**
 *  修改管理员密码
 * @param data
 */
export function updateStudentPassword(data) {
  return request({
    url: API_URL('update/studentPassword'),
    method: 'post',
    data: data
  })
}
