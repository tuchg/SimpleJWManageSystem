const getters = {
  sidebar: state => state.app.sidebar,
  size: state => state.app.size,
  device: state => state.app.device,
  token: state => state.user.token,
  // todo!!! 冗余信息严重
  userInfo: state => state.user.userInfo,
  avatar: state => state.user.userInfo.avatar,
  name: state => state.user.name,
  roles: state => state.user.roles,
  permission_routes: state => state.permission.routes,
  errorLogs: state => state.errorLog.logs,
  faculties: state => state.user.faculties,
  titles: state => state.user.titles
}
export default getters
