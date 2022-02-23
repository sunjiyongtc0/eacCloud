<template>
  <div id="center">
    <div class="up">
      <div v-for="item in titleItem" :key="item.title" class="bg-color-black item">
        <p class="ml-3 colorBlue fw-b">{{ item.title }}</p>
        <div>
          <dv-digital-flop :config="item.number" style="width:100px;height:50px;" />
        </div>
      </div>
    </div>
    <div class="down" style="margin-top: 40px">
      <div class="ranking bg-color-black">
        <span class="fs-xl text mx-2 mb-1">年度负责人组件达标榜</span>
        <dv-scroll-ranking-board :config="ranking" style="height:220px" />
      </div>
      <div class="percent">
        <div class="item  bg-color-black">
          <span>今日任务通过率</span>
          <centerChart1 />
        </div>
        <div class="item  bg-color-black">
          <span>今日任务达标率</span>
          <centerChart2 />
        </div>
        <div class="water">
          <dv-water-level-pond :config="water" style="height: 120px" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import centerChart1 from './components/center/centerChart1'
import centerChart2 from './components/center/centerChart2'

export default {
  components: {
    centerChart1,
    centerChart2
  },
  data() {
    return {
      titleItem: [
        {
          title: '今年累计任务建次数',
          number: {
            number: [120],
            toFixed: 1,
            content: '{nt}'
          }
        },
        {
          title: '本月累计任务次数',
          number: {
            number: [18],
            toFixed: 1,
            content: '{nt}'
          }
        },
        {
          title: '今日累计任务次数',
          number: {
            number: [2],
            toFixed: 1,
            content: '{nt}'
          }
        },
        {
          title: '今年失败任务次数',
          number: {
            number: [14],
            toFixed: 1,
            content: '{nt}'
          }
        },
        {
          title: '今年成功任务次数',
          number: {
            number: [106],
            toFixed: 1,
            content: '{nt}'
          }
        },
        {
          title: '今年达标任务个数',
          number: {
            number: [100],
            toFixed: 1,
            content: '{nt}'
          }
        }
      ],
      ranking: {
        data: [
          {
            name: '王坚',
            value: 55
          },
          {
            name: '张建锋',
            value: 120
          },
          {
            name: '蔡景现',
            value: 78
          },
          {
            name: '刘振飞',
            value: 66
          },
          {
            name: '程立',
            value: 80
          },
          {
            name: '胡喜',
            value: 90
          },
          {
            name: '蒋江伟',
            value: 70
          },
          {
            name: '吴泽明',
            value: 50
          }
        ],
        waitTime: 13000,
        carousel: 'page',
        unit: '份'
      },
      water: {
        data: [24, 45],
        shape: 'roundRect',
        formatter: '{value}%',
        waveNum: 3
      }
    }
  }
}
</script>

<style lang="scss" scoped>
#center {
  display: flex;
  flex-direction: column;
  .up {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
    .item {
      border-radius: 5px;
      padding-top: 1rem;
      margin-top: 0.5rem;
      width: 32%;
      height: 70px;
    }
  }
  .down {
    padding: 4px;
    padding-bottom: 0px;
    width: 100%;
    display: flex;
    height: 255px;
    justify-content: space-between;
    .bg-color-black {
      border-radius: 5px;
    }
    .ranking {
      padding: 10px;
      width: 59%;
    }
    .percent {
      width: 40%;
      display: flex;
      flex-wrap: wrap;
      .item {
        width: 50%;
        height: 120px;
        span {
          margin-top: 7px;
          display: flex;
          justify-content: center;
        }
      }
      .water {
        width: 100%;
      }
    }
  }
}
</style>
