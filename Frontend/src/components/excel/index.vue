<template>
    <div id="excels">
        <upload-excel :col-map="colMap" :upload-api="uploadApi"/>
        <export-excel :col-map="colMap" :download-api="downloadApi" style="margin-left: 10px"/>
    </div>
</template>

<script>
  import UploadExcel from '@/components/excel/components/ImportExcelButton'
  import ExportExcel from '@/components/excel/components/ExportExcelButton'

  export default {
    name: 'ExcelGroup',
    components: { UploadExcel, ExportExcel },
    props: {
      colMap: {},
      uploadApi: Function,
      downloadApi: Function
    }
  }
</script>

<style>
    #excels {
        display: inline-flex;
    }
</style>
