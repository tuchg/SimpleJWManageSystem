const tokens = {
  admin: {
    token: 'admin-token'
  },
  student: {
    token: 'student-token'
  },
  teacher: {
    token: 'teacher-token'
  }
}

const users = {

  'teacher-token': {
    roles: ['teacher'],
    introduction: '我是个教师',
    avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
    name: '教师'
  },
  'student-token': {
    roles: ['student'],
    introduction: '我是名学生',
    avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
    name: '学生'
  },
  'admin-token': {
    roles: ['admin'],
    introduction: '我是个管理员',
    avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
    name: '管理员'
  }
}

export default [
  // user login
  {
    url: '/user/login',
    type: 'post',
    response: config => {
      const { username, role } = config.body
      const token = tokens[role]
      console.log(token, role)
      // mock error
      if (!token) {
        return {
          code: 60204,
          message: '登录失败'
        }
      }

      return {
        code: 20000,
        data: token
      }
    }
  },

  // get user info
  {
    url: '/user/info\.*',
    type: 'get',
    response: config => {
      const { token } = config.query
      const info = users['teacher-token']

      /*
            // mock error
            if (!info) {
              return {
                code: 50008,
                message: 'Login failed, unable to get user details.'
              }
            }
      */

      return {
        code: 20000,
        data: info
      }
    }
  },

  // user logout
  {
    url: '/user/logout',
    type: 'post',
    response: _ => {
      return {
        code: 20000,
        data: 'success'
      }
    }
  }
]
