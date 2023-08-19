package cn.merryyou.blockchain;

import cn.merryyou.blockchain.utils.StringUtil;

import java.security.PublicKey;

/**
 * Created on 2018/3/10 0010.
 *
 * @author zlf
 * @email i@merryyou.cn
 * @since 1.0
 */
public class TransactionOutput {

    /**
     * 当前交易id
     */
    public String id;
    /**
     * 资金接收者公钥
     */
    public PublicKey reciepient; //also known as the new owner of these coins.
    /**
     * 交易金额
     */
    public float value; //the amount of coins they own
    /**
     * 来源交易id
     */
    public String parentTransactionId; //the id of the transaction this output was created in

    //Constructor
    public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId) {
        this.reciepient = reciepient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(StringUtil.getStringFromKey(reciepient)+Float.toString(value)+parentTransactionId);
    }

    //Check if coin belongs to you
    public boolean isMine(PublicKey publicKey) {
        return (publicKey == reciepient);
    }
}
