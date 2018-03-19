package one.chainclub.qybcoin.jsonrpcclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

public class QybcoinRawTxBuilder {
    public final Qybcoin bitcoin;

    public QybcoinRawTxBuilder(Qybcoin bitcoin) {
        this.bitcoin = bitcoin;
    }
    public LinkedHashSet<Qybcoin.TxInput> inputs = new LinkedHashSet();
    public List<Qybcoin.TxOutput> outputs = new ArrayList();

    private class Input extends Qybcoin.BasicTxInput {

        public Input(String txid, int vout) {
            super(txid, vout);
        }

        public Input(Qybcoin.TxInput copy) {
            this(copy.txid(), copy.vout());
        }

        @Override
        public int hashCode() {
            return txid.hashCode() + vout;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (!(obj instanceof Qybcoin.TxInput))
                return false;
            Qybcoin.TxInput other = (Qybcoin.TxInput) obj;
            return vout == other.vout() && txid.equals(other.txid());
        }

    }
    public QybcoinRawTxBuilder in(Qybcoin.TxInput in) {
        inputs.add(new Input(in.txid(), in.vout()));
        return this;
    }

    public QybcoinRawTxBuilder in(String txid, int vout) {
        in(new Qybcoin.BasicTxInput(txid, vout));
        return this;
    }

    public QybcoinRawTxBuilder out(String address, double amount) {
        if (amount <= 0d)
            return this;
        outputs.add(new Qybcoin.BasicTxOutput(address, amount));
        return this;
    }

    public QybcoinRawTxBuilder in(double value) throws QybcoinException {
        return in(value, 6);
    }

    public QybcoinRawTxBuilder in(double value, int minConf) throws QybcoinException {
        List<Qybcoin.Unspent> unspent = bitcoin.listUnspent(minConf);
        double v = value;
        for (Qybcoin.Unspent o : unspent) {
            if (!inputs.contains(new Input(o))) {
                in(o);
                v = QybcoinUtil.normalizeAmount(v - o.amount());
            }
            if (v < 0)
                break;
        }
        if (v > 0)
            throw new QybcoinException("Not enough bitcoins ("+v+"/"+value+")");
        return this;
    }

    private HashMap<String, Qybcoin.RawTransaction> txCache = new HashMap<String, Qybcoin.RawTransaction>();

    private Qybcoin.RawTransaction tx(String txId) throws QybcoinException {
        Qybcoin.RawTransaction tx = txCache.get(txId);
        if (tx != null)
            return tx;
        tx = bitcoin.getRawTransaction(txId);
        txCache.put(txId, tx);
        return tx;
    }

    public QybcoinRawTxBuilder outChange(String address) throws QybcoinException {
        return outChange(address, 0d);
    }

    public QybcoinRawTxBuilder outChange(String address, double fee) throws QybcoinException {
        double is = 0d;
        for (Qybcoin.TxInput i : inputs)
            is = QybcoinUtil.normalizeAmount(is + tx(i.txid()).vOut().get(i.vout()).value());
        double os = fee;
        for (Qybcoin.TxOutput o : outputs)
            os = QybcoinUtil.normalizeAmount(os + o.amount());
        if (os < is)
            out(address, QybcoinUtil.normalizeAmount(is - os));
        return this;
    }

    public String create() throws QybcoinException {
        return bitcoin.createRawTransaction(new ArrayList<Qybcoin.TxInput>(inputs), outputs);
    }

    public String sign() throws QybcoinException {
        return bitcoin.signRawTransaction(create());
    }

    public String send() throws QybcoinException {
        return bitcoin.sendRawTransaction(sign());
    }

}
