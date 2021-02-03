import Mock from 'mockjs'
import { validate } from './common/vaildate.js'

const mockData = []
const count = 6
const API_URL = (e) => '/common/' + e
for (let i = 0; i < count; i++) {
  mockData.push(
    Mock.mock({
      id: i,
      name: '@first'
    })
  )
}
//院系
const faculties = new Map([
  [1, '信息工程系'],
  [2, '人文艺术系'],
  [3, '现代医学系']
])

function _strMapToObj(strMap) {
  let obj = Object.create(null)
  for (let [k, v] of strMap) {
    obj[k] = v
  }
  return obj
}

export default [
  {
    url: API_URL('facultyList'),
    type: 'get',
    response: config => {
      const token = config.get('X-Token')
      // mock error
   /*   if (!validate(token)) {
        return {
          code: 60204,
          message: 'Account and password are incorrect.'
        }
      }*/
      return {
        code: 20000,
        data: [
          { id: 1, name: '信息工程系' },
          { id: 2, name: '人文艺术系' },
          { id: 3, name: '现代医学系' }
        ]
      }
    }
  }, {
    url: API_URL('classRoomList'),
    type: 'get',
    response: config => {
      const token = config.get('X-Token')
      // mock error
      /* if (!validate(token)) {
         return {
           code: 60204,
           message: 'Account and password are incorrect.'
         }
       }*/
      return {
        code: 20000,
        data: [
          { id: 1, name: '2教114' },
          { id: 2, name: '2教116' },
          { id: 3, name: '2教120' }
        ]
      }
    }
  },
  {
    url: API_URL('titleList'),
    type: 'get',
    response: config => {
      const token = config.get('X-Token')
      // mock error
      /* if (!validate(token)) {
         return {
           code: 60204,
           message: 'Account and password are incorrect.'
         }
       }*/
      return {
        code: 20000,
        data: [
          { id: 1, name: '教授' },
          { id: 2, name: '副教授' },
          { id: 3, name: '讲师' }
        ]
      }
    }
  },
  {
    url: API_URL('putAvatar'),
    type: 'post',
    response: config => {
      const token = config.get('X-Token')
      return {
        code: 20000,
        message: '成功',
        url: 'https://cn.bing.com/sa/simg/SharedSpriteDesktop_2x_090619.png'

      }
    }
  }
]
