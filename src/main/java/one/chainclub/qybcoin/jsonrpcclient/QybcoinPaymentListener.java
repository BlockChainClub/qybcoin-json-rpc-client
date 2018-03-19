package one.chainclub.qybcoin.jsonrpcclient;

import one.chainclub.qybcoin.jsonrpcclient.Qybcoin.Transaction;

public interface QybcoinPaymentListener {

    public void block(String blockHash);

    public void transaction(Transaction transaction);
}
