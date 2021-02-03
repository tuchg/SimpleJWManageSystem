import Layout from '@/layout/index'

const roles = ['teacher']
const teacherRouters = [
  // 教师
  {
    path: '/teacher/course',
    component: Layout,
    // redirect: 'noRedirect',
    meta: {
      title: '我的课程',
      icon: 'component',
      roles: roles
    },
    children: [
      {
        path: '',
        component: () => import('@/views/rolesPages/teacher/CourseManager'),
        name: 'ViewCourse',
        meta: { title: '我的课程', icon: 'list' }
      }
    ]
  },
  {
    path: '/teacher/students',
    component: Layout,
    // redirect: 'noRedirect',
    meta: {
      roles: roles
    },
    children: [
      {
        path: '',
        component: () => import('@/views/rolesPages/teacher/MyStudentManager'),
        name: 'MyStudent',
        meta: { title: '成绩录入', icon: 'skill' }
      }
    ]
  }
]
export default teacherRouters
