import path from "path"
<template>
    <!--
todo
成绩等级分布图
学分变化图

    -->
    <div class="dashboard-editor-container">
        <github-corner class="github-corner"/>
        <div class="chart-wrapper" style="background-color: transparent;font-size: 24px">
            <mallki text="欢迎使用教务管理系统"/>
        </div>


        <user-card :no-alert="true" :no-header="true" :user="user" style="width: 400px;margin:0 auto"/>

        <MyFooter/>

    </div>

</template>

<script>
  import GithubCorner from '@/components/GithubCorner'

  import MyFooter from '@/components/MyFooter'

  import Mallki from '@/components/TextHoverEffect/Mallki'
  import { mapGetters } from 'vuex'
  import UserCard from '@/views/profile/components/UserCard'

  export default {
    name: 'DashboardStudent',
    components: {
      Mallki,
      MyFooter,
      GithubCorner,
      UserCard
    },
    data() {
      return {
        user: {}
      }
    },
    computed: {
      ...mapGetters(['userInfo', 'avatar', 'roles'])
    },
    created() {
      this.getUser()
    },
    methods: {
      /* 返回相应的角色名
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
      }
    }
  }
</script>

<style lang="scss" scoped>
    .dashboard-editor-container {

        padding: 32px;
        background-color: rgb(240, 242, 245);
        position: relative;

        .github-corner {
            position: absolute;
            top: 0px;
            border: 0;
            right: 0;
        }

        .chart-wrapper {
            background: #fff;
            padding: 16px 16px 0;
            margin-bottom: 32px;
        }
    }

    #footer {
        margin-top: 20vh !important;
    }

    @media (max-width: 1024px) {
        .chart-wrapper {
            padding: 8px;
        }
    }
</style>
