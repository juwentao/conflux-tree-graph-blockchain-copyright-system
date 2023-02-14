import { Conflux } from 'js-conflux-sdk'

import abiCopyrightSystemTransactions from './abi/CopyrightSystemTransactions'
import abiHnuCoin from './abi/HnuCoin'
import abiCopyright from './abi/Copyright'

import abiSponsorWhitelistControl from './abi/SponsorWhitelistControl.json'

const conflux = new Conflux({
    url: 'http://202.197.99.35:12537/',
    defaultGasPrice: 100, // The default gas price of your following transactions
    defaultGas: 1000000, // The default gas of your following transactions
    networkId: 1024, //链id为1024
})


export const ContractCopyrightSystemTransactions = {
    name: 'CopyrightSystemTransactions',
    abi: abiCopyrightSystemTransactions,
    contract: conflux.Contract({
        abi: abiCopyrightSystemTransactions,
        address: 'NET1024:TYPE.CONTRACT:ACF6JMUYWHC084E4GA4PBZBPZ0CY3AWJUAEPN97DU4',
    }),
}

export const ContractHnuCoin = {
    name: 'HnuCoin',
    abi: abiHnuCoin,
    contract: conflux.Contract({
        abi: abiHnuCoin,
        address: 'NET1024:TYPE.CONTRACT:ACDXDD197H1F0ER4ZSTJN4GU060BXB6TBY4AU8993V',
    }),
}

export const ContractCopyright = {
    name: 'Copyright',
    abi: abiCopyright,
    contract: conflux.Contract({
        abi: abiCopyright,
        address: 'NET1024:TYPE.CONTRACT:ACCXWWFBWDT7EWP08V9P6BJN40VD3CRYHJKU1YSJ8J',
    }),
}

export const ContractSponsorWhitelistControl = {
    name: 'SponsorWhitelistControl',
    abi: abiSponsorWhitelistControl,
    contract: conflux.Contract({
        abi: abiSponsorWhitelistControl,
        address: '0x0888000000000000000000000000000000000001',
    }),
}

export default conflux
