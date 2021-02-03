<template>
    <div class="app-container">
        <div v-if="user">
            <el-row :gutter="20">

                <el-col :span="6" :xs="24">
                    <user-card :user="user"/>
                </el-col>

                <el-col :span="18" :xs="24">
                    <el-card>
                        <el-tabs v-model="activeTab">
                            <el-tab-pane label="修改信息" name="account">
                                <account
                                        :role.sync="rolesInfo[userInfo.roles[0]]"
                                        :user="user"
                                />
                            </el-tab-pane>
                            <el-tab-pane label="修改密码" name="password">
                                <password
                                        :role.sync="rolesInfo[userInfo.roles[0]]"
                                />
                            </el-tab-pane>
                        </el-tabs>
                    </el-card>
                </el-col>

            </el-row>
        </div>
    </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import UserCard from './components/UserCard'

  import Account from './components/Account'
  import Password from './components/Password'
  import { updateAdmin, updateAdminPassword } from '@/api/admin/index'
  import { updateStudent, updateStudentPassword } from '@/api/student'
  import { updateTeacher, updateTeacherPassword } from '@/api/teacher'

  export default {
    name: 'Profile',
    computed: {
      ...mapGetters([
        'name',
        'avatar',
        'roles',
        'userInfo'
      ])
    },
    components: { Password, UserCard, Account },
    data() {
      return {
        user: {},
        activeTab: 'account',
        // 动态角色渲染数据
        rolesInfo: {
          student: {
            that: null,
            //表单数据
            formHeader: {
              avatar: '头像',
              /*   sno: '学号',*/
              name: '姓名',
              sex: '性别',
              age: '年龄',
              jiguan: '籍贯',
              email: '电子邮箱',
              phone: '手机号码'
            },
            formData: {},
            //提交
            onSubmit() {
              updateStudent(this.formData)
                .then(res => {
                  if (res.code === 20000) {
                    this.that.showToast('success', res.message)
                    setTimeout(() => this.that.logout(), 2000)
                  }
                })
            },
            /**
             * 修改密码
             * @param _new 新
             * @param _old 旧
             */
            onSetPassword(_new, _old) {
              updateStudentPassword({
                id: this.formData.id,
                oldPassword: _old,
                newPassword: _new
              }).then(res => {
                if (res.code === 20000) {
                  this.that.showToast('success', res.message)
                  setTimeout(() => this.that.logout(), 2000)
                }
              })
            }
          },
          admin: {
            that: null,
            formHeader: {
              name: '用户名'
            },
            formData: {
              id: this.userInfo?.id,
              name: this.userInfo?.name
            },
            //提交
            onSubmit() {
              updateAdmin(this.formData)
                .then(res => {
                  if (res.code === 20000) {
                    this.that.showToast('success', res.message)
                    setTimeout(() => this.that.logout(), 2000)
                  }
                })
            },
            /**
             * 修改密码
             * @param _new 新
             * @param _old 旧
             */
            onSetPassword(_new, _old) {
              updateAdminPassword({
                id: this.formData.id,
                oldPassword: _old,
                newPassword: _new
              }).then(res => {
                if (res.code === 20000) {
                  this.that.showToast('success', res.message)
                  setTimeout(() => this.that.logout(), 2000)
                }
              })
            }
          },
          teacher: {
            that: null,
            formHeader: {
              avatar: '头像',
              name: '姓名',
              sex: '性别',
              age: '年龄',
              email: '电子邮箱',
              phone: '手机号码'
            },
            formData: {},
            //提交
            onSubmit() {
              updateTeacher(this.formData)
                .then(res => {
                  if (res.code === 20000) {
                    this.that.showToast('success', res.message)
                    setTimeout(() => this.that.logout(), 2000)
                  }
                })
            },
            /**
             * 修改密码
             * @param _new 新
             * @param _old 旧
             */
            onSetPassword(_new, _old) {
              updateTeacherPassword({
                id: this.formData.id,
                oldPassword: _old,
                newPassword: _new
              }).then(res => {
                if (res.code === 20000) {
                  this.that.showToast('success', res.message)
                  setTimeout(() => this.that.logout(), 2000)
                }
              })
            }
          }
        }
      }

    },
    created() {
      this.getUser()
    },
    methods: {
      /**
       * 提醒功能
       * @param how 提醒的方式 默认上浮
       * @param type 提醒的类型
       * @param message 消息正文
       */
      showToast(type, message, how = 1) {
        if (how === 1) {
          this.$notify({
            title: '提示',
            message: message,
            type: type,
            duration: 2000
          })
        } else {
          this.$message({
            message: message,
            type: type
          })
        }
      },
      //重设密码后要求重新登录
      async logout() {
        await this.$store.dispatch('user/logout')
        this.$router.push(`/login?redirect=${this.$route.fullPath}`)
      },
      /**
       * 返回相应的角色名
       * @param arg
       * @returns {*}
       */
      matchRoleName(arg) {
        const roleTable = {
          admin: '管理员',
          student: '学生',
          teacher: '教师'
        }
        //克隆对象 防止源数据污染
        const tmp = Object.assign([], arg)

        tmp.forEach((e, i) => {
          tmp[i] = roleTable[e]
        })
        return tmp
      },
      getUser() {
        this.user = {
          name: this.userInfo.name,
          role: this.matchRoleName(this.roles).join(' | '),
          avatar: this.userInfo.avatar
        }

        let element = this.rolesInfo[this.roles[0]]
        element.that = this
        element.formData = this.userInfo
      }
    }

  }
</script>
