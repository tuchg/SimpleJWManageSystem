<template>
    <el-table :data="list" style="width: 100%;padding-top: 15px;">
        <el-table-column label="用户名" min-width="200">
            <template slot-scope="scope">
                {{ scope.row.order_no | orderNoFilter }}
            </template>
        </el-table-column>
        <el-table-column align="center" label="身份类型" width="195">
            <template slot-scope="scope">
                ¥{{ scope.row.price | toThousandFilter }}
            </template>
        </el-table-column>
        <el-table-column align="center" label="状态" width="100">
            <template slot-scope="{row}">
                <el-tag :type="row.status | statusFilter">
                    {{ row.status }}
                </el-tag>
            </template>
        </el-table-column>
    </el-table>
</template>

<script>

  export default {
    props: {
      data: {}
    },
    filters: {
      statusFilter(status) {
        const statusMap = {
          success: 'success',
          pending: 'danger'
        }
        return statusMap[status]
      },
      orderNoFilter(str) {
        return str.substring(0, 30)
      }
    },
    data() {
      return {
        list: null
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      fetchData() {

      }
    }
  }
</script>
