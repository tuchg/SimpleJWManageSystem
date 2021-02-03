import Mock from 'mockjs'
import { validate } from '../common/vaildate.js'

let mockData = []
const count = 6
const API_URL = (e) => '/admin/' + e
for (let i = 0; i < count; i++) {
  mockData.push(
    Mock.mock({
      id: i,
      cname: '@first',
      mark: '@float(0, 100, 2, 2)',
      t_id: '@integer(1, 30)',
      room_id: '@integer(1, 3)',
      maxChooseNum: '@integer(10, 30)',
      selectedNum: '@integer(10, 30)',
      time: ['@datetime', '@datetime']
    })
  )
}

export default [
  {
    url: API_URL('courseList'),
    type: 'get',
    response: request => {
      const {
        id,
        // 搜索的内容
        content,
        // 排序方式
        sort,
        // 第几页
        page,
        // 选课状态过滤
        selectStatus,
        // 每页限制多少
        limit
      } = request.query
      const token = request.get('X-Token')
      console.log(request.body)
      // mock error
      if (!validate(token)) {
        return {
          code: 50008,
          message: '请登录后操作'
        }
      }

      let mockList = mockData/*.filter(item => {
/!*        if (content && type) {
          if (!item.includes(content) || item.type !== type) {
            return false
          }
        }
        if (faculty && item.faculty !== faculty) return false
        *!/
        return true
      })*/

      if (sort === -1) {
        mockList = mockList.reverse()
      }

      // const pageList = mockList.filter((item, index) => index < limit * page && index >= limit * (page - 1))

      return {
        code: 20000,
        message: '查询成功',
        data: {
          total: mockList.length,
          items: mockList
        }
      }
    }
  }, {
    url: API_URL('delCourse'),
    type: 'post',
    response: config => {
      const {
        id
      } = config.body
      const token = config.get('X-Token')
      // mock error
      if (!validate(token)) {
        return {
          code: 50008,
          message: 'Account and password are incorrect.'
        }
      }
      //
      mockData.slice(id)

      return {
        code: 20000,
        message: '删除成功'
      }
    }
  },
  {
    url: API_URL('updateCourse'),
    type: 'post',
    response: config => {
      const {
        id,
        cname,
        mark,
        t_id,
        room_id,
        maxChooseNum,
        selectedNum,
        time
      } = config.body
      console.log(config.message)
      const token = config.get('X-Token')
      // mock error
      if (!validate(token)) {
        return {
          code: 50008,
          message: 'Account and password are incorrect.'
        }
      }
      mockData = mockData.slice(id)

      return {
        code: 20000,
        message: '更新成功'
      }
    }
  },
  {
    url: API_URL('createCourse'),
    type: 'post',
    response: config => {
      const {
        id,
        cname,
        mark,
        t_id,
        room_id,
        maxChooseNum,
        selectedNum,
        time
      } = config.body
      const token = config.get('X-Token')
      // mock error
      if (!validate(token)) {
        return {
          code: 60204,
          message: 'Account and password are incorrect.'
        }
      }
      return {
        code: 20000,
        message: '创建成功'
      }
    }
  }
]
