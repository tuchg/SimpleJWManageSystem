import Layout from '@/layout/index'

const roles = ['student']
const studentRouters = [
  // 学生
  {
    path: '/student/course',
    component: Layout,
    // redirect: 'noRedirect',
    meta: {
      title: '课程服务',
      icon: 'component',
      roles: roles
    },
    children: [
      {
        path: 'select',
        component: () => import('@/views/rolesPages/student/SelectManager'),
        name: 'SelectCourse',
        meta: { title: '选课', icon: 'form' }
      },
      {
        path: 'selected',
        component: () => import('@/views/rolesPages/student/SelectedManager'),
        name: 'SelectedCourse',
        meta: { title: '已选课程', icon: 'list' }
      }
    ]
  },
  {
    path: '/student/score',
    component: Layout,
    // redirect: 'noRedirect',
    meta: {
      roles: roles
    },
    children: [
      {
        path: '',
        component: () => import('@/views/rolesPages/student/ScorePanel'),
        name: 'MyScore',
        meta: { title: '我的成绩单', icon: 'tab' }
      }
    ]
  },
]
export default studentRouters
