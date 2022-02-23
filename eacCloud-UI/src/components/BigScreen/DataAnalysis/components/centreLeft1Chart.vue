<template>
  <div>
    <div id="centreLeft1Chart" style="width:260px; height: 220px;" />
  </div>
</template>

<script>
const echarts = require('echarts')
export default {
  data() {
    return {}
  },
  mounted() {
    this.drawPie()
  },
  destroyed() {
    window.onresize = null
  },
  methods: {
    drawPie(sidebar) {
      // 基于准备好的dom，初始化echarts实例
      if (document.getElementById('centreLeft1Chart') == null) {
        return
      }
      echarts.dispose(document.getElementById('centreLeft1Chart'))
      const myChartPieLeft = echarts.init(
        document.getElementById('centreLeft1Chart')
      )
      //  ----------------------------------------------------------------

      myChartPieLeft.setOption({
        color: [
          '#37a2da',
          '#32c5e9',
          '#9fe6b8',
          '#ffdb5c',
          '#ff9f7f',
          '#fb7293',
          '#e7bcf3',
          '#8378ea'
        ],
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        toolbox: {
          show: true
        },
        calculable: true,
        legend: {
          orient: 'horizontal',
          icon: 'circle',
          bottom: 0,
          x: 'center',
          data: ['任务1', '任务2', '任务3', '任务4', '任务5', '任务6'],
          textStyle: {
            color: '#fff'
          }
        },
        series: [
          {
            name: '增值电信业务统计表',
            type: 'pie',
            radius: [10, 60],
            roseType: 'area',
            center: ['50%', '40%'],
            data: [
              { value: 10, name: '任务1' },
              { value: 5, name: '任务2' },
              { value: 15, name: '任务3' },
              { value: 25, name: '任务4' },
              { value: 20, name: '任务5' },
              { value: 35, name: '任务6' }
            ]
          }
        ]
      })
      // -----------------------------------------------------------------
      // 响应式变化
      window.addEventListener('resize', () => myChartPieLeft.resize(), false)
      // 侧边栏变化
      if (sidebar) {
        myChartPieLeft.resize()
      }
    }
  }
}
</script>
