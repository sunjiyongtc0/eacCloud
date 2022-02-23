<template>
  <div>
    <div id="centreLeft2Chart" style="width:430px; height: 370px" />
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
      if (document.getElementById('centreLeft2Chart') == null) {
        return
      }
      echarts.dispose(document.getElementById('centreLeft2Chart'))
      const myChartPieLeft = echarts.init(
        document.getElementById('centreLeft2Chart')
      )
      //  ----------------------------------------------------------------

      const option = {
        angleAxis: {
          interval: 1,
          type: 'category',
          data: [
            '数据流通1',
            '数据流通2',
            '数据流通3',
            '数据流通4',
            '数据流通5',
            '数据流通6',
            '数据流通7',
            '数据流通8',
            '数据流通9',
            '数据流通10 '
          ],
          z: 10,
          axisLine: {
            show: true,
            lineStyle: {
              color: '#00c7ff',
              width: 1,
              type: 'solid'
            }
          },
          axisLabel: {
            interval: 0,
            show: true,
            color: '#00c7ff',
            margin: 8,
            fontSize: 12
          }
        },
        radiusAxis: {
          min: 0,
          max: 100,
          interval: 20,
          axisLine: {
            show: true,
            lineStyle: {
              color: '#00c7ff',
              width: 1,
              type: 'solid'
            }
          },
          axisLabel: {
            formatter: '{value} %',
            show: false,
            padding: [0, 0, 20, 0],
            color: '#00c7ff',
            fontSize: 16
          },
          splitLine: {
            lineStyle: {
              color: '#00c7ff',
              width: 1,
              type: 'solid'
            }
          }
        },
        legend: {
          show: true,
          orient: 'vartical',
          top: 'center',
          bottom: '0%',
          itemWidth: 16,
          itemHeight: 8,
          itemGap: 16,
          textStyle: {
            color: '#A3E2F4',
            fontSize: 12,
            fontWeight: 0
          },
          data: ['A']
        },
        polar: {},
        series: [
          {
            name: 'A',
            type: 'bar',
            radius: ['20%', '100%'],
            data: [
              {
                value: 87,
                itemStyle: {
                  color: '#f54d4d'
                }
              },
              {
                value: 80,
                itemStyle: {
                  color: '#f87544'
                }
              },
              {
                value: 75,
                itemStyle: {
                  color: '#ffae00'
                }
              },
              {
                value: 69,
                itemStyle: {
                  color: '#dcff00'
                }
              },
              {
                value: 63,
                itemStyle: {
                  color: '#25d053'
                }
              },
              {
                value: 54,
                itemStyle: {
                  color: '#01fff5'
                }
              },
              {
                value: 47,
                itemStyle: {
                  color: '#007cff'
                }
              },
              {
                value: 40,
                itemStyle: {
                  color: '#4245ff'
                }
              },
              {
                value: 35,
                itemStyle: {
                  color: '#c32eff'
                }
              },
              {
                value: 33,
                itemStyle: {
                  color: '#ff62e8'
                }
              }
            ],
            coordinateSystem: 'polar'
          }
        ]
      }
      myChartPieLeft.setOption(option)
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
