package one.chainclub.qybcoin.jsonrpcclient;

import one.chainclub.qybcoin.jsonrpcclient.Qybcoin.Transaction;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class ConfirmedPaymentListener extends SimpleQybcoinPaymentListener {

    public int minConf;

    public ConfirmedPaymentListener(int minConf) {
        this.minConf = minConf;
    }

    public ConfirmedPaymentListener() {
        this(6);
    }

    protected Set<String> processed = Collections.synchronizedSet(new HashSet<String>());

    protected boolean markProcess(String txId) {
        return processed.add(txId);
    }

    @Override
    public void transaction(Transaction transaction) {
        if (transaction.confirmations() < minConf)
            return;
        if (!markProcess(transaction.txId()))
            return;
        confirmed(transaction);
    }

    public abstract void confirmed(Transaction transaction);
}
