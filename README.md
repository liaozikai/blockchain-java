# java简单实现区块链

1. [【译】用Java创建你的第一个区块链-part1](https://longfeizheng.github.io/2018/03/10/%E7%94%A8Java%E5%88%9B%E5%BB%BA%E4%BD%A0%E7%9A%84%E7%AC%AC%E4%B8%80%E4%B8%AA%E5%8C%BA%E5%9D%97%E9%93%BE-part1/)
2. [【译】用Java创建你的第一个区块链-part2:可交易](https://longfeizheng.github.io/2018/03/11/%E7%94%A8Java%E5%88%9B%E5%BB%BA%E4%BD%A0%E7%9A%84%E7%AC%AC%E4%B8%80%E4%B8%AA%E5%8C%BA%E5%9D%97%E9%93%BE-part2/)


# java实现简单区块链的理解
关于钱包
1.钱包分公私钥。公钥对外可见，私钥则是对交易数据签名。在交易过程中会使用转出者的私钥进行签名，后续在验证过程中通过公钥，数据，以及签名验证数据的正确性。

关于交易
1.交易须有转出者的公钥，收入者的公钥，交易的金额，签名，以及来源utxo和去向utxo。一个输出一般有两个输出。
2.交易数据中保存签名信息，是有转发者用私钥进行签名的，表明这笔数据是由该转出者转出。
3.交易的验证是通过转出者的公钥，签名的数据以及签名三者进行校验的。即验证这笔签名数据是否由该转出者签名的。
4.交易的来源utxo对应转出者的未消费金额utxo列表。而输出的utxo将变成接收者的未消费utxo列表。
5.链上维系者最新的未消费的utxo列表。且每一笔消费将会已经消费的utxo删除掉，新增最新的剩余utxo。

关于区块
1.一个区块可以拥有多笔交易，当前区块哈希，上个区块哈希，当前默克尔树根节点。以及时间戳和nonce。
2.哈希的生成过程即是挖矿的过程。通过对difficulty的设置来实现对挖矿难度的设置。nonce标记挖矿的次数。difficulty值越小，则对应需要匹配中的数值就越大，则需要计算的次数就越大，对应nonce值就越大。
3.计算hash或者默克尔树根节点的算法基本都是将数据用算法计算后转换为64位长度的字符串。
