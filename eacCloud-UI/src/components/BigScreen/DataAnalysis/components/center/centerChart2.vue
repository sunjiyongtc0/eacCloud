<template>
  <div>
    <div id="centerChart2" style="width:120px;height:100px;" />
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
    drawPie() {
      // 基于准备好的dom，初始化echarts实例
      if (document.getElementById('centerChart2') == null) {
        return
      }
      echarts.dispose(document.getElementById('centerChart2'))
      const myChartPieLeft = echarts.init(
        document.getElementById('centerChart2')
      )
      //  ----------------------------------------------------------------
      const tips = 40
      const option = {
        title: [
          {
            text: tips * 1 + '%',
            x: 'center',
            y: 'center',
            textStyle: {
              color: '#67e0e3',
              fontSize: 16
            }
          }
        ],
        series: [
          {
            type: 'pie',
            radius: ['75%', '80%'],
            center: ['50%', '50%'],
            emphasis: { scale: false },
            color: ['#f3ffb070', 'transparent'],
            label: {
              show: false
            },
            data: [
              {
                value: tips,
                itemStyle: {
                  color: '#ff9800',
                  shadowBlur: 10,
                  shadowColor: '#97e2f5'
                }
              },
              {
                value: 100 - tips
              }
            ]
          }
        ]
      }
      myChartPieLeft.setOption(option)
      // -----------------------------------------------------------------
      // 响应式变化
      window.addEventListener('resize', () => myChartPieLeft.resize(), false)
    }
  }
}
</script>
