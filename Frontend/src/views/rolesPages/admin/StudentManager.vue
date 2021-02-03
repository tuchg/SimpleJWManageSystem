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
                    placeholder="学号/学号"
                    style="width: 150px"
                    v-model="testQuery.type"
            >
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
                @row-click="clickExpandTable"
                @sort-change="sortChange"
                border
                fit
                highlight-current-row
                ref="refTable"
                style="width: 100%"
                v-loading="listLoading"
        >
            <el-table-column label type="expand">
                <template slot-scope="props">
                    <el-form class="my-table-expand" inline label-position="left">
                        <el-form-item label="头像">
                            <!--todo 图片地址拼接 -->

                            <el-image :src="props.row.avatar" lazy style="width: 100px; height: 100px"/>
                        </el-form-item>
                        <el-form-item label>
                            <span/>
                        </el-form-item>
                        <el-form-item label="ID">
                            <span>{{props.row.id}}</span>
                        </el-form-item>
                        <el-form-item label="电子邮件">
                            <span>{{props.row.email}}</span>
                        </el-form-item>
                        <el-form-item label="学生年龄">
                            <span>{{props.row.age}}</span>
                        </el-form-item>
                        <el-form-item label="累积学分">
                            <span>{{props.row.mark}}</span>
                        </el-form-item>
                        <el-form-item label="最大选课数">
                            <span>{{props.row.maxChooseNum}}</span>
                        </el-form-item>
                    </el-form>
                </template>
            </el-table-column>
            <el-table-column align="center" label="学号" prop="sno" sortable="custom" width="150px">
                <template slot-scope="{row}">
                    <span>{{ row.sno }}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="学生姓名" width="150px">
                <template slot-scope="{row}">
                    <span>{{ row.name }}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="性别" width="110px">
                <template slot-scope="{row}">
                    <span>{{ row.sex }}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="所在院系" min-width="100px">
                <template slot-scope="{row}">
                    <span>{{ matchFaculty(row.fId) }}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="手机号码" min-width="90px">
                <template slot-scope="{row}">
                    <span>{{ row.phone }}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="籍贯" min-width="80px">
                <template slot-scope="{row}">
                    <span>{{ row.jiguan }}</span>
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
                    label-width="90px"
                    ref="dataForm"
                    style="width: 400px; margin-left:50px;"
            >
                <el-form-item label="头像" prop="avatar">
                    <el-upload
                            :headers="headers"
                            :on-change="handleAvatarSelected"
                            :on-success="handleAvatarUploaded"
                            :show-file-list="false"
                            action="api/common/putImg"
                            auto-upload
                            class="avatar-uploader"
                    >
                        <el-avatar :size="120" :src="avatarUrl" shape="square"/>
                    </el-upload>
                </el-form-item>
                <el-form-item label="学号" prop="sno">
                    <el-input v-model="temp.sno"/>
                </el-form-item>

                <el-form-item label="姓名" prop="name">
                    <el-input clearable v-model="temp.name"/>
                </el-form-item>

                <el-form-item label="密码" prop="pwd">
                    <el-input clearable ref="password" type="password" v-model="temp.password"/>
                </el-form-item>
                <el-form-item label="性别" prop="sex">
                    <el-select class="filter-item" clearable placeholder="请选择" v-model="temp.sex">
                        <el-option :key="item" :label="item" :value="item" v-for="(item) in sexOptions"/>
                    </el-select>
                </el-form-item>
                <el-form-item label="年龄" prop="age">
                    <el-input-number
                            max="100"
                            min="0"
                            size="small" v-model="temp.age"/>
                </el-form-item>
                <el-form-item label="所在院系" prop="faculty">
                    <!--                    可通过computed 计算-->
                    <el-select class="filter-item" clearable placeholder="Please select" v-model="temp.fId">
                        <el-option
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"
                                v-for="item in facultyOptions"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="籍贯" prop="jiguan">
                    <el-input v-model="temp.jiguan"/>
                </el-form-item>
                <el-form-item label="手机号码" prop="phone">
                    <el-input type="phone" v-model="temp.phone"/>
                </el-form-item>
                <el-form-item label="电子邮箱" prop="email">
                    <el-input type="email" v-model="temp.email"/>
                </el-form-item>

                <el-form-item label="最大选课数" prop="maxChooseNum">
                    <el-input-number
                            max="100"
                            min="0"
                            size="small" v-model="temp.maxChooseNum"/>
                </el-form-item>
            </el-form>
            <div class="dialog-footer" slot="footer">
                <el-button @click="dialogFormVisible = false">取消</el-button>
                <el-button @click="dialogStatus==='create'?createData():updateData()" type="primary">确认</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
  import waves from '@/directive/waves' // waves directive
  import Pagination from '@/components/Pagination/index'
  import elDragDialog from '@/directive/el-drag-dialog'
  import ExcelGroup from '@/components/excel'
  import {
    createStudent,
    delStudent,
    exportStudents,
    fetchStudents,
    importStudents,
    updateStudent
  } from '@/api/admin/student'
  import { getToken } from '@/utils/auth'

  export default {
    name: 'StudentManager',
    components: { Pagination, ExcelGroup },
    directives: { waves, elDragDialog },
    filters: {
      /*  typeFilter(type) {
            return calendarTypeKeyValue[type]
          }*/
    },
    computed: {
      //表格自适应高度
      tableHeight() {
        return window.innerHeight * 0.75
      },
      //上传授权
      headers() {
        return { 'X-Token': getToken() }
      }

    },
    data() {
      return {
        searchTypeOptions: ['姓名', '学号'],
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
        temp: {
          id: '',
          name: '',
          sno: '',
          sex: '',
          age: '',
          fId: '',
          jiguan: '',
          email: '',
          avatar: '',
          phone: '',
          password: '',
          maxChooseNum: ''
        },
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
        },
        colMap: {
          ID: 'id',
          学号: 'sno',
          姓名: 'name',
          性别: 'sex',
          年龄: 'age',
          院系ID: 'fId',
          籍贯: 'jiguan',
          电子邮件: 'email',
          手机号码: 'phone',
          最大可选课数: 'maxChooseNum'
        }
      }
    },
    created() {
      // 初始化获取数据
      this.getList()
      //获取路由传参
      this.testQuery.faculty = this.$route.params.faculty
    },
    methods: {
      /**
       *导出
       */
      exportAPI() {
        return exportStudents()
      },
      importAPI(data) {
        return importStudents(data)
      },

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
      //头像预览
      handleAvatarSelected(res, file) {
        let url = null
        if (window.createObjectURL !== undefined) {
          // basic
          url = window.createObjectURL(file[0].raw)
        } else if (window.webkitURL !== undefined) {
          // webkit or chrome
          url = window.webkitURL.createObjectURL(file[0].raw)
        } else if (window.URL !== undefined) {
          // mozilla(firefox)
          url = window.URL.createObjectURL(file[0].raw)
        }

        this.avatarUrl = url

        console.log(this.avatarUrl)
      },
      // 上传成功后的图片url处理
      handleAvatarUploaded(response) {
        if (response.code === 20000) {
          this.temp.avatar = response.url
        }
      },
      // 表格数据
      getList() {
        this.listLoading = true
        // this.testQuery.token = this.$store.getters.token

        fetchStudents(this.testQuery).then(response => {
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
        this.temp = {
          id: '',
          name: '',
          sno: '',
          sex: '',
          age: '',
          fId: '',
          jiguan: '',
          email: '',
          avatar: '',
          phone: '',
          password: '',
          maxChooseNum: ''
        }
        this.avatarUrl = null
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
            // this.temp.token = this.testQuery.token
            createStudent(this.temp).then(response => {
              if (response.code === 20000) {
                this.dialogFormVisible = false
                this.showToast('success', response.message)
                // 从列表插入一个新的
                this.list.unshift(this.temp)
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
        this.clickExpandTable(row)

        this.temp = Object.assign({}, row) // copy obj
        this.temp.id = row.id
        this.avatarUrl = this.temp.avatar
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

            updateStudent(tempData).then(response => {
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
        this.clickExpandTable(row)
        //执行请求
        this.deleteData(row)
      },
      // 删
      deleteData(row) {
        delStudent({ id: row.id }).then(response => {
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
