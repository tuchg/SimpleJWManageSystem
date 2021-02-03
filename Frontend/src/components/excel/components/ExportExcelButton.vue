<template>
    <div class="download-excel-container">
        <el-button :loading="exportLoading" @click="handleExport" icon="el-icon-document"
                   type="primary">
            导出
        </el-button>
    </div>
</template>

<script>

  export default {
    name: 'ExportExcelButton',
    props: {
      /**
       * 下载API
       */
      downloadApi: {
        type: Function,
        default() {
          return function() {
            console.log('传入')
            return new Promise(resolve => {
              resolve({
                code: 20000,
                data: {
                  items: [
                    {
                      name: '测试',
                      value: 'dsd'
                    }, {
                      name: '测试1',
                      value: 'dsdggfas'
                    }, {
                      name: '测试2',
                      value: 'ds323vf2321vfd1d'
                    }
                  ],
                  total: 3
                }
              })
            })
          }
        }
      },
      /**
       * 列名映射
       */
      colMap: {
        default() {
          return {
            姓名: 'name',
            不是道: 'value'
          }
        }
      }
    },
    data() {
      return {
        list: null,
        total: null,
        dataHeader: [],
        dataContent: '',
        exportLoading: false,
        filename: '',
        bookType: 'xlsx'
      }
    },
    methods: {
      handleExport() {
        this.exportLoading = true
        //调用远程数据
        this.downloadApi()
          .then(response => {
              if (response.code === 20000) {
                //初始化表格数据
                this.dataHeader = []
                //数据映射
                this.dataContent = response.data.items.map(item => {
                  //todo 破坏了源数据
                  for (const key in item) {

                    //找到对应的列名
                    let newKey = undefined
                    const colMap = this.colMap
                    for (const _key in colMap) {
                      if (colMap[_key] === key) {
                        //提升变量
                        newKey = _key
                        break
                      }
                    }

                    if (newKey) {
                      //添加属性
                      item[newKey] = item[key]
                      //删除原属性,完成替换
                      delete item[key]
                    }
                  }
                  return item
                })

                //提取头部
                let map = this.colMap

                for (const key in map) {
                  this.dataHeader.push(key)
                }

                this.$notify.info({
                  title: '成功',
                  message: '后端数据下载成功,开始准备导出...请稍后'
                })
                this.total = response.data.total
                //文件名以当前日期命名
                this.filename = new Date().toLocaleString()

                //数据导出
                import('@/vendor/Export2Excel').then(excel => {
                  const data = this.formatJson(this.dataHeader, this.dataContent)

                  excel.export_json_to_excel({
                    header: this.dataHeader,
                    data,
                    filename: this.filename,
                    autoWidth: true,
                    bookType: this.bookType
                  })
                  //关闭按钮载入状态
                  this.exportLoading = false
                  //通知
                  this.$notify({
                    title: '成功',
                    message: 'Excel文件已准备完成,开始下载',
                    type: 'success'
                  })
                })
              } else {
                this.$notify.error({
                  title: '失败',
                  message: res.message
                })
                this.exportLoading = false
              }
            }
          )
      },
      formatJson(headers, content) {
        //压平JSON将JSON转化为无头组
        return content.map(item =>
          headers.map(key => item[key])
        )
      }
    }
  }
</script>
