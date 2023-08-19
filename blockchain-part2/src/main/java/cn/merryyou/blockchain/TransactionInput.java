package cn.merryyou.blockchain;

/**
 * Created on 2018/3/10 0010.
 *
 * @author zlf
 * @email i@merryyou.cn
 * @since 1.0
 */
public class TransactionInput {

    /**
     * 交易输出id
     */
    public String transactionOutputId; //Reference to TransactionOutputs -> transactionId
    /**
     * 上一笔的输出对应这一笔的输入
     */
    public TransactionOutput UTXO; //Contains the Unspent transaction output

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }
}
