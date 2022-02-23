<template>
  <div class="dashboard-container">
    <div class="dashboard-editor-container">
      <div>
        eac微服务平台为响应式布局，针对不同屏幕大小设计，适配电脑、平板和手机。
      </div>
      <br>
      <div>
        eac微服务平台使用阿里巴巴Sentinel作为流量控制，阿里巴巴Nacos作为配置注册，阿里巴巴Seata作为分布式事务，阿里巴巴Druid作为数据库连接池。
      </div>
      <br>
      <div>
        <font color="blue">信息化项目,定制大屏</font>
      </div>
      <br>
      <img :src="architecturePng" class="architecturePng">
      <br>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CommonEditor',
  data() {
    return {
      architecturePng: 'https://img.cc0.cn/pixabay/2019102121144319242.jpg!860.jpg'
    }
  }
}
</script>

<style lang="scss" scoped>
  .architecturePng {
    max-width: 100%;
  }

  .dashboard-container {
    color: rgba(0, 0, 0, 0.65);
    font-size: 14px;
    min-height: 100vh;
    padding: 16px;
    text-align: center;
  }

  .dashboard-editor-container {
    background-color: white;
    padding: 20px;
    display: inline-block;
    text-align: left;
  }

  a {
    color: #1890FF;
  }

  a:hover {
    color: #40a9ff;
  }
</style>
