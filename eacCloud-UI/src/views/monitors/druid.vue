<template>
  <div v-loading="loading" class="container">
    <iframe ref="iframe" :src="src" scrolling="yes" frameborder="no" border="0" height="100%" width="100%" />
  </div>
</template>

<script>
export default {
  name: 'Druid',
  data() {
    return {
      loading: true,
      src: 'http://127.0.0.1:10003/druid'
    }
  },
  mounted() {
    const { iframe } = this.$refs
    // IE和非IE浏览器，监听iframe加载事件不一样，需要兼容
    const that = this
    if (iframe.attachEvent) {
      // IE
      iframe.attachEvent('onload', () => {
        that.stateChange()
      })
    } else {
      // 非IE
      iframe.onload = function() {
        that.stateChange()
      }
    }
  },
  methods: {
    stateChange() {
      this.loading = false
    }
  }
}
</script>
<style scoped>
.container {
  position: relative;
  width: 100%;
  height: calc(100vh - 84px);
  padding: 5px;
}
</style>
