import Layout from '@/layout'

const roles = ['admin']
const adminRouters = [{

  path: '/admin/student',
  component: Layout,
  // redirect: 'noRedirect',
  meta: {
    icon: 'people',
    roles: roles
  },
  children: [
    {
      path: '',
      component: () => import('@/views/rolesPages/admin/StudentManager'),
      name: 'Student',
      meta: { title: '学生信息管理' }
    }
  ]
},
  {
    path: '/admin/teacher',
    component: Layout,
    // redirect: 'noRedirect',
    meta: {
      icon: 'teacher',
      roles: roles
    },
    children: [
      {
        path: '',
        component: () => import('@/views/rolesPages/admin/TeacherManager'),
        name: 'Teacher',
        meta: { title: '教师信息管理' }
      }
    ]
  },
  {
    path: '/admin/course',
    component: Layout,
    // redirect: 'noRedirect',
    meta: {
      icon: 'education',
      roles: roles
    },
    children: [
      {
        path: '',
        component: () => import('@/views/rolesPages/admin/CourseManager'),
        name: 'Course',
        meta: { title: '课程信息管理' }
      }
    ]
  },
  {
    path: '/admin/classroom',
    component: Layout,
    meta: {
      roles: roles
    }, children: [{
      path: '',
      component: () => import('@/views/rolesPages/admin/ClassroomManager'),
      name: 'Classroom',
      meta: { title: '教室管理', icon: 'tab' }
    }]
  },
  {
    path: '/admin/title',
    component: Layout,
    meta: {
      roles: roles
    }, children: [{
      path: '',
      component: () => import('@/views/rolesPages/admin/TitleManager'),
      name: 'Title',
      meta: { title: '职称管理', icon: 'skill' }
    }]
  },
  {
    path: '/admin/faculty',
    component: Layout,
    meta: {
      roles: roles
    }, children: [{
      path: '',
      component: () => import('@/views/rolesPages/admin/FacultyManager'),
      name: 'Faculty',
      meta: { title: '院系管理', icon: 'tree' }
    }]
  }
]

export default adminRouters
