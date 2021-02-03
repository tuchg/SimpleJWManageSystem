<template>
    <div id="app">
        <!--    <upload-excel/>
            <export-excel/>-->
        <br>
        <Excel/>
        <!--
        <div>
            <test-excel/>
        </div>-->
    </div>
</template>

<script>

  import Excel from '@/components/excel'
  // import TestExcel from '@/views/common/excel/test-excel'

  export default {
    name: 'Test',
    components: { Excel }
  }
</script>

<style scoped>

</style>
