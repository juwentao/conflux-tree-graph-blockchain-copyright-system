<template>
  <div class="NewsDetails">
    <banner img="../assets/img/bgtop.jpg" />
    <div class="NewsDetails-product">
      <div class="NewsDetails-product-content">
        <img v-lazy="newsIdList.url" alt />
        <p class="product-title">{{newsIdList.title}}</p>
        <p class="product-time">{{newsIdList.createtime.toLocaleString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')}}</p>
        <p class="product-content">{{newsIdList.content}}</p>
      </div>
    </div>
  </div>
</template>

<script>
import Banner from "../components/Banner";
export default {
  name: "NewsDetails",
  components: {
    Banner
  },
  data() {
    return {
      pid: 0,
      newsIdList: {}
    };
  },
  created() {
    this.pid = this.$route.params.id;
    window.console.log(this.pid);
    console.log("初始化咯")
    this.$emit('header',true);
    this.$emit('footer',true);
  },
  mounted() {
    this.loadData();
  },
  methods: {
    loadData() {
      this.$axios.post(`new/getNewById/`,{id: this.pid})
        .then(response => {
          //console.log(response);
          this.newsIdList = response.data.result;
          window.console.log(this.newsIdList);
        })
        .catch(function(error) {
          window.console.log(error);
        });
    }
  }
};
</script>

<style lang="scss" scoped>
.NewsDetails {
  width: 100%;
  height: 100%;
  //overflow: hidden;
  background-color: #14679f;
  &-product {
    width: 1240px;
    margin: 0 auto;
    background-color: #fff;
    border: 1px solid red;
    &-content {
      width: 760px;
      margin: 0 auto;
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      padding: 50px 0;
      img {
        width: 100%;
        //height: 500px;
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