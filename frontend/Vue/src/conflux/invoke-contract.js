/**
 * 这一部分是智能合约的调用，需要已部署的合约地址
 */
const { Conflux } = require('js-conflux-sdk');

async function main() {
    const PRIVATE_KEY = '0x0B56FA01455E5B9F1BD515762201F5AD3D2272E5EF9D66AFEC6C54758962B054';

    const cfx = new Conflux({
        url: 'http://202.197.99.35:12537/',
        defaultGasPrice: 100,
        defaultGas: 1000000,
        //logger: console,
        networkId: 1024,
    });

    const account = cfx.wallet.addPrivateKey(PRIVATE_KEY); // create account instance
    // const address1='NET1024:TYPE.CONTRACT:ACF6JMUYWHC084E4GA4PBZBPZ0CY3AWJUAEPN97DU4'
    // const address2='NET1024:TYPE.CONTRACT:ACDXDD197H1F0ER4ZSTJN4GU060BXB6TBY4AU8993V'
    // const address3='NET1024:TYPE.CONTRACT:ACCXWWFBWDT7EWP08V9P6BJN40VD3CRYHJKU1YSJ8J'
    const contractAddress1 = 'NET1024:TYPE.CONTRACT:ACF6JMUYWHC084E4GA4PBZBPZ0CY3AWJUAEPN97DU4';
    const contractAddress2 = 'NET1024:TYPE.CONTRACT:ACDXDD197H1F0ER4ZSTJN4GU060BXB6TBY4AU8993V';
    const contractAddress3 = 'NET1024:TYPE.CONTRACT:ACCXWWFBWDT7EWP08V9P6BJN40VD3CRYHJKU1YSJ8J';
    const contract1 = cfx.Contract({
        abi: require('./abi/CopyrightSystemTransactions.json'),
        address: contractAddress1,
    });
    const contract2 = cfx.Contract({
        abi: require('./abi/HnuCoin.json'),
        address: contractAddress2,
    });
    const contract3 = cfx.Contract({
        abi: require('./abi/Copyright.json'),
        address: contractAddress3,
    });
    //调用合约的写方法
    const tx = await contract1._exists.call('123');
    console.log(tx)
    // console.log(await cfx.getTransactionByHash('0xec119820aabcfbce6a4c1b15f69cb4bf32f1fd907303263ddedabfafb2bf2d0f'));
    // console.log('调用合约成功,交易收据为'+tx);

    // //调用合约读方法
    // const ret = await contract3._exists('12345').call({
    //     from:account
    // });
    // console.log('调用_exists'+ret);

   //根据交易哈希获取交易信息
   //   const result=await cfx.getTransactionByHash('0xfda667bc4caf02224710113b4615a8adaa3c51ca0d8d3a0ce8fc3dca0099da26')
   //   console.log(result);

    //将交易中data数据解码
    //console.log(await contract.abi.decodeData('0xdb49adf4000000000000000000000000000000000000000000000000000000000000006000000000000000000000000000000000000000000000000000000000000000a000000000000000000000000000000000000000000000000000000000000000e0000000000000000000000000000000000000000000000000000000000000000c3230313732363031303231340000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000334303400000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008323031382d352d36000000000000000000000000000000000000000000000000'));

}

main().catch(e => console.error(e));
