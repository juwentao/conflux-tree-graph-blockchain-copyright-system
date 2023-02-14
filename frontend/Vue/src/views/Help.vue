<template>
  <div :key="url">
    <pdf
        ref="pdf"
        v-for="i in numPages"
        :key="i"
        :src="loadingTask"
        :page="i"
    ></pdf>
  </div>
</template>

<script>
import pdf from 'vue-pdf'
export default {
  components:{
    pdf
  },
  name: "Help",
  data(){
    return {
      loadingTask:'',
      url:"http://120.48.51.66:8083/copyright/works/downloadWorks?fileLocation=C%3A%5Capache-tomcat-8.5.68%5Cwebapps%5Ccopyright&fileName=help.pdf",
      numPages: null, // pdf 总页数
    }
  }
  ,
  mounted() {
    this.getNumPages()
  },
  methods: {
    getNumPages() {
      let loadingTask = pdf.createLoadingTask(this.url)
      this.loadingTask=loadingTask
      loadingTask.promise.then(pdf => {
        this.numPages = pdf.numPages
      }).catch(err => {
        this.$message({
          message: '加载pdf失败',
          type: "error"
        });
      })
    },
  },
  created() {
    console.log("初始化咯")
    this.$emit('header',true);
    this.$emit('footer',true);
  }
}
</script>

<style scoped>

</style>