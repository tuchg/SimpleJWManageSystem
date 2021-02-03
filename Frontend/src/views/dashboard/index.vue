<template>
    <div class="dashboard-container">
        <component :is="currentRole"/>
    </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import adminDashboard from './admin'
  import teacherDashboard from './teacher'
  import studentDashboard from './student'

  export default {
    name: 'Dashboard',
    components: { adminDashboard, studentDashboard, teacherDashboard },
    data() {
      return {
        currentRole: 'adminDashboard'
      }
    },
    computed: {
      ...mapGetters([
        'roles'
      ])
    },
    created() {
      if (this.roles.includes('admin')) {
        this.currentRole = 'adminDashboard'
      } else if (this.roles.includes('student')) {
        this.currentRole = 'studentDashboard'
      } else if (this.roles.includes('teacher')) {
        this.currentRole = 'teacherDashboard'
      }
    }
  }
</script>
