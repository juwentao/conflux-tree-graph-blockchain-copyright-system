/**
 * 这一部分是智能合约的调用，需要已部署的合约地址
 */
const { Conflux } = require('js-conflux-sdk');

async function main() {
    const PRIVATE_KEY = '0x0B56FA01455E5B9F1BD515762201F5AD3D2272E5EF9D66AFEC6C54758962B054';
    const PRIVATE_KEY1 = '0xC346F300030CFB754B4CB2437F9E974A091E5C2A9B0274B5ADCF91A4D47BC51D';
    const PRIVATE_KEY_sponsor='0xC962382AEAB1E787A287D85879A69BF9B1B01913B464314F31DD9268E81B158A';

    const cfx = new Conflux({
        url: 'http://202.197.99.35:12537/',
        defaultGasPrice: 100,
        defaultGas: 1000000,
        //logger: console,
        networkId: 1024,
    });

    const account = cfx.wallet.addPrivateKey(PRIVATE_KEY); // create account instance
    const account1= cfx.wallet.addPrivateKey(PRIVATE_KEY1); // create account instance
    const account_sponsor = cfx.wallet.addPrivateKey(PRIVATE_KEY_sponsor); // create account instance
    const contractAddress = 'NET1024:TYPE.CONTRACT:ACBEYWSHHHDEUP9K8ZWVYY4KVDWMV4MV6E8J8RRU37';
    // const contract = cfx.Contract({
    //     abi: require('./copyrightSystemTransactions.abi.json'),
    //     address: contractAddress,
    // });
    //======================================================内置合约调用部分
    // //引入内置赞助合约
    const sponsor_contract = cfx.InternalContract('SponsorWhitelistControl');
    // //引入内置管理员合约
    //  const Admin_contract=cfx.InternalContract('AdminControl');
    //  设置管理员
    // const tx0=await Admin_contract.setAdmin(contractAddress,sponsor_contract).sendTransaction({
    //     from: account,
    // }).confirmed();
    // console.log(tx0);

    const address1='NET1024:TYPE.CONTRACT:ACF6JMUYWHC084E4GA4PBZBPZ0CY3AWJUAEPN97DU4'
    const address2='NET1024:TYPE.CONTRACT:ACDXDD197H1F0ER4ZSTJN4GU060BXB6TBY4AU8993V'
    const address3='NET1024:TYPE.CONTRACT:ACCXWWFBWDT7EWP08V9P6BJN40VD3CRYHJKU1YSJ8J'
    // //为合约设置赞助燃料费用的账户以及赞助的金额
    const tx1= await sponsor_contract.setSponsorForGas(address1, 1000000000000).sendTransaction({
        from: account,
        value: 1000000000000000000000,
    }).confirmed();
    console.log(tx1);

    // // // //为合约设置赞助存储抵押物费用的账户以及赞助的金额
    const tx2= await sponsor_contract.setSponsorForCollateral(address1).sendTransaction({
        from: account,
        value: 1000000000000000000000,
    }).confirmed();
    console.log(tx2);

    // // //合约添加白名单，该名单内的账户可以享受赞助，0x000000000为所有账户都添加入白名单
    var arr = new Array(1);
    arr[0]='0x0000000000000000000000000000000000000000';
    const tx3=await sponsor_contract.addPrivilegeByAdmin(address1,arr).sendTransaction({
        from: account,
    }).confirmed();
    console.log(tx3);
    console.log('111111111');
//------------------------------------------合约2---------------------------------------------------
    // // //为合约设置赞助燃料费用的账户以及赞助的金额
    const tx4= await sponsor_contract.setSponsorForGas(address2, 1000000000000).sendTransaction({
        from: account,
        value: 1000000000000000000000,
    }).confirmed();
    console.log(tx4);

    // // // //为合约设置赞助存储抵押物费用的账户以及赞助的金额
    const tx5= await sponsor_contract.setSponsorForCollateral(address2).sendTransaction({
        from: account,
        value: 1000000000000000000000,
    }).confirmed();
    console.log(tx5);

    // // //合约添加白名单，该名单内的账户可以享受赞助，0x000000000为所有账户都添加入白名单
    var arr = new Array(1);
    arr[0]='0x0000000000000000000000000000000000000000';
    const tx6=await sponsor_contract.addPrivilegeByAdmin(address2,arr).sendTransaction({
        from: account,
    }).confirmed();
    console.log(tx6);
    console.log('111111111');
//------------------------------------------合约3---------------------------------------------------
    // // //为合约设置赞助燃料费用的账户以及赞助的金额
    const tx7= await sponsor_contract.setSponsorForGas(address3, 1000000000000).sendTransaction({
        from: account,
        value: 1000000000000000000000,
    }).confirmed();
    console.log(tx7);

    // // // //为合约设置赞助存储抵押物费用的账户以及赞助的金额
    const tx8= await sponsor_contract.setSponsorForCollateral(address3).sendTransaction({
        from: account,
        value: 1000000000000000000000,
    }).confirmed();
    console.log(tx8);

    // //合约添加白名单，该名单内的账户可以享受赞助，0x000000000为所有账户都添加入白名单
    var arr = new Array(1);
    arr[0]='0x0000000000000000000000000000000000000000';
    const tx9=await sponsor_contract.addPrivilegeByAdmin(address3,arr).sendTransaction({
        from: account,
    }).confirmed();
    console.log(tx9);
    console.log('111111111');
//====================================================合约调用部分
    //调用合约的register方法,合约调用者可以申领100HDB
    let address = 'net1024:aanggmnc7gxygx052tey1zjcsmgwb2813j4dae8n2j';
    const tx = await contract.borrowRecord('13515','2021-11-30','2021-12-20').sendTransaction({
        from: account_sponsor,
    }).confirmed();
//     console.log(await tx.getLogs())
    //console.log(await cfx.getTransactionByHash('0xec119820aabcfbce6a4c1b15f69cb4bf32f1fd907303263ddedabfafb2bf2d0f'));
    // console.log('调用合约申领代币成功,交易收据为'+tx);
//
//     const tx = await contract.bookRecord('2022','231213','2021-12-20').sendTransaction({
//         from: account_sponsor,
//         }).confirmed();
//         console.log(tx)
//       const ret = await contract.balanceOf('0x84fc4c05d05f9807446746ba8ebb873697d2a697');
//       console.log(ret);
//
//    //调用智能合约中的borrowRecord方法
//    ret1=await contract.borrowRecord('加密数据').sendTransaction({
//        from:account,
//    }).confirmed();
//    console.log('调用智能合约将加密数据传入成功,交易收据为'+ret1);
//
//     //get balance of address
//     ret = await contract.balanceOf(address);
//     console.log('调用boorowrecord后再次查看余额,可以看到减少了10HDB'+ret);
//
//    //根据交易哈希获取交易信息
    //const result=await cfx.getTransactionByHash('0xfda667bc4caf02224710113b4615a8adaa3c51ca0d8d3a0ce8fc3dca0099da26')
    //console.log(result);

    //将交易中data数据解码
    //console.log(await contract.abi.decodeData('0xdb49adf4000000000000000000000000000000000000000000000000000000000000006000000000000000000000000000000000000000000000000000000000000000a000000000000000000000000000000000000000000000000000000000000000e0000000000000000000000000000000000000000000000000000000000000000c3230313732363031303231340000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000334303400000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008323031382d352d36000000000000000000000000000000000000000000000000'));

}

main().catch(e => console.error(e));
