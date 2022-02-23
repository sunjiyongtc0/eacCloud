<template>
  <div class="dashboard-container">
    <component :is="currentRole" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import commonDashboard from './common'
import adminDashboard from './admin'
import editorDashboard from './editor'

export default {
  name: 'Home',
  components: { commonDashboard, adminDashboard, editorDashboard },
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
    this.currentRole = 'commonDashboard'
  }
}
</script>
