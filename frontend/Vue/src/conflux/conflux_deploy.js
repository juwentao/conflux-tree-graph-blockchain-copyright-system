/**
 * 这部分代码是通过JavaScript进行智能合约的部署 需要coin.abi.json及con.bin.json文件，可以从remix智能合约编译器编译得到
 */
const { Conflux } = require('js-conflux-sdk');
const PRIVATE_KEY = '0x0B56FA01455E5B9F1BD515762201F5AD3D2272E5EF9D66AFEC6C54758962B054';//私钥

async function main() {
    const conflux = new Conflux({
        url: 'http://202.197.99.35:12537/', //私链的url
        networkId: 1024, //链id为1024
        defaultGasPrice: 100,
        defaultGas: 21001,
        // logger: console, // use console to print log
    });

    const account = conflux.wallet.addPrivateKey((PRIVATE_KEY)); //部署合约的签名地址
    // ================================ Contract ================================
    // create contract instance
    const contract1 = conflux.Contract({ //合约的构造
        abi: require('./abi/CopyrightSystemTransactions.json'),
        bytecode: require('./abi/copyrightSystemTransactions.bin.json'),
    });

    // deploy the contract, and get `contractCreated`
    const receipt1 = await contract1.constructor().sendTransaction({ from: account}).confirmed();
    console.log("合约1："+receipt1.contractCreated);

    const contract2 = conflux.Contract({ //合约的构造
        abi: require('./abi/HnuCoin.json'),
        bytecode: require('./abi/hnuCoin.bin.json'),
    });

    // deploy the contract, and get `contractCreated`
    const receipt2 = await contract2.constructor(
        receipt1.contractCreated
    ).sendTransaction({ from: account}).confirmed();
    console.log("合约2："+receipt2.contractCreated);


    const contract3 = conflux.Contract({ //合约的构造
        abi: require('./abi/Copyright.json'),
        bytecode: require('./abi/copyright.bin.json'),
    });

    // deploy the contract, and get `contractCreated`
    const receipt3 = await contract3.constructor(
        'csc','csc',receipt1.contractCreated,
    ).sendTransaction({ from: account}).confirmed();
    console.log("合约3："+receipt3.contractCreated);

}

main().catch(e => console.error(e));
