package cn.merryyou.blockchain;

import cn.merryyou.blockchain.utils.StringUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

/**
 * 交易类
 * Created on 2018/3/10 0010.
 *
 * @author zlf
 * @email i@merryyou.cn
 * @since 1.0
 */
public class Transaction {

    /**
     * 交易id
     */
    public String transactionId; //Contains a hash of transaction*
    /**
     * 发起转账的账户公钥
     */
    public PublicKey sender; //Senders address/public key.
    /**
     * 接收转账的账户公钥
     */
    public PublicKey reciepient; //Recipients address/public key.
    /**
     * 转账金额
     */
    public float value; //Contains the amount we wish to send to the recipient.
    /**
     * 签名，防止其他人花费这笔交易
     */
    public byte[] signature; //This is to prevent anybody else from spending funds in our wallet.

    /**
     * 来源交易
     */
    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    /**
     * 输出交易
     */
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    /**
     * 表明生产了多少笔交易
     */
    private static int sequence = 0; //A rough count of how many transactions have been generated

    // Constructor:

    /**
     *
     * @param from 资金来源账户公钥
     * @param to 资金去向账户公钥
     * @param value 资金金额
     * @param inputs 交易入参
     */
    public Transaction(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.reciepient = to;
        this.value = value;
        this.inputs = inputs;
    }

    /**
     * 处理交易
     * @return
     */
    public boolean processTransaction() {

        /**
         * 签名交易失败，则无法处理交易
         */
        if(verifySignature() == false) {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }

        //Gathers transaction inputs (Making sure they are unspent):
        // 设置inputs中每个utxo的值，这个是交易资金来源，即上一笔交易结算得到的结果。
        for(TransactionInput i : inputs) {
            i.UTXO = NoobChain.UTXOs.get(i.transactionOutputId);
        }

        //Checks if transaction is valid:
        // 该笔交易的总交易金额小于最小交易金额，则不允许交易
        if(getInputsValue() < NoobChain.minimumTransaction) {
            System.out.println("Transaction Inputs to small: " + getInputsValue());
            return false;
        }

        //Generate transaction outputs:
        // 计算输入的来源金额减去该笔交易的金额得到剩余未消费的金额大小
        float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
        // 计算这笔交易的hash值
        transactionId = calulateHash();
        // 每笔交易产生两笔输出，一笔是接收者收到多少钱，一笔是转发者剩余多少钱
        outputs.add(new TransactionOutput( this.reciepient, value,transactionId)); //send value to recipient
        outputs.add(new TransactionOutput( this.sender, leftOver,transactionId)); //send the left over 'change' back to sender

        //Add outputs to Unspent list
        // 将最新交易的输出来源设置到链的UTXOs中
        for(TransactionOutput o : outputs) {
            NoobChain.UTXOs.put(o.id , o);
        }

        //Remove transaction inputs from UTXO lists as spent:
        // 将链上旧的交易去掉，链上保持最新的未消费的交易数据
        for(TransactionInput i : inputs) {
            if(i.UTXO == null) continue; //if Transaction can't be found skip it
            NoobChain.UTXOs.remove(i.UTXO.id);
        }

        return true;
    }

    /**
     * 获取该笔交易中输入来源所使用的总金额
     * @return
     */
    public float getInputsValue() {
        float total = 0;
        for(TransactionInput i : inputs) {
            if(i.UTXO == null) continue; //if Transaction can't be found skip it, This behavior may not be optimal.
            total += i.UTXO.value;
        }
        return total;
    }

    /**
     * 生成签名
     * @param privateKey 转账交易发起方的私钥
     */
    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value)	;
        // 通过转账交易发起方的私钥对数据发起签名
        signature = StringUtil.applyECDSASig(privateKey,data);
    }

    /**
     * 校验签名
     * @return
     */
    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value)	;
        // 通过转账交易发起方公钥和数据已经之前私钥签名进行验证该笔交易是否由发起方发起
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

    /**
     * 获取所有输出交易的总金额
     * @return
     */
    public float getOutputsValue() {
        float total = 0;
        for(TransactionOutput o : outputs) {
            total += o.value;
        }
        return total;
    }

    /**
     * 计算该笔交易对应的hash值
     * @return
     */
    private String calulateHash() {
        sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                        StringUtil.getStringFromKey(reciepient) +
                        Float.toString(value) + sequence
        );
    }
}
