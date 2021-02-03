<template>
    <div class="upload-excel-container">
        <input @change="handleClick"
               accept=".xlsx, .xls"
               hidden
               ref="excel-upload-input"
               type="file"
        >
        <el-button
                :loading="fileLoading"
                @click="handleImport"
                icon="el-icon-circle-plus"
                type="primary"
                v-waves
        >
            导入
        </el-button>
        <el-dialog :visible.sync="dialogVisible" title="待上传信息(默认全选)">
            <el-table
                    :data="tableData"
                    @selection-change="handleSelectionChange"
                    border
                    fit
                    highlight-current-row
                    v-loading="tableLoading">
                <el-table-column align="center" type="selection"/>
                <el-table-column :key="value" :label="key" :prop="value" align="center"
                                 v-for="(value,key) of colMap"/>
            </el-table>
            <!-- 分页组件-->
            <pagination
                    :limit.sync="queryPage.limit"
                    :page.sync="queryPage.page"
                    :total="queryPage.total"
                    @pagination="getNext"
                    v-show="queryPage.total>1"
            />
            <div class="dialog-footer" slot="footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button @click="handleUpload"
                           type="primary"
                           v-loading.fullscreen.lock="uploading"
                >
                    确认
                </el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
  import XLSX from 'xlsx'
  import Pagination from '@/components/Pagination/index'
  import waves from '@/directive/waves'

  export default {
    name: 'ImportExcelButton',
    components: { Pagination },
    directives: { waves },
    props: {
      uploadApi: {
        type: Function,
        default() {
          return function(x) {
            console.log('传入', x)
            return new Promise(resolve => {
              resolve({ code: 20000 })
            })
          }
        }
      },
      colMap: {
        default() {
          return {
            头像: 'avatar',
            学号: 'sno'
          }
        }
      }
    },
    data() {
      return {
        tableData: [],
        excelData: {
          header: null,
          results: null
        },
        //已选对象表
        multipleSelection: null,
        //页查询
        queryPage: {
          //总数
          total: 0,
          //页尺寸
          limit: 20,
          //当前页
          page: 0
        },
        //文件选择中
        fileLoading: false,
        //对话框可见性
        dialogVisible: false,
        //表格加载中
        tableLoading: false,
        //上传中
        uploading: false
      }
    },
    methods: {
      handleImport() {
        this.fileLoading = true
        this.$refs['excel-upload-input'].click()
      },
      generateData({ header, results }) {
        this.excelData.header = header
        this.excelData.results = results
        this.onSuccess && this.onSuccess(this.excelData)
      },
      handleClick(e) {
        if (this.dialogVisible) return//防止重复调用
        const files = e.target.files
        const rawFile = files[0] // only use files[0]
        if (!rawFile) return
        if (!this.isExcel(rawFile)) {
          this.$message.error('仅支持 .xlsx, .xls, .csv 等格式文件')
          this.fileLoading = false
          return false
        }
        this.dialogVisible = true
        this.upload(rawFile)
        this.fileLoading = false
      },
      upload(rawFile) {
        this.$refs['excel-upload-input'].value = null // fix can't select the same excel
        if (!this.beforeUpload) {
          this.readData(rawFile)
          return
        }
        const before = this.beforeUpload(rawFile)
        if (before) {
          this.readData(rawFile)
          this.dialogVisible = false
        }
      },
      //解析数据
      readData(rawFile) {
        this.tableLoading = true
        return new Promise((resolve, reject) => {
          const reader = new FileReader()
          reader.onload = e => {
            const data = e.target.result
            const workbook = XLSX.read(data, { type: 'array' })
            const firstSheetName = workbook.SheetNames[0]
            const worksheet = workbook.Sheets[firstSheetName]
            const header = this.getHeaderRow(worksheet)
            const results = XLSX.utils.sheet_to_json(worksheet)
            this.generateData({ header, results })
            //
            this.queryPage.total = this.excelData.results.length  // Math.round((this.excelData.results.length / this.queryPage.limit) * 10 / 10)
            this.queryPage.page = 0
            //进行列名转换
            this.colMapper()
            //
            resolve()
            this.tableLoading = false
          }
          reader.readAsArrayBuffer(rawFile)
        })
      },
      //解析表头
      getHeaderRow(sheet) {
        const headers = []
        const range = XLSX.utils.decode_range(sheet['!ref'])
        let C
        const R = range.s.r
        /* start in the first row */
        for (C = range.s.c; C <= range.e.c; ++C) { /* walk every column in the range */
          const cell = sheet[XLSX.utils.encode_cell({ c: C, r: R })]
          /* find the cell in the first row */
          let hdr = '未知 ' + C // <-- replace with your desired default
          if (cell && cell.t) hdr = XLSX.utils.format_cell(cell)
          headers.push(hdr)
        }
        return headers
      },
      //验证表格数据文件
      isExcel(file) {
        return /\.(xlsx|xls|csv)$/.test(file.name)
      },
      //响应选择项的改变
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      //翻页
      getNext() {
        const query = this.queryPage
        query.total = this.excelData.length
        //起始索引
        const start = page * query.limit
        this.tableData = this.excelData.results.slice(start, start + query.limit)
        //翻页
        ++query.page
      },
      //处理上传
      handleUpload() {
        this.uploading = true
        //有选则上传选择的,否则全部上传
        this.uploadApi(this.multipleSelection ? this.multipleSelection : this.tableData).then((res, rej) => {
          if (res.code === 20000) {
            this.$notify({
              title: '成功',
              message: res.message,
              type: 'success'
            })
            //关闭对话框
            this.dialogVisible = false
          } else {
            this.$notify.error({
              title: '失败',
              message: res.message
            })
          }
          this.uploading = false
        })
      },
      /**
       * 列名替换映射处理
       */
      colMapper() {
        this.tableData = this.excelData.results.map(item => {
          //todo 破坏了源数据
          for (const key in item) {
            const newKey = this.colMap[key]
            if (newKey) {
              //添加属性
              item[newKey] = item[key]
              //删除原属性,完成替换
              delete item[key]
            }
          }
          return item
        })
      }
    }
  }
</script>
