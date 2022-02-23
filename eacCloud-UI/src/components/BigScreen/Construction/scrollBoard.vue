<template>
  <div id="scroll-board">
    <dv-scroll-board :config="config" class="row-item" />
  </div>
</template>

<script>
export default {
  name: 'ScrollBoard',
  data() {
    return {
      config: {
        header: ['时间', '病害信息', '数量', '标段'],
        data: [
          ['2021-07-01 19:25:00', '路面危害-松散', '5', '路面'],
          ['2021-07-02 17:25:00', '路面危害-路面油污清理', '13', '路面'],
          ['2021-07-03 16:25:00', '交安设施-交通标志牌结构', '6', '交安设施'],
          ['2021-07-04 15:25:00', '路基危害-防尘网', '2', '路基'],
          ['2021-07-05 14:25:00', '交安设施-交通标志牌结构', '1', '交安设施'],
          ['2021-07-06 13:25:00', '路面危害-松散', '3', '路面'],
          ['2021-07-07 12:25:00', '路基危害-防尘网', '4', '路基'],
          ['2021-07-08 11:25:00', '路面危害-路面油污清理', '2', '路面'],
          ['2021-07-09 10:25:00', '交安设施-交通标志牌结构', '5', '交安设施'],
          ['2021-07-10 09:25:00', '路基危害-防尘网', '3', '路基'],
          ['2021-07-11 18:25:00', '路面危害-路面油污清理', '4', '路面'],
          ['2021-07-12 06:25:00', '交安设施-交通标志牌结构', '10', '交安设施'],
          ['2021-07-13 19:25:00', '路基危害-防尘网', '6', '路基']
        ],
        index: true,
        columnWidth: [50, 170, 300],
        align: ['center'],
        rowNum: 7,
        headerBGC: '#1981f6',
        headerHeight: 45,
        oddRowBGC: 'rgba(0, 44, 81, 0.8)',
        evenRowBGC: 'rgba(10, 29, 50, 0.8)'
      }
    }
  }
}
</script>

<style lang="scss" scoped>
#scroll-board {
  width: 50%;
  box-sizing: border-box;
  margin-left: 20px;
  height: 100%;
  overflow: hidden;
}
.row-item {
  height: 33.65px;
  line-height: 33.65px;
}
</style>
