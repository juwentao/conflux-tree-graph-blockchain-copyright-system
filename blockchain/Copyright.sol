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



    /**
     * @dev 版权平台智能合约demo1，因为作品和nft类似，所以以nft的ERC721标准为例，仿写了一套版权智能合约实现版权的确权以及后续的版权交易
     */
contract Copyright{
    
    /**
     * @dev 事件日志，在版权转移时触发记录
     */
    event Transfer(address indexed from, address indexed to, string indexed workId);

    /**
     * @dev 事件日志，在某个作者版权作品授权时触发记录
     */
    event Approval(address indexed owner, address indexed approved, string indexed workId);

    /**
     * @dev 事件日志，在某个作者所有版权作品授权时触发记录
     */
    event ApprovalForAll(address indexed owner, address indexed operator, bool approved);
    
    struct Work
    {
        address owner;
        string workUri;
        string workOwnername;
        string workOwnerid;
    }

    struct workDownload{
        address downloadAddr;
        string downloadName;
        string downloadId;
    }
 

 

    //该版权合约的创始者
    address private _owner;

    // 版权token名称
    string private _name;

    // 版权token标志
    string private _symbol;
    
    //作品版权id到版权下载列表的映射，记录谁被授权下载了这个版权作品
    mapping(string => workDownload[]) _workDownloadList;

    // 版权作品id到版权拥有者地址的映射，用于存储某个id作品的拥有者,版权作品id用版权文件hash作为唯一标识
    mapping(string => address) private _owners;
    
    // 版权作品id到版权信息的映射，用于存储某个作品的详细信息，是一个结构体
    mapping(string => Work) private _works;

    // 版权作品拥有者地址到版权作品数量的映射，用于存储该拥有者有多少个版权作品
    mapping(address => uint256) private _workNumber;

    // 版权作品id到地址的映射，用于存储该版权作品授权给了哪个地址，该地址可以转移这个版权作品拥有者的这个id作品
    mapping(string => address) private _workApprovals;

    // 一个二级映射结构，用于存储某个版权拥有者是否授权给了某个地址所以的他的版权转移权限，bool变量true为是
    mapping(address => mapping(address => bool)) private _operatorApprovals;

    // 一个二级映射结构，用于存储某个地址所拥有的所有版权作品中的某个index的作品id
    mapping(address => mapping(uint256 => string)) private _ownedWorks;

    //作品id到index的映射用于获取该作品id在作品列表中的索引值
    mapping(string => uint256) private _ownedWorksIndex;

    // 存储所有的作品的数组
    string[] private _allWorks;

    // 存储某个作品id在所有作品数组中的索引值
    mapping(string => uint256) private _allWorksIndex;


    
    // 调用版权交易合约的方法initTransactionsAddr
    bytes4 private constant SELECTOR = bytes4(keccak256(bytes("initTransactionsAddr(address)")));
    
    constructor(string memory name_, string memory symbol_,address redenvelopAddr_)
    {
        _name = name_;
        _symbol = symbol_;
        _owner = msg.sender;
        // 调用版权交易合约的initTransactionsAddr方法，将版权合约地址注入进去
        (bool success, bytes memory data) = redenvelopAddr_.call(abi.encodeWithSelector(SELECTOR, address(this)));
        require(success && (data.length == 0 || abi.decode(data, (bool))), "INIT_FAILED");
    }
    
    
    function getName() public view  returns (string memory) 
    {
        return _name;
    }

   
    function getSymbol() public view  returns (string memory) 
    {
        return _symbol;
    }

    function getOwner() public view  returns (address ) 
    {
        return _owner;
    }

    /**
     * @dev 查询某个作品id的拥有者
     */
    function ownerOf(string memory workId) public view returns (address)
    {
        address owner = _owners[workId];
        require(owner != address(0), "owner query for nonexistent work");
        return owner;
    }

    /**
     * @dev 获取作品的URI，即作品的描述信息
     */
    function workURI(string memory workId) public view returns (string memory) 
    {
        require(_exists(workId), "URI query for nonexistent work");

        string memory _workURI = _works[workId].workUri;
       
        return _workURI;
    }

    /**
     * @dev 获取作品的著作权人名字
     */
    function workOwnername(string memory workId) public view returns (string memory) 
    {
        require(_exists(workId), "URI query for nonexistent work");

        string memory _workOwnername = _works[workId].workOwnername;
      
        return _workOwnername;
    }

    /**
     * @dev 获取作品的著作权人id
     */
    function workOwnerid(string memory workId) public view returns (string memory) 
    {
        require(_exists(workId), "URI query for nonexistent work");

        string memory _workOwnerid = _works[workId].workOwnerid;
      
        return _workOwnerid;
    }

    /**
     * @dev 按index序号返回该owner的所有持有版权作品的id
     */
     function workOfOwnerByIndex(address owner, uint256 index) public view returns (string memory) {
        require(index < Copyright.workNumsOf(owner), "owner index out of bounds");
        return _ownedWorks[owner][index];
    }

    /**
     * @dev 所有版权作品的数量
     */
    function totalWork() public view returns (uint256) {
        return _allWorks.length;
    }

    /**
     * @dev 按index序号返回作品的id
     */
    function workByIndex(uint256 index) public view returns (string memory) {
        require(index < Copyright.totalWork(), "global index out of bounds");
        return _allWorks[index];
    }


    /**
     * @dev 查询某个地址所拥有的版权数量
     */
    function workNumsOf(address owner) public view returns (uint256) 
    {
        require(owner != address(0), "balance query for the zero address");//该地址不能为0地址
        return _workNumber[owner];
    }

     /**
     * @dev 获取该作品授权给了哪个地址
     */
    function getApproved(string memory workId) public view returns (address)
    {
        require(_exists(workId), "approved query for nonexistent work");

        return _workApprovals[workId];
    }

    /**
     * @dev 查询某个地址是否被另一个地址拥有了他所有作品的转让权
     */
    function isApprovedForAll(address owner, address operator) public view  returns (bool) 
    {
        return _operatorApprovals[owner][operator];
    }

    /**
     * @dev 判断某个作品id是否已经存在，不允许重复
     */
    function _exists(string memory workId) internal view virtual returns (bool) 
    {
        return _owners[workId] != address(0);
    }

    /**
     * @dev 判断该地址是否时作品所有者或者是授权者，用于判断当前操作者是否拥有转移权
     */
    function _isApprovedOrOwner(address spender, string memory workId) internal view virtual returns (bool) 
    {
        require(_exists(workId), "operator query for nonexistent work");
        address owner = Copyright.ownerOf(workId);
        return (spender == owner || isApprovedForAll(owner, spender) || getApproved(workId) == spender);
    }

    /**
     * @dev 判断该地址是否获得了某个作品的下载授权
     */
     function isDownload(string memory workId,address downloadAddr) public view  returns (bool) 
    {
        workDownload[] memory downloadList=_workDownloadList[workId];
        for (uint i = 0; i < downloadList.length; i++) {
            if(downloadList[i].downloadAddr==downloadAddr)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @dev 判断该地址是否获得了某个作品的下载授权
     */
     function workdownloaders(string memory workId) public view  returns (workDownload[] memory) 
    {
        workDownload[] memory downloadList=_workDownloadList[workId];
      
        return downloadList;
    }

    //__________________写方法____________________

    /**
     * @dev 把workid的作品授权给某个地址，该地址拥有对该作品的转让权
     */
    function approve(address to, string memory workId) public
    {
        address owner = Copyright.ownerOf(workId);
        require(to != owner, "approval to current owner");
        require(
            msg.sender == owner || isApprovedForAll(owner,msg.sender),
            "approve caller is not owner nor approved for all"
        );

        _approve(to, workId);
    }
    
    function _approve(address to, string memory workId) internal virtual 
    {
        _workApprovals[workId] = to;
        emit Approval(Copyright.ownerOf(workId), to, workId);
    }
   
   


    /**
     * @dev 授权给operator你的所有作品的转让权
     */
    function setApprovalForAll(address operator, bool approved) public 
    {
        _setApprovalForAll(msg.sender, operator, approved);
    }

    function _setApprovalForAll(address owner,address operator,bool approved) internal virtual 
    {
        require(owner != operator, "approve to caller");
        _operatorApprovals[owner][operator] = approved;
        emit ApprovalForAll(owner, operator, approved);
    }
    
   /**
     * @dev 作品的转移，从from到to
     */
    function transferFrom(address from,address to,string memory workId,string memory ownername,string memory ownerid) public returns(bool)
    {
        //作品若要转移则先要看当前操作者是否是该作品的拥有者，或者拥有对该作品的授权
        require(_isApprovedOrOwner(msg.sender, workId), "transfer caller is not owner nor approved");
        _transfer(from, to, workId,ownername,ownerid);
        return true;

    }

    function _transfer(address from,address to,string memory workId, string memory ownername, string memory ownerid) internal virtual 
    {
        require(Copyright.ownerOf(workId) == from, "transfer from incorrect owner");
        require(to != address(0), "transfer to the zero address");

        _beforeWorkTransfer(from, to, workId);

        // 转让给其他人后，先前的授权失效，所以要清空
        _approve(address(0), workId);

        _workNumber[from] -= 1;
        _workNumber[to] += 1;
        _owners[workId] = to;
        _works[workId].workOwnername=ownername;
        _works[workId].workOwnerid=ownerid;

        emit Transfer(from, to, workId);

        _afterWorkTransfer(from, to, workId);
    }


    /**
     * @dev 下载作品
     */
    function downloadWorks(address downloadAddr,string memory workId,string memory downloadName,string memory downloadId) public returns(bool)
    {
        require(_isApprovedOrOwner(msg.sender, workId), "transfer caller is not owner nor approved");
        _downloadWorks(downloadAddr,workId,downloadName,downloadId);
        return true;
    }

    function _downloadWorks(address _downloadAddr,string memory _workId,string memory _downloadName,string memory _downloadId) internal virtual 
    {
        require(_downloadAddr != address(0), "transfer to the zero address");
        require(_exists(_workId), "work does'n exists");
        _workDownloadList[_workId].push(workDownload({
            downloadAddr:_downloadAddr,
            downloadName:_downloadName,
            downloadId:_downloadId
        }));
        
    }

    
    /**
     * @dev 添加作品
     */
    function addWorks(address to,string memory workId, string memory uri ,string memory ownername ,string memory ownerid) public returns(bool)
    {
        _addWorks(to, workId, uri, ownername, ownerid);
        return true;
    }
    
    function _addWorks(address to, string  memory workId, string memory _workURI, string memory ownername, string memory ownerid) internal virtual 
    {
        require(to != address(0), "add to the zero address");
        require(!_exists(workId), "work already minted");

        _beforeWorkTransfer(address(0), to, workId);

        _workNumber[to] += 1;
        _owners[workId] = to;
        _works[workId].workUri= _workURI;
        _works[workId].workOwnername=ownername;
        _works[workId].workOwnerid=ownerid;
        emit Transfer(address(0), to, workId);

        _afterWorkTransfer(address(0), to, workId);
    }


    /**
     * @dev 销毁作品id
     */
    function _burn(string memory workId) internal virtual 
    {
        address owner = Copyright.ownerOf(workId);

        _beforeWorkTransfer(owner, address(0), workId);

        // 清除原先对该作品的授权
        _approve(address(0), workId);

        _workNumber[owner] -= 1;
        delete _owners[workId];
        delete _works[workId];
      
        emit Transfer(owner, address(0), workId);

        _afterWorkTransfer(owner, address(0), workId);
    }


    /**
     * @dev 在任何版权作品发生转移之前调用
     *
     * Calling conditions:
     *
     * 当 `from` 和 `to` 都非零时，`from` 的 `workId` 将被转移到 `to`。
     * 当 `from` 为零时，`workId` 将为`to` 铸造。
     * 当 `to` 为 0 时，`from` 的 `workId` 将被销毁。
     * `from` 和 `to` 永远不会都是零。
     */
    function _beforeWorkTransfer(address from,address to,string memory workId) internal virtual 
    {
        if (from == address(0)) 
        {
            _addWorkToAllWorksEnumeration(workId);
        } 
        else if (from != to) 
        {
            _removeWorkFromOwnerEnumeration(from, workId);
        }

        if (to == address(0)) 
        {
            _removeWorkFromAllWorksEnumeration(workId);
        } 
        else if (to != from) 
        {
            _addWorkToOwnerEnumeration(to, workId);
        }
    }

    function _addWorkToOwnerEnumeration(address to, string memory workId) private 
    {
        uint256 length = Copyright.workNumsOf(to);
        _ownedWorks[to][length] = workId;
        _ownedWorksIndex[workId] = length;
    }

    function _addWorkToAllWorksEnumeration(string memory workId) private
    {
        _allWorksIndex[workId] = _allWorks.length;
        _allWorks.push(workId);
    }

    function _removeWorkFromOwnerEnumeration(address from, string memory workId) private 
    {
       

        uint256 lastWorkIndex = Copyright.workNumsOf(from) - 1;
        uint256 workIndex = _ownedWorksIndex[workId];

    
        if (workIndex != lastWorkIndex) {
            string memory lastWorkId = _ownedWorks[from][lastWorkIndex];

            _ownedWorks[from][workIndex] = lastWorkId; 
            _ownedWorksIndex[lastWorkId] = workIndex; 
        }

    
        delete _ownedWorksIndex[workId];
        delete _ownedWorks[from][lastWorkIndex];
    }

     function _removeWorkFromAllWorksEnumeration(string memory workId) private 
     {
   

        uint256 lastWorkIndex = _allWorks.length - 1;
        uint256 workIndex = _allWorksIndex[workId];

    
        string memory lastWorkId = _allWorks[lastWorkIndex];

        _allWorks[workIndex] = lastWorkId; 
        _allWorksIndex[lastWorkId] = workIndex; 

        
        delete _allWorksIndex[workId];
        _allWorks.pop();
    }

    /**
     * @dev 当作品所有权发生变化后被调用，暂时无用，可用于后续转账
     *
     */
    function _afterWorkTransfer(address from,address to,string memory workId) internal virtual {}

     modifier onlyOwner()
    {
        require(msg.sender == _owner, "NOT_CURRENT_OWNER");
         _;
    }
    
   
      
}