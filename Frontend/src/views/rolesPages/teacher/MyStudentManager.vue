<template
>
    <div class="app-container">
        <div class="filter-container">
            <el-input
                    @keyup.enter.native="handleFilter"
                    class="filter-item"
                    clearable
                    placeholder="搜索内容"
                    style="width: 200px;"
                    v-model="testQuery.content"
            />
            <el-select
                    class="filter-item"
                    clearable
                    placeholder="学号/学号/课程名"
                    style="width: 200px"
                    v-model="testQuery.type">
                <el-option
                        :key="item"
                        :label="item"
                        :value="index"
                        v-for="(item,index) in searchTypeOptions"
                />
            </el-select>
            <el-select
                    @change="handleFilter"
                    class="filter-item"
                    clearable
                    placeholder="院系"
                    style="width: 120px"
                    v-model="testQuery.faculty"
            >
                <el-option
                        :key="item.id"
                        :label="item.name"
                        :value="item.id"
                        v-for="item in facultyOptions"
                />
            </el-select>
            <el-button
                    @click="handleFilter"
                    class="filter-item"
                    icon="el-icon-search"
                    type="primary"
                    v-waves
            >
                搜索
            </el-button>
        </div>
        <!-- 表格-->
        <el-table
                :data="list"
                :height="tableHeight"
                :key="tableKey"
                @sort-change="sortChange"
                border
                fit
                highlight-current-row
                ref="refTable"
                style="width: 100%"
                v-loading="listLoading"
        >
            <el-table-column
                    align="center"
                    label="学号"
                    prop="sno"
                    sortable="custom"
                    width="150px"
            >
                <template slot-scope="{row}">
                    <span>{{ row.sno }}</span>
                </template>
            </el-table-column>
            <el-table-column
                    align="center"
                    label="学生姓名"
                    width="150px"
            >
                <template slot-scope="{row}">
                    <span>{{ row.name }}</span>
                </template>
            </el-table-column>
            <el-table-column
                    align="center"
                    label="选择的课程"
                    width="110px"
            >
                <template slot-scope="{row}">
                    <span>{{ row.cname }}</span>
                </template>
            </el-table-column>
            <el-table-column
                    align="center"
                    label="性别"
                    width="110px"
            >
                <template slot-scope="{row}">
                    <span>{{ row.sex }}</span>
                </template>
            </el-table-column>
            <el-table-column
                    align="center"
                    label="所在院系"
                    min-width="100px"
            >
                <template slot-scope="{row}">
                    <span>{{  matchFaculty(row.fId) }}</span>
                </template>
            </el-table-column>
            <el-table-column
                    align="center"
                    label="分数"
                    min-width="80px"
            >
                <template slot-scope="{row}">
                    <span>{{ row.score }}</span>
                </template>
            </el-table-column>
            <el-table-column
                    align="center"
                    class-name="small-padding fixed-width"
                    label="操作"
                    width="230"
            >
                <template slot-scope="{row}">
                    <el-button
                            @click="handleUpdate(row)"
                            size="mini"
                            type="primary"
                    >
                        录入
                    </el-button>
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
                :visible.sync="dialogFormVisible"
                @dragDialog="handleDrag()"
                title="成绩录入"
                v-el-drag-dialog
        >
            <el-form
                    :model="temp"
                    :rules="rules"
                    label-position="right"
                    label-width="90px"
                    ref="dataForm"
                    style="width: 400px; margin-left:50px;"
            >


                <el-form-item
                        label="成绩"
                        prop="score"
                >
                    <el-input-number
                            max="100"
                            min="0"
                            size="medium"
                            v-model="temp.score"
                    />
                </el-form-item>
            </el-form>
            <div
                    class="dialog-footer"
                    slot="footer"
            >
                <el-button @click="dialogFormVisible = false">
                    取消
                </el-button>
                <el-button
                        @click="updateData()"
                        type="primary"
                >
                    确认
                </el-button>
            </div>
        </el-dialog>
    </div>
</template>


<script>
  import waves from '@/directive/waves' // waves directive
  import Pagination from '@/components/Pagination/index'
  import elDragDialog from '@/directive/el-drag-dialog'
  import { fetchMyStudents, updateMyStudent } from '@/api/teacher/student'

  export default {
    name: 'MyStudentManager',
    components: { Pagination },
    directives: { waves, elDragDialog },
    computed: {
      //表格自适应高度
      tableHeight() {
        return window.innerHeight * 0.75
      }
    },
    data() {
      return {
        searchTypeOptions: ['姓名', '学号', '课程名'],
        sexOptions: ['男', '女'],
        facultyOptions: this.$store.getters.faculties,
        //头像上传模块
        avatarUrl: '',
        // 查询串
        testQuery: {
          // 搜索的内容
          content: '',
          // 搜索的类别
          type: '',
          // 排序方式
          sort: 1,
          // 院系过滤
          faculty: '',
          // 第几页
          page: 1,
          // 每页限制多少
          limit: 20
        },
        tableKey: 0,
        list: null,
        //总页数
        total: 0,

        listLoading: true,

        // 对话框控制
        temp: {},
        dialogFormVisible: false,
        dialogStatus: '',
        statusMap: {
          update: '编辑',
          create: '创建'
        },
        // 表单填写验证
        rules: {
          sno: [{ required: true, message: '学号是必须的', trigger: 'change' }],
          name: [{ required: true, message: '学生姓名是必须的', trigger: 'blur' }]
        }
      }
    },
    created() {
      // 初始化获取数据
      this.getList()
    },
    methods: {
      //根据ID匹配院系,该匹配数据在页面刷新且已登录时获取
      matchFaculty(id) {
        return this.facultyOptions.find(item => item.id === id)?.name
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
      // 表格数据
      getList() {
        this.listLoading = true
        // this.testQuery.token = this.$store.getters.token

        fetchMyStudents(this.testQuery).then(response => {
          if (response.code !== 20000) {
            this.showToast('error', response.message)
            this.listLoading = false
            return
          }
          this.list = response.data.items
          this.total = response.data.total

          // Just to simulate the time of the request
          setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 100)
        })
      },
      // 搜索
      handleFilter() {
        this.testQuery.page = 1
        this.getList()
      },

      //响应排序
      sortChange(data) {
        const { prop, order } = data
        if (prop === 'sno') {
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
        this.temp = {}

      },
      // 更 前置
      handleUpdate(row) {
        this.temp = Object.assign({}, row) // copy obj
        this.temp.id = row.id
        this.dialogStatus = 'update'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },
      // 更
      updateData() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            const tempData = {
              id: this.temp.id,
              csId: this.temp.csId,
              stId: this.temp.stId,
              score: this.temp.score
            }
            updateMyStudent(tempData).then(response => {
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
      // v-el-drag-dialog onDrag callback function
      handleDrag() {
        this.$refs['dataForm'].blur()
      }
    }
  }
</script>
