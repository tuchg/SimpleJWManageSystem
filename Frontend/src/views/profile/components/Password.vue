<template>
    <el-form :model="form" :rules="rules" ref="form2">
        <el-form-item label="旧密码" prop="oldPassword" style="width: 50vh">
            <el-input clearable show-password type="password" v-model="form.oldPassword"/>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword" style="width: 50vh">
            <el-input clearable show-password type="password" v-model="form.newPassword"/>
        </el-form-item>
        <el-form-item>
            <el-button @click="submit" type="primary">保存修改</el-button>
        </el-form-item>
    </el-form>
</template>

<script>

  export default {
    props: {
      role: {}
    },
    data() {
      return {
        form: {
          oldPassword: ' ',
          newPassword: ''
        },
        rules: {
          oldPassword: [{ required: true, message: '请填写旧密码' }],
          newPassword: [{ required: true, message: '新密码不能为空' }]
        }
      }
    },
    methods: {
      submit() {
        this.$refs['form2'].validate((valid) => {
          if (valid) {
            this.role.onSetPassword(this.form.newPassword, this.form.oldPassword)
          } else {
            this.$notify({
              title: '提示',
              message: '请按提示将数据填写正确',
              type: 'warning',
              duration: 2000
            })
          }
        })

      }
    }
  }
</script>
