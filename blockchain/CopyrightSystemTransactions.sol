// SPDX-License-Identifier: MIT
pragma solidity >=0.8.0;

interface IERC20 
{
    function totalSupply() external view returns (uint256);
    function balanceOf(address account) external view returns (uint256);
    function transfer(address sender,address recipient, uint256 amount) external returns (bool);
    function allowance(address owner, address spender) external view returns (uint256);
    function approve(address spender, uint256 amount) external returns (bool);
    function transferFrom(address sender, address recipient, uint256 amount) external returns (bool);
    function test()  external view returns (address);

    event Transfer(address indexed from, address indexed to, uint256 value);

    event Approval(address indexed owner, address indexed spender, uint256 value);
}

interface COPYRIGHT 
{
    function ownerOf(string memory workId)  external view returns (address);

    function transferFrom(address from,address to,string memory workId,string memory ownername,string memory ownerid)  external returns(bool);

    function downloadWorks(address _downloadAddr,string memory _workId,string memory _downloadName,string memory _downloadId) external returns(bool);

    function addWorks(address to,string memory workId, string memory uri ,string memory ownername ,string memory ownerid) external returns(bool);

    event Transfer(address indexed from, address indexed to, string indexed workId);

    event Approval(address indexed owner, address indexed approved, string indexed workId);
}


contract CopyrightSystemTransactions
{
    
    
    // 初始化湖大币的合约地址，因为创建版权合约的时候还没创建湖大币合约
    // 所以需要告诉红包合约赠送的湖大币的合约地址多少
    // 湖大币合约里最后调用的方法就是这个方法
    function initBonusCoin(address initTokenAddr) external
    {
        // 只允许初始化一次就不允许任何人再修改这个地址了
        require(HNUCoinAddr == address(0), "Already Initialized");
        HNUCoinAddr = initTokenAddr;
    }
    
    function initTransactionsAddr(address _initTransactionsAddr) external 
    {
        // 只允许初始化一次就不允许任何人再修改这个地址了
        require(TransactionsAddr == address(0), "Already Initialized");
        TransactionsAddr = _initTransactionsAddr;
    }

    // 湖大币的合约地址
    address public HNUCoinAddr;
    
    //版权合约的合约地址
    address public TransactionsAddr;

   
    /**
     * @dev 购买著作权
     */
    function dealDone(string memory workId,uint256 money,string memory ownername,string memory ownerid) public returns(bool)
    {
        IERC20 token = IERC20(HNUCoinAddr);
        COPYRIGHT copyright=COPYRIGHT(TransactionsAddr);
        address owner = copyright.ownerOf(workId);
        require(token.transfer(msg.sender,owner, money), "Token transfer fail");
        require(copyright.transferFrom(owner,msg.sender, workId,ownername,ownerid), "work transfer fail");
        return true;
    }

    /**
     * @dev 购买使用著作权
     */
    function download(string memory workId,uint256 money,string memory downloadname,string memory downloadid) public returns(bool)
    {
        IERC20 token = IERC20(HNUCoinAddr);
        COPYRIGHT copyright=COPYRIGHT(TransactionsAddr);
        address owner = copyright.ownerOf(workId);
        require(token.transfer(msg.sender,owner, money), "Token transfer fail");
        require(copyright.downloadWorks(msg.sender, workId,downloadname,downloadid), "work download fail");
        return true;
    }

    /**
     * @dev 添加著作权
     */
    function addWorks(string memory workId, string memory uri ,string memory ownername ,string memory ownerid,uint256 money) public returns(bool)
    {
        IERC20 token = IERC20(HNUCoinAddr);
        COPYRIGHT copyright=COPYRIGHT(TransactionsAddr);
        address receiver=address(this);
        address sender=msg.sender;
        require(token.transfer(sender,receiver, money), "Token transfer fail");
        require(copyright.addWorks(sender, workId, uri, ownername, ownerid), "work download fail");
        return true;
    }
    
    function getMoney(uint256 money) public returns(bool)
    {
        address receiver=msg.sender;
        IERC20 token = IERC20(HNUCoinAddr);
        address sender=address(this);
        require(token.transfer(sender,receiver, money), "Token transfer fail");
        return true;
    }
}