<template>
    <div class="navbar">
        <hamburger
                :is-active="sidebar.opened"
                @toggleClick="toggleSideBar"
                class="hamburger-container"
                id="hamburger-container"
        />

        <breadcrumb class="breadcrumb-container" id="breadcrumb-container"/>

        <div class="right-menu">
            <template v-if="device!=='mobile'">
                <search class="right-menu-item" id="header-search"/>

                <el-tooltip content="Bugs" effect="dark" placement="bottom">
                    <error-log class="errLog-container right-menu-item hover-effect"/>
                </el-tooltip>

                <el-tooltip content="全局组件尺寸" effect="dark" placement="bottom">
                    <size-select class="right-menu-item hover-effect" id="size-select"/>
                </el-tooltip>

                <el-tooltip content="全屏" effect="dark" placement="bottom">
                    <screenfull class="right-menu-item hover-effect" id="screenfull"/>
                </el-tooltip>
            </template>

            <el-tooltip content="主题色" effect="dark" placement="bottom">
                <theme-picker @change="themeChange" class="right-menu-item hover-effect"/>
            </el-tooltip>

            <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
                <div class="avatar-wrapper">

                    <img :src="avatar" class="user-avatar"/>
                    <i class="el-icon-caret-bottom"/>
                </div>
                <el-dropdown-menu slot="dropdown">
                    <router-link to="/">
                        <el-dropdown-item>首页</el-dropdown-item>
                    </router-link>
                    <router-link to="/profile/index">
                        <el-dropdown-item>个人信息</el-dropdown-item>
                    </router-link>
                    <el-dropdown-item divided>
                        <span @click="logout" style="display:block;">安全退出</span>
                    </el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>
    </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import Breadcrumb from '@/components/Breadcrumb'
  import Hamburger from '@/components/Hamburger'
  import ErrorLog from '@/components/ErrorLog'
  import Screenfull from '@/components/Screenfull'
  import Search from '@/components/HeaderSearch'
  import ThemePicker from '@/components/ThemePicker'
  import SizeSelect from '@/components/SizeSelect'

  export default {
    components: {
      Breadcrumb,
      Hamburger,
      ErrorLog,
      Screenfull,
      Search,
      ThemePicker,
      SizeSelect
    },
    computed: {
      ...mapGetters(['sidebar', 'avatar', 'device'])
    },
    methods: {
      themeChange(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'theme',
          value: val
        })
      },
      toggleSideBar() {
        this.$store.dispatch('app/toggleSideBar')
      },
      async logout() {
        await this.$store.dispatch('user/logout')
        // this.$router.push(`/login?redirect=${this.$route.fullPath}`)
        this.$router.push(`/login`)
      }
    }
  }
</script>

<style lang="scss" scoped>
    .navbar {
        height: 50px;
        overflow: hidden;
        position: relative;
        background: #fff;
        box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

        .hamburger-container {
            line-height: 46px;
            height: 100%;
            float: left;
            cursor: pointer;
            transition: background 0.3s;
            -webkit-tap-highlight-color: transparent;

            &:hover {
                background: rgba(0, 0, 0, 0.025);
            }
        }

        .breadcrumb-container {
            float: left;
        }

        .errLog-container {
            display: inline-block;
            vertical-align: top;
        }

        .right-menu {
            float: right;
            height: 100%;
            line-height: 50px;

            &:focus {
                outline: none;
            }

            .right-menu-item {
                display: inline-block;
                padding: 0 8px;
                height: 100%;
                font-size: 18px;
                color: #5a5e66;
                vertical-align: text-bottom;

                &.hover-effect {
                    cursor: pointer;
                    transition: background 0.3s;

                    &:hover {
                        background: rgba(0, 0, 0, 0.025);
                    }
                }
            }

            .avatar-container {
                margin-right: 30px;

                .avatar-wrapper {
                    margin-top: 5px;
                    position: relative;

                    .user-avatar {
                        cursor: pointer;
                        width: 40px;
                        height: 40px;
                        border-radius: 10px;
                    }

                    .el-icon-caret-bottom {
                        cursor: pointer;
                        position: absolute;
                        right: -20px;
                        top: 25px;
                        font-size: 12px;
                    }
                }
            }
        }

        .el-icon-caret-bottom {
            display: none;
        }
    }
</style>
<style>
    /*主题选择强制居中*/
    .el-color-picker__color {
        /* chrome不兼容 */
        /* display: inline flow-root list-item !important; */
        display: list-item !important;
    }
</style>
