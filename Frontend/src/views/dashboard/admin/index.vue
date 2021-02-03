<template>
    <!--
todo

    管理员端

学生
教师
学院

饼状图 学生
    各学院人数

雷达图
    :职称 (教师)
教授
副教授
讲师
助教

雷达图
    :学院
学生数
教师数
课程数


登录日志面板 谁谁登录了谁谁退出了 具体操作忽略

    -->
    <div class="dashboard-editor-container">
        <github-corner class="github-corner"/>
        <!-- todo 数目变化-->
        <panel-group :data="headerData" @handleSetChartData="handleSetChartData"/>

        <el-row :gutter="32">
            <el-col :lg="12" :sm="24" :xs="24">
                <div class="chart-wrapper">
                    <transaction-table/>
                </div>
            </el-col>
            <el-col :lg="12" :sm="24" :xs="24">
                <div class="chart-wrapper">
                    <component :chartData="testData" v-bind:is="currentChart"/>
                </div>
            </el-col>
        </el-row>

        <MyFooter/>
    </div>

</template>

<script>
  import GithubCorner from '@/components/GithubCorner'
  import PanelGroup from './components/PanelGroup'
  import RaddarChart from './components/RaddarChart'
  import PieChart from './components/PieChart'
  import TransactionTable from './components/TransactionTable'

  import BoxCard from './components/BoxCard'
  import MyFooter from '@/components/MyFooter'
  import Hamburger from '@/components/Hamburger/index'
  import { fetchHomeData } from '@/api/admin'

  const lineChartData = {
    newVisitis: {
      expectedData: [100, 120, 161, 134, 105, 160, 165],
      actualData: [120, 82, 91, 154, 162, 140, 145]
    },
    messages: {
      expectedData: [200, 192, 120, 144, 160, 130, 140],
      actualData: [180, 160, 151, 106, 145, 150, 130]
    },
    purchases: {
      expectedData: [80, 100, 121, 104, 105, 90, 100],
      actualData: [120, 90, 100, 138, 142, 130, 130]
    },
    shoppings: {
      expectedData: [130, 140, 141, 142, 145, 150, 160],
      actualData: [120, 82, 91, 154, 162, 140, 130]
    }
  }

  export default {
    name: 'DashboardAdmin',
    components: {
      Hamburger,
      MyFooter,
      GithubCorner,
      PanelGroup,
      RaddarChart,
      PieChart,
      TransactionTable,
      BoxCard
    },
    data() {
      return {
        headerData: {
          studentNum: 100,
          teacherNum: 10000,
          facultyNum: 2000
        },
        lineChartData: lineChartData.newVisitis,
        currentChart: 'pie-chart',
        testData: [
          { value: 320, name: '计算机系' },
          { value: 240, name: '数学系' },
          { value: 149, name: '人文系' },
          { value: 100, name: '哲学系' },
          { value: 59, name: '商业系' }
        ]
      }
    },
    created() {
      // 初始化获取数据
      this.getList()
    },
    methods: {
      handleSetChartData(type) {
        switch (type) {
          case 'student':
            this.currentChart = 'pie-chart'
            break
          case 'teacher':
            this.currentChart = 'raddar-chart'
            break
          case 'faculty':
            this.currentChart = 'pie-chart'
            break
        }
        this.getList()

      },
      getList() {
        fetchHomeData().then(response => {
          if (response.code === 20000) {
            this.headerData.teacherNum = response.data.teacherNum
            this.headerData.studentNum = response.data.studentNum
            this.headerData.facultyNum = response.data.facultyNum
            this.testData = response.data.data
          }
        })
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


    @media (max-width: 1024px) {
        .chart-wrapper {
            padding: 8px;
        }
    }
</style>
