<template
>
    <div class="app-container">
        <div class="filter-container">
            <el-input
                    @keyup.enter.native="handleFilter"
                    class="filter-item"
                    placeholder="搜索内容"
                    style="width: 200px;"
                    v-model="testQuery.content"
            />
            <el-select
                    @change="handleFilter"
                    class="filter-item"
                    clearable
                    placeholder="选课状态"
                    style="width: 120px"
                    v-model="testQuery.status"
            >
                <el-option :key="item" :label="item" :value="item" v-for="item in chooseStatus"/>
            </el-select>
            <el-button
                    @click="handleFilter"
                    class="filter-item"
                    icon="el-icon-search"
                    type="primary"
                    v-waves
            >搜索
            </el-button>
            <el-button
                    @click="handleCreate"
                    class="filter-item"
                    icon="el-icon-edit"
                    style="margin-left: 10px;"
                    type="primary"
                    v-waves
            >添加
            </el-button>
            <div style="float: right">
                <excel-group :col-map="colMap" :download-api="exportAPI" :upload-api="importAPI"/>

            </div>
        </div>
        <!-- 表格-->
        <el-table
                :data="list"
                :height="tableHeight"
                :key="tableKey"
                :row-key="Math.round(Math.random()*10000).toString()"
                @row-click="clickExpandTable"
                @sort-change="sortChange"
                border
                fit
                highlight-current-row
                ref="refTable"
                style="width: 100%"
                v-loading="listLoading"
        >
            <el-table-column align="center" label="ID" prop="id" sortable="custom" width="70px">
                <template slot-scope="{row}">
                    <span>{{ row.id }}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="课程名" width="150px">
                <template slot-scope="{row}">
                    <span>{{ row.cname }}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="学分" width="110px">
                <template slot-scope="{row}">
                    <span>{{ row.mark }}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="授课老师" min-width="100px">
                <template slot-scope="{row}">
                    <span>{{matchTeacher(row.tId)}}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="授课地点" min-width="90px">
                <template slot-scope="{row}">
                    <span>{{matchClassRoom(row.roomId)}}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="开课时间" min-width="180px">
                <template slot-scope="{row}">
                    <span>{{ row.startTime}}-{{row.endTime}}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="选课情况" min-width="60px">
                <template slot-scope="{row}">
                    <span @click="handleFetchStudents(row.id)" class="link-type">{{ row.selectedNum }}</span>
                    <span>/{{row.maxChooseNum}}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" class-name="small-padding fixed-width" label="操作" width="230">
                <template slot-scope="{row}">
                    <el-button @click="handleUpdate(row)" size="mini" type="primary">编辑</el-button>
                    <el-button @click="handleDelete(row)" size="mini" type="danger">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 分页组件-->
        <pagination
                :limit.sync="testQuery.limit"
                :page.sync="testQuery.page"
                :total="total"
                @pagination="getList"
                v-show="total>0"
        />
        <!--对话框-->
        <el-dialog
                :title="statusMap[dialogStatus]"
                :visible.sync="dialogFormVisible"
                @dragDialog="handleDrag()"
                v-el-drag-dialog
        >
            <el-form
                    :model="temp"
                    :rules="rules"
                    label-position="right"
                    label-width="100px"
                    ref="dataForm"
                    style="width: 400px; margin-left:50px;"
            >
                <el-form-item label="ID" prop="id">
                    <el-input disabled v-model="temp.id"/>
                </el-form-item>

                <el-form-item label="课程名" prop="cname">
                    <el-input clearable v-model="temp.cname"/>
                </el-form-item>

                <el-form-item label="学分" prop="mark">
                    <el-input clearable v-model="temp.mark"/>
                </el-form-item>
                <el-form-item label="授课教师" prop="sex">
                    <el-select class="filter-item" clearable placeholder="请选择" v-model="temp.tId">
                        <el-option
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"
                                v-for="(item) in teacherOptions"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="授课地点" prop="address">
                    <el-select class="filter-item" clearable placeholder="请选择" v-model="temp.roomId">
                        <el-option
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"
                                v-for="(item) in classRoomOptions"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="课程时间" prop="time">
                    <el-date-picker
                            align="right"
                            end-placeholder="结束日期"
                            format="yyyy-MM-dd HH:mm"
                            range-separator="至"
                            start-placeholder="开始日期"
                            type="datetimerange"
                            v-model="temp.time"
                            value-format="yyyy-MM-dd HH:mm"/>
                </el-form-item>
                <el-form-item label="最大选课人数" prop="maxChooseNum">
                    <el-input type="number" v-model="temp.maxChooseNum"/>
                </el-form-item>
            </el-form>
            <div class="dialog-footer" slot="footer">
                <el-button @click="dialogFormVisible = false">取消</el-button>
                <el-button @click="dialogStatus==='create'?createData():updateData()" type="primary">确认</el-button>
            </div>
        </el-dialog>
        <el-dialog
                :visible.sync="dialogSelectedVisible"
                @dragDialog="handleDrag()"
                title="已选课学生信息汇总"
                v-el-drag-dialog
        >
            <el-table
                    :data="studentTableData"
                    border
                    highlight-current-row
                    stripe
                    style="width: 100%;margin-top:20px;"
            >
                <el-table-column
                        :key="item.key"
                        :label="item.value"
                        :prop="item.key"
                        v-for="item of studentTableHeader"
                />
                <el-table-column
                        align="center"
                        class-name="small-padding fixed-width"
                        label="操作"
                        min-width="60px"
                >
                    <template slot-scope="{row}">
                        <el-button @click="handleStudentDelete(row)" size="mini" type="danger">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <span class="dialog-footer" slot="footer">
        <el-button @click="dialogSelectedVisible = false" type="primary">确认</el-button>
      </span>
        </el-dialog>
    </div>
</template>

<script>
  import waves from '@/directive/waves' // waves directive
  import Pagination from '@/components/Pagination/index'
  import elDragDialog from '@/directive/el-drag-dialog'
  import {
    createCourse,
    delCourse,
    delCourseStudent, exportCourses,
    fetchCourses,
    fetchCourseStudents, importCourses,
    updateCourse
  } from '@/api/admin/course'
  import { getClassRooms, getTeachers } from '@/api/common'
  import ExcelGroup from '@/components/excel/index'

  export default {
    name: 'CourseManager',
    components: { ExcelGroup, Pagination },
    directives: { waves, elDragDialog, ExcelGroup },
    filters: {
      /*  typeFilter(type) {
            return calendarTypeKeyValue[type]
          }*/
    },
    computed: {
      //表格自适应高度
      tableHeight() {
        return window.innerHeight * 0.75
      }
    },

    data() {
      return {
        studentTableData: [],
        studentTableHeader: [
          {
            key: 'sno',
            value: '学号'
          },
          {
            key: 'name',
            value: '姓名'
          },
          {
            key: 'sex',
            value: '性别'
          },
          {
            key: 'fId',
            value: '所在院系'
          }
        ],
        facultyOptions: this.$store.getters.faculties,
        teacherOptions: [],
        classRoomOptions: [],
        chooseStatus: ['已满', '未满'],
        sexOptions: ['男', '女'],
        // 查询串
        testQuery: {
          // 搜索的内容
          content: '',
          // 排序方式
          sort: 1,
          status: '',
          // 第几页
          page: 1,
          // 每页限制多少
          limit: 20
        },
        tableKey: 0,
        list: [],
        //总页数
        total: 0,

        listLoading: true,

        // 对话框控制
        temp: {
          id: '',
          cname: '',
          mark: '',
          tId: '',
          roomId: '',
          maxChooseNum: '',
          selectedNum: '',
          startTime: '',
          endTime: '',
          time: []
        },
        dialogSelectedVisible: false,
        dialogFormVisible: false,
        dialogStatus: '',
        statusMap: {
          update: '编辑',
          create: '创建'
        },
        colMap: {
          ID: 'id',
          课程名: 'cname',
          成绩: 'mark',
          教师ID: 'tId',
          教室ID: 'roomId',
          最大选课人数: 'maxChooseNum',
          已选人数: 'selectedNum',
          开课时间: 'startTime',
          结课时间: 'endTime'
        },
        // 表单填写验证
        rules: {
          name: [{ required: true, message: '姓名是必须的', trigger: 'blur' }]
        }
      }
    },

    created() {
      // 初始化获取数据
      this.getList()
    },
    methods: {
      /**
       *导出
       */
      exportAPI() {
        return exportCourses()
      },
      importAPI(data) {
        return importCourses(data)
      },

      //根据ID匹配院系,该匹配数据在页面刷新且已登录时获取
      matchFaculty(id) {
        return this.facultyOptions.find(item => item.id === id)?.name
      },
      matchTeacher(t_id) {
        return this.teacherOptions.find(item => item.id === t_id)?.name
      },
      matchClassRoom(room_id) {
        return this.classRoomOptions.find(item => item.id === room_id)?.name
      },
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
            status: type,
            duration: 2000
          })
        } else {
          this.$message({
            message: message,
            status: type
          })
        }
      },
      // 显示学生信息
      handleFetchStudents(id) {
        this.dialogSelectedVisible = true
        // todo 接收选本课学生 需独立接口
        fetchCourseStudents(id).then(response => {
          if (response.code === 20000) {
            const data = response.data.items

            //数据匹配清洗
            this.studentTableData = data.map(item => {
              item.fId = this.matchFaculty(item.fId)
              return item
            })
          }
        })
      },
      // 删除学生
      handleStudentDelete(row) {
        delCourseStudent(row.cId, row.sId).then(res => {
          if (res.code === 20000) {
            this.showToast('success', res.message)
            this.studentTableData.splice(this.studentTableData.indexOf(row), 1)
          }
        })
      },

      // 表格数据
      async getList() {
        this.listLoading = true
        //教室集
        const classrooms = await getClassRooms()
        this.classRoomOptions = classrooms.data
        //老师集
        const teacherRes = await getTeachers()
        this.teacherOptions = teacherRes.data

        fetchCourses(this.testQuery).then(response => {
          if (response.code !== 20000) {
            this.showToast('error', response.message)
            this.listLoading = false
            return
          }

          this.list = response.data.items
          this.list.time = [this.list.startTime, this.list.endTime]
          this.total = response.data.total
          this.listLoading = false
          // Just to simulate the time of the request
          setTimeout(() => {
            this.listLoading = false
          }, 2 * 100)
        })
      },
      // 搜索        debugger

      handleFilter() {
        this.testQuery.page = 1
        this.getList()
      },

      //响应排序
      sortChange(data) {
        const { prop, order } = data
        if (prop === 'id') {
          this.sortByID(order)
        }
      },
      sortByID(order) {
        if (order === 'ascending') {
          this.testQuery.sort = '-1'
        } else {
          this.testQuery.sort = '1'
        }
        this.handleFilter()
      },

      // 重置弹窗内的消息
      resetTemp() {
        this.temp = {
          id: '',
          cname: '',
          mark: '',
          tId: '',
          roomId: '',
          maxChooseNum: '',
          selectedNum: '',
          startTime: '',
          endTime: '',
          time: []
        }
      },
      // 监听创建事件
      handleCreate() {
        this.resetTemp()

        this.dialogStatus = 'create'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },
      // 增
      createData() {
        this.$refs['dataForm'].validate(valid => {
          if (valid) {
            this.temp.startTime = this.temp.time[0]
            this.temp.endTime = this.temp.time[1]
            // this.temp.token = this.testQuery.token
            createCourse(this.temp).then(response => {
              if (response.code === 20000) {
                this.dialogFormVisible = false
                this.showToast('success', response.message)
                // 从列表插入一个新的
                // this.list.unshift(this.temp);
                this.handleFilter()
              }
            })
          } else {
            this.showToast('warning', '请将数据填写正确')
          }
        })
      },
      // 更 前置
      handleUpdate(row) {
        //避免点击展开
        // this.clickExpandTable(row);

        this.temp = Object.assign({}, row) // copy obj
        this.temp.id = row.id
        this.temp.time = [row.startTime, row.endTime]
        this.dialogStatus = 'update'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },
      // 更
      updateData() {
        this.$refs['dataForm'].validate(valid => {
          if (valid) {
            const tempData = Object.assign({}, this.temp)
            tempData.startTime = this.temp.time[0]
            tempData.endTime = this.temp.time[1]
            updateCourse(tempData).then(response => {
              if (response.code === 20000) {
                for (const i of this.list) {
                  if (i.id === this.temp.id) {
                    const index = this.list.indexOf(i)
                    this.list.splice(index, 1, this.temp)
                    break
                  }
                }
                this.dialogFormVisible = false
                this.showToast('success', response.message)
              }
            })
          } else {
            this.showToast('warning', '请将数据填写正确')
          }
        })
      },
      //删除前置动作
      handleDelete(row) {
        //避免点击展开
        // this.clickExpandTable(row);
        //执行请求
        this.deleteData(row)
      },
      // 删
      deleteData(row) {
        delCourse({ id: row.id }).then(response => {
          if (response.code === 20000) {
            this.showToast('success', response.message, 2)
            this.list.splice(this.list.indexOf(row), 1)
          }
        })
      },
      // v-el-drag-dialog onDrag callback function
      handleDrag() {
        this.$refs['dataForm'].blur()
      },
      // 表格展开
      clickExpandTable(row) {
        // 调用,table的方法,展开/折叠 行
        this.$refs.refTable.toggleRowExpansion(row)
      }
    }
  }
</script>
