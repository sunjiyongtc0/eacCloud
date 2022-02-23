<template>
  <div id="index" style="min-width: 2250px; min-height: 1300px">
    <!-- <dv-full-screen-container class="bg"> -->
    <div class="bg">
      <dv-loading v-if="loading">Loading...</dv-loading>
      <div v-else class="host-body">
        <div class="d-flex jc-center">
          <dv-decoration-10 style="width:33.3%;height:5px;" />
          <div class="d-flex jc-center">
            <dv-decoration-8 :color="['#568aea', '#000000']" style="width:200px;height:50px;" />
            <div class="title">
              <span class="title-text">大数据可视化平台</span>
              <dv-decoration-6
                class="title-bototm"
                :reverse="true"
                :color="['#50e3c2', '#67a1e5']"
                style="width:250px;height:8px;"
              />
            </div>
            <dv-decoration-8
              :reverse="true"
              :color="['#568aea', '#000000']"
              style="width:200px;height:50px;"
            />
          </div>
          <dv-decoration-10 style="width:33.3%;height:5px;" />
        </div>

        <!-- 第二行 -->
        <div class="d-flex jc-between px-2">
          <div class="d-flex" style="width: 40%">
            <div
              class="react-right ml-4"
              style="width: 500px; text-align: left;background-color: #0f1325;"
            >
              <span class="react-before" />
              <span class="text fw-b">数据分析1</span>
            </div>
            <div class="react-right ml-3" style="background-color: #0f1325;">
              <span class="text colorBlue fw-b">数据分析2</span>
            </div>
          </div>
          <div style="width: 40%" class="d-flex">
            <div class="react-left bg-color-blue mr-3">
              <span class="text fw-b">数据分析3</span>
            </div>
            <div
              class="react-left mr-4"
              style="width: 500px; background-color: #0f1325; text-align: right;"
            >
              <span class="react-after" />
              <span class="text">{{ new Date() | formatDate }}</span>
            </div>
          </div>
        </div>

        <div class="body-box">
          <!-- 第三行数据 -->
          <div class="content-box">
            <div>
              <dv-border-box-12>
                <centreLeft1 />
              </dv-border-box-12>
            </div>
            <div>
              <dv-border-box-12>
                <centreLeft2 />
              </dv-border-box-12>
            </div>
            <!-- 中间 -->
            <div>
              <center />
            </div>
            <!-- 中间 -->
            <div>
              <centreRight2 />
            </div>
            <div>
              <dv-border-box-13>
                <centreRight1 />
              </dv-border-box-13>
            </div>
          </div>

          <!-- 第四行数据 -->
          <div class="bototm-box">
            <dv-border-box-13>
              <bottomLeft />
            </dv-border-box-13>
            <dv-border-box-12>
              <bottomRight />
            </dv-border-box-12>
          </div>
        </div>
      </div>
    <!-- </dv-full-screen-container> -->
    </div>
  </div>
</template>

<script>
import centreLeft1 from './centreLeft1'
import centreLeft2 from './centreLeft2'
import centreRight1 from './centreRight1'
import centreRight2 from './centreRight2'
import center from './center'
import bottomLeft from './bottomLeft'
import bottomRight from './bottomRight'
export default {
  components: {
    centreLeft1,
    centreLeft2,
    centreRight1,
    centreRight2,
    center,
    bottomLeft,
    bottomRight
  },
  data() {
    return {
      loading: true
    }
  },
  mounted() {
    this.cancelLoading()
  },
  methods: {
    cancelLoading() {
      setTimeout(() => {
        this.loading = false
      }, 2000)
    }
  }
}
</script>

<style lang="scss" scoped>
@import "./styles/index.scss";
@import "./styles/style.scss";
</style>
