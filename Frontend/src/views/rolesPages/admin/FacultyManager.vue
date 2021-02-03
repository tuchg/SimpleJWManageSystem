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
        </div>
        <!-- 表格-->
        <el-table
                :data="list"
                :height="tableHeight"
                :key="tableKey"
                border
                fit
                highlight-current-row
                ref="refTable"
                style="width: 100%"
                v-loading="listLoading"
        >
            <el-table-column align="center" label="ID" min-width="70px" prop="id" sortable="custom">
                <template slot-scope="{row}">
                    <span>{{ row.id }}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="院系" min-width="150px">
                <template slot-scope="{row}">
                    <span>{{ row.name }}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="教师数" min-width="80px">
                <template slot-scope="{row}">
                    <span @click="handleTSelect(row)" class="link-type">{{ row.numTeacher }}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="学生数" min-width="80px">
                <template slot-scope="{row}">
                    <span @click="handleSSelect(row)" class="link-type">{{ row.numStudent }}</span>
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
                    label-position="right"
                    label-width="90px"
                    ref="dataForm"
                    style="width: 400px; margin-left:50px;"
            >
                <el-form-item label="ID" prop="id">
                    <el-input disabled v-model="temp.id"/>
                </el-form-item>
                <el-form-item label="院系名" prop="name">
                    <el-input clearable v-model="temp.name"/>
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
  import { createFaculty, delFaculty, fetchFaculties, updateFaculty } from '@/api/admin/faculty'

  export default {
    name: 'TitleManager',
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
        // 查询串
        testQuery: {
          // 搜索的内容
          content: '',
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
          name: ''
        },
        dialogSelectedVisible: false,
        dialogFormVisible: false,
        dialogStatus: '',
        statusMap: {
          update: '编辑',
          create: '创建'
        }
      }
    },

    created() {
      // 初始化获取数据
      this.getList()
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
      // 搜索
      handleFilter() {
        this.testQuery.page = 1
        this.getList()
      },
      // 重置弹窗内的信息
      resetTemp() {
        this.temp = {
          id: '',
          name: ''
        }
      },
      // v-el-drag-dialog onDrag callback function
      handleDrag() {
        this.$refs['dataForm'].blur()
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
      //删除前置动作
      handleDelete(row) {
        //执行请求
        this.deleteData(row)
      },

      // 增
      createData() {
        this.$refs['dataForm'].validate(valid => {
          if (valid) {
            // this.temp.token = this.testQuery.token
            createFaculty(this.temp).then(response => {
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
      // 删
      deleteData(row) {
        delFaculty(row.id).then(response => {
          if (response.code === 20000) {
            this.showToast('success', response.message, 2)
            this.list.splice(this.list.indexOf(row), 1)
          }
        })
      },
      // 更
      updateData() {
        this.$refs['dataForm'].validate(valid => {
          if (valid) {
            const tempData = Object.assign({}, this.temp)

            updateFaculty(tempData).then(response => {
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
      // 表格数据
      async getList() {
        this.listLoading = true
        fetchFaculties(this.testQuery).then(response => {
          if (response.code !== 20000) {
            this.showToast('error', response.message)
            this.listLoading = false
            return
          }

          this.list = response.data.items
          this.total = response.data.total
          this.listLoading = false
          // Just to simulate the time of the request
          setTimeout(() => {
            this.listLoading = false
          }, 2 * 100)
        })
      },
      handleSSelect(row) {
        this.handleRouteSelect('Student', row)
      },
      handleTSelect(row) {
        this.handleRouteSelect('Teacher', row)
      },
      handleRouteSelect(name, row) {
        this.$router.push(
          {
            name: name,
            params: { faculty: row.id }
          })
      }
    }
  }
</script>
