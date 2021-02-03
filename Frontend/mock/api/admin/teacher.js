import Mock from 'mockjs'
import { validate } from '../common/vaildate.js'

let mockData = []
const count = 6
const API_URL = (e) => '/admin/' + e
for (let i = 0; i < count; i++) {
  mockData.push(
    Mock.mock({
      id: i,
      name: '@first',
      'sex|1': '[\'男\',\'女\']',
      age: '@integer(18, 22)',
      f_id: '@integer(1, 3)',
      t_id: '@integer(1, 3)',
      email: Mock.Random.email(),
      avatar: 'https://wpimg.wallstcn.com/e4558086-631c-425c-9430-56ffb46e70b3',
      phone: '@phone',
      password: 'cc'
    })
  )
}

export default [
  {
    url: API_URL('teacherList'),
    type: 'get',
    response: request => {
      const {
        id,
        // 搜索的内容
        content,
        //排序
        sort,
        // 院系过滤
        faculty,
        //职称过滤
        title,
        // 第几页
        page,
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
    url: API_URL('delTeacher'),
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
    url: API_URL('updateTeacher'),
    type: 'post',
    response: config => {

      const {
        id,
        name,
        sex,
        age,
        f_id,
        t_id,
        email,
        avatar,
        phone,
        password
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

      mockData.push({
        id: id,
        name: name,
        sex: sex,
        age: age,
        f_id: f_id,
        t_id: t_id,
        email: email,
        avatar: avatar,
        phone: phone,
        password: password
      })
      return {
        code: 20000,
        message: '更新成功'
      }
    }
  },
  {
    url: API_URL('createTeacher'),
    type: 'post',
    response: config => {
      const {
        id,
        name,
        sex,
        age,
        f_id,
        t_id,
        email,
        avatar,
        phone,
        password

      } = config.body
      const token = config.get('X-Token')
      // mock error
      if (!validate(token)) {
        return {
          code: 60204,
          message: 'Account and password are incorrect.'
        }
      }
      mockData.push({
        id: id,
        name: name,
        sex: sex,
        age: age,
        f_id: f_id,
        t_id: t_id,
        email: email,
        avatar: avatar,
        phone: phone,
        password: password
      })

      return {
        code: 20000,
        message: '创建成功'
      }
    }
  }
]
