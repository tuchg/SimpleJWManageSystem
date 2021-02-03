<template>
    <el-form :model="role.formData" :rules="rules" ref="form1">
        <el-form-item :key="key" :label="value" :prop="key" style="width: 50vh" v-for="(value,key) of role.formHeader">
            <el-form-item
                    prop="avatar"
                    v-if="key==='avatar'"
            >

                <el-upload
                        :headers="headers"
                        :on-change="handleAvatarSelected"
                        :on-success="handleAvatarUploaded"
                        :show-file-list="false"
                        action="api/common/putImg"
                        auto-upload
                        class="avatar-uploader">
                    <el-avatar :size="120" :src="avatarUrl" fit="contain" shape="circle"/>
                </el-upload>
            </el-form-item>

            <el-input v-else v-model="role.formData[key]"/>
        </el-form-item>
        <el-form-item>
            <el-button @click="submit" type="primary">保存修改</el-button>
        </el-form-item>
    </el-form>
</template>

<script>
  import { getToken } from '@/utils/auth'

  export default {
    props: {
      role: {}
    },
    computed: {
      //上传授权
      headers() {
        return { 'X-Token': getToken() }
      }
    },
    data() {
      return {
        //头像上传模块
        avatarUrl: '',
        // 表单填写验证
        rules: {
          name: [{ required: true, message: '请填写用户名' }],
          email: [{ type: 'email', message: '请输入正确的邮箱地址' }]
        }
      }
    },
    created() {
      this.avatarUrl = this.role.formData.avatar
    },
    methods: {

      submit() {
        this.$refs['form1'].validate((valid) => {
          if (valid) {
            this.role.onSubmit()
          } else {
            this.$notify({
              title: '警告',
              message: '请按提示将数据填写正确',
              type: 'warning',
              duration: 2000
            })
          }
        })
      },
      //头像预览
      handleAvatarSelected(res, file) {
        let url = null

        if (window.createObjectURL !== undefined) { // basic
          url = window.createObjectURL(file[0].raw)
        } else if (window.webkitURL !== undefined) {
          // webkit or chrome
          url = window.webkitURL.createObjectURL(file[0].raw)
        } else if (window.URL !== undefined) {
          // mozilla(firefox)
          url = window.URL.createObjectURL(file[0].raw)
        }

        this.avatarUrl = url

      },
      // 上传成功后的图片url处理
      handleAvatarUploaded(response) {
        if (response.code === 20000) {
          this.role.formData.avatar = response.url

        }
      }
    }
  }
</script>
