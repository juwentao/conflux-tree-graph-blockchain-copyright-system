<template>
  <div class="case">
    <banner img="../assets/img/bgtop.jpg" />
    <div class="case-product">
      <div class="case-product-content">
        <img v-lazy="caseIdList.url" alt/>
        <p class="product-title">{{caseIdList.title}}</p>
        <p class="product-time">{{caseIdList.createtime.toLocaleString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')}}</p>
        <p class="product-content">{{caseIdList.content}}</p>
      </div>
    </div>
  </div>
</template>

<script>
import Banner from "../components/Banner";
export default {
  components: {
    Banner
  },
  data() {
    return {
      pid: 0,
      caseIdList: {}
    };
  },
  created() {
    this.pid = this.$route.params.id;
    console.log("初始化咯")
    this.$emit('header',true);
    this.$emit('footer',true);
    window.console.log(this.pid);
  },
  mounted() {
    this.loadData();
  },
  methods: {
    loadData() {
      this.$axios.post(`/case/getCaseById/`, {id: this.pid}).then(response => {
        if (response.data.code==='200') {
          this.$message({
            message: "获取案例详情成功",
            type: "success"
          });
          this.caseIdList = response.data.result;
        } else {
          this.$message({
            message: "获取案例详情失败",
            type: "error"
          });
        }
      }).catch(e => {
          this.$message({
            message: "网络异常！",
            type: "error"
          });
      });
    }
  },
};
</script>

<style lang="scss" scoped>
.case {
  width: 100%;
  height: 100%;
  overflow: hidden;
  background-color: #14679f;
  &-product {
    width: 1240px;
    margin: 0 auto;
    background-color: #fff;
    //border: 1px solid red;
    &-content {
      width: 760px;
      margin: 0 auto;
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      padding: 50px 0;
      img {
        width: 100%;
        height: 430px;
      }
      .product-title {
        font-size: 25px;
        color: #e13834;
        padding: 20px 0;
      }
      .product-content {
        font-size: 17px;
        font-weight: bolder;
        padding: 20px 0;
      }
    }
  }
}
</style>