<template>
    <div class="login-container">
        <div id="particles"></div>
        <div class="title-container">
            <h3 class="title">
                <el-image
                        style="width: 1.5em; height: 1.5em; vertical-align: middle;margin-right: 10px;"
                        src="https://wpimg.wallstcn.com/69a1c46c-eb1c-4b46-8bd4-e9e686ef5251.png"
                />
                教务管理系统
            </h3>
        </div>
        <el-form
                :model="loginForm"
                :rules="loginRules"
                autocomplete="on"
                class="login-form"
                label-position="left"
                ref="loginForm"
        >
            <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user"/>
        </span>
                <el-input
                        autocomplete="on"
                        name="username"
                        placeholder="用户名"
                        ref="username"
                        tabindex="1"
                        type="text"
                        v-model="loginForm.username"
                />
            </el-form-item>

            <el-tooltip content="大写字母键已打开" manual placement="right" v-model="capsTooltip">
                <el-form-item prop="password">
          <span class="svg-container">
            <svg-icon icon-class="password"/>
          </span>
                    <el-input
                            :key="passwordType"
                            :type="passwordType"
                            @blur="capsTooltip = false"
                            @keyup.enter.native="handleLogin"
                            @keyup.native="checkCapslock"
                            autocomplete="on"
                            name="password"
                            placeholder="密码"
                            ref="password"
                            tabindex="2"
                            v-model="loginForm.password"
                    />
                    <span @click="showPwd" class="show-pwd">
            <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'"/>
          </span>
                </el-form-item>
            </el-tooltip>
            <el-form-item align="left">
        <span class="svg-container">
          <svg-icon icon-class="example"/>
        </span>
                <el-radio-group style="padding: 12px 5px 12px 15px;" v-model="loginForm.role">
                    <el-radio label="student">学生</el-radio>
                    <el-radio label="teacher">教师</el-radio>
                    <el-radio label="admin">管理员</el-radio>
                </el-radio-group>
            </el-form-item>
            <!--todo 可考虑添加验证码-->
            <div class="text-center">
                <el-button
                        :loading="loading"
                        @click.native.prevent="handleLogin"
                        class="pan-btn blue-btn"
                        style="width:50%;"
                        type="primary"
                >登录
                </el-button>
            </div>
        </el-form>
        <my-footer/>
    </div>
</template>

<script>
  import Mallki from '@/components/TextHoverEffect/Mallki'
  import MyFooter from '@/components/MyFooter'
  import { log } from 'util'
  import particles from 'particles.js'

  export default {
    name: 'Login',
    components: { MyFooter, Mallki },
    data() {
      /*    const validateUsername = (rule, value, callback) => {
              if (!validUsername(value)) {
                callback(new Error('Please enter the correct user name'))
              } else {
                callback()
              }
            }*/
      const validatePassword = (rule, value, callback) => {
        if (value.length < 3) {
          callback(new Error('密码至少要3位'))
        } else {
          callback()
        }
      }
      return {
        loginForm: {
          username: 'admin',
          password: '111111',
          role: 'student'
        },
        loginRules: {
          username: [
            { required: true, trigger: 'blur', message: '请输入用户名' }
          ],
          password: [
            { required: true, trigger: 'blur', validator: validatePassword }
          ]
        },
        passwordType: 'password',
        capsTooltip: false,
        loading: false,
        showDialog: false,
        redirect: undefined,
        otherQuery: {}
      }
    },
    watch: {
      $route: {
        handler: function(route) {
          const query = route.query
          if (query) {
            this.redirect = query.redirect
            this.otherQuery = this.getOtherQuery(query)
          }
        },
        immediate: true
      }
    },
    created() {
      // window.addEventListener('storage', this.afterQRScan)
    },
    mounted() {
      if (this.loginForm.username === '') {
        this.$refs.username.focus()
      } else if (this.loginForm.password === '') {
        this.$refs.password.focus()
      }
      // .onmouseover()

      //todo 最终需去除
      // particles.toString()
      //挂载粒子特效模块
      particlesJS.load('particles', 'particlesjs-config.json')
    },
    destroyed() {
      // window.removeEventListener('storage', this.afterQRScan)
    },
    methods: {
      checkCapslock({ shiftKey, key } = {}) {
        if (key && key.length === 1) {
          if (
            (shiftKey && (key >= 'a' && key <= 'z')) ||
            (!shiftKey && (key >= 'A' && key <= 'Z'))
          ) {
            this.capsTooltip = true
          } else {
            this.capsTooltip = false
          }
        }
        if (key === 'CapsLock' && this.capsTooltip === true) {
          this.capsTooltip = false
        }
      },
      showPwd() {
        if (this.passwordType === 'password') {
          this.passwordType = ''
        } else {
          this.passwordType = 'password'
        }
        this.$nextTick(() => {
          this.$refs.password.focus()
        })
      },
      handleLogin() {
        this.$refs.loginForm.validate(valid => {
          if (valid) {
            this.loading = true
            this.$store
              .dispatch('user/login', this.loginForm)
              .then(() => {
                this.$router.push({
                  path: this.redirect || '/',
                  queryPage: this.otherQuery
                })
                this.loading = false
              })
              .catch(e => {
                console.log(e)
                this.loading = false
              })
          } else {
            console.log('登录失败!')
            return false
          }
        })
      },
      getOtherQuery(query) {
        return Object.keys(query).reduce((acc, cur) => {
          if (cur !== 'redirect') {
            acc[cur] = query[cur]
          }
          return acc
        }, {})
      }
    }
  }
</script>

<style lang="scss">
    /* 修复input 背景不协调 和光标变色 */
    /* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

    $bg: #283443;
    $light_gray: #fff;
    $cursor: #fff;

    @supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
        .login-container .el-input input {
            color: $cursor;
        }
    }

    /* reset element-ui css */
    .login-container {
        .el-input {
            display: inline-block;
            height: 47px;
            width: 85%;

            input {
                background: transparent;
                border: 0px;
                -webkit-appearance: none;
                border-radius: 0px;
                padding: 12px 5px 12px 15px;
                color: $light_gray;
                height: 47px;
                caret-color: $cursor;

                &:-webkit-autofill {
                    box-shadow: 0 0 0px 1000px $bg inset !important;
                    -webkit-text-fill-color: $cursor !important;
                }
            }
        }

        .el-form-item {
            border: 1px solid rgba(255, 255, 255, 0.1);
            background: rgba(0, 0, 0, 0.1);
            border-radius: 5px;
            color: #454545;
        }
    }
</style>

<style lang="scss" scoped>
    $bg: #2d3a4b;
    $dark_gray: #889aa4;
    $light_gray: #eee;
    //粒子效果
    #particles {
        position: absolute;
        width: 100%;
        height: 95%;
    }

    #footer {
        color: $light_gray;
        position: relative;
        top: 25vh;
    }

    .login-container {
        min-height: 100%;
        width: 100%;
        background-color: $bg;
        overflow: auto;

        .login-form {
            position: relative;
            background-color: $bg;
            top: 25vh;
            width: 520px;
            max-width: 100%;
            padding: 30px 30px;
            margin: 0 auto;
            overflow: hidden;
        }

        .tips {
            font-size: 14px;
            color: #fff;
            margin-bottom: 10px;

            span {
                &:first-of-type {
                    margin-right: 16px;
                }
            }
        }

        .svg-container {
            padding: 6px 5px 6px 15px;
            color: $dark_gray;
            vertical-align: middle;
            width: 30px;
            display: inline-block;
        }

        .title-container {
            position: relative;
            top: 25vh;

            .title {
                font-size: 28px;
                color: $light_gray;
                margin: 0px auto 20px auto;
                text-align: center;
                font-family: 华文新魏, sans-serif;
                font-weight: bold;
            }
        }

        .show-pwd {
            position: absolute;
            right: 10px;
            top: 7px;
            font-size: 16px;
            color: $dark_gray;
            cursor: pointer;
            user-select: none;
        }

        .thirdparty-button {
            position: absolute;
            right: 0;
            bottom: 6px;
        }

        @media only screen and (max-width: 470px) {
            .thirdparty-button {
                display: none;
            }
        }
    }

    .el-radio {
        color: $light_gray;
    }
</style>
