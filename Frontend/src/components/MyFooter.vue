<template>
    <div id="footer">
        © 计软 2018 - 2 - 11
        <br>
        <div>
            基于
            <el-link target="_blank" type="primary" href="https://panjiachen.github.io/vue-admin-template/" underline>
                vue-element-admin
            </el-link>
            修改制作
        </div>
    </div>
</template>

<script>
  export default {}
</script>

<style lang="scss" scoped>
    #footer {
        color: rgba(0, 0, 0, 0.45);
        /*font-weight: bold;*/
        /*position: relative;*/
        text-align: center;
        width: auto;
        margin-top: 10vh;
        /*top: 25vh;*/
        & > div {
            margin-top: 15px;
            font-size: small;
        }
    }
</style>
