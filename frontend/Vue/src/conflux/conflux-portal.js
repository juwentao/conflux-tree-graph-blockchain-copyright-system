import { Drip } from 'js-conflux-sdk'
import { Message } from "element-ui";
class ConfluxPortal {
  // constructor (conflux) {
  //   console.log(conflux)
  //   if (typeof conflux === 'undefined') {
  //     if(confirm('检测到您的浏览器中并未安装conflux钱包插件，请按照教程完成钱包插件安装以及注册')){
  //       window.open("https://github.com/Conflux-Chain/conflux-portal/releases")
  //     }
  //   }
  //   this.conflux = conflux
  // }
  async checkConflux(conflux) {
    if (typeof conflux === 'undefined') {
      if (confirm('检测到您的浏览器中并未安装conflux钱包插件，请按照教程完成钱包插件安装以及注册')) {
        window.open("https://github.com/Conflux-Chain/conflux-portal/releases")
      }
    }

    this.conflux = conflux

  }


  async enable () {
    await this.checkConflux(window.conflux);
    const netWorkId=this.conflux.networkVersion;
    if(netWorkId!=='1024')
    {
      Message.warning("请在钱包插件中将网络切换至 Conflux 1024 Network")
    }
    else
    {
      this.accounts = await this.conflux.enable()
    }


    console.log(this.accounts)
  }

  async getStatus(){
    console.log(await this.conflux.getStatus())
  }

  getAccount () {
    if (!this.accounts) {
      return '';
    }
    return this.accounts[0]
  }

  async sendTransaction (params) {
    return new Promise((resolve, reject) => {
      this.conflux.sendAsync({
        method: 'cfx_sendTransaction',
        params: [params],
        from: params.from,
        gasPrice: '0x09184e72a000', // customizable by user during ConfluxPortal confirmation.
        gas: '0x2710',  // customizable by user during ConfluxPortal confirmation.
        value: '0x00',
      }, (err, data) => {
        if (err) {
          reject(err)
        } else {
          resolve(data)
        }
      }
    )
    })
  }
}

export default new ConfluxPortal(window.conflux)
