package one.chainclub.qybcoin.jsonrpcclient;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface Qybcoin {

    //addmultisigaddress
    public static enum AddNodeAction {
        ADD,
        REMOVE,
        ONETRY
    }

    public void addNode(String nodeAddress, AddNodeAction action) throws QybcoinException;

    //backupwallet
    public static interface TxInput {
        public String txid();
        public int vout();
    }

    public static class BasicTxInput implements TxInput{
        public String txid;
        public int vout;

        public BasicTxInput(String txid, int vout) {
            this.txid = txid;
            this.vout = vout;
        }

        public String txid() {
            return txid;
        }

        public int vout() {
            return vout;
        }

    }

    public static interface TxOutput {
        public String address();
        public double amount();
    }

    public static class BasicTxOutput implements TxOutput{
        public String address;
        public double amount;

        public BasicTxOutput(String address, double amount) {
            this.address = address;
            this.amount = amount;
        }

        public String address() {
            return address;
        }

        public double amount() {
            return amount;
        }
    }

    /**
     * Use QybcoinRawTxBuilder , which is more convenient
     * @param inputs
     * @param outputs
     * @return
     * @throws QybcoinException
     */
    public String createRawTransaction(List<TxInput> inputs, List<TxOutput> outputs) throws QybcoinException;

    public RawTransaction decodeRawTransaction(String hex) throws QybcoinException;

    public String dumpPrivKey(String address) throws QybcoinException;

    //encryptwallet

    public String getAccount(String address) throws QybcoinException;

    public String getAccountAddress(String account) throws QybcoinException;

    //getaddednodeinfo

    public List<String> getAddressesByAccount(String account) throws QybcoinException;

    /**
     *
     * @return returns the server's total available balance
     * @throws QybcoinException
     */
    public double getBalance() throws QybcoinException;

    /**
     *
     * @param account
     * @return returns the balance in the account
     * @throws QybcoinException
     */
    public double getBalance(String account) throws QybcoinException;

    /**
     *
     * @param account
     * @param minConf
     * @return returns the balance in the account
     * @throws QybcoinException
     */
    public double getBalance(String account, int minConf) throws QybcoinException;

    public static interface Block {
        public String hash();
        public int confirmations();
        public int size();
        public int height();
        public int version();
        public String merkleRoot();
        public List<String> tx();
        public Date time();
        public long nonce();
        public String bits();
        public double difficulty();
        public String previousHash();
        public String nextHash();
        public Block previous() throws QybcoinException;
        public Block next() throws QybcoinException;
    }

    public Block getBlock(String blockHash) throws QybcoinException;

    public int getBlockCount() throws QybcoinException;

    public String getBlockHash(int blockId) throws QybcoinException;

    //getblocknumber - deprecated

    public int getConnectionCount() throws QybcoinException;

    public double getDifficulty() throws QybcoinException;

    public boolean getGenerate() throws QybcoinException;

    public double getHashesPerSec() throws QybcoinException;

    @Deprecated
    public static interface Info {
        public int version();
        public int protocolVersion();
        public int walletVersion();
        public double balance();
        public int blocks();
        public int timeOffset();
        public int connections();
        public String proxy();
        public double difficulty();
        public boolean testnet();
        public int keyPoolOldest();
        public int keyPoolSize();
        public int unlockedUntil();
        public double payTxFee();
        public double relayFee();
        public String errors();
    }

    @Deprecated
    public Info getInfo() throws QybcoinException;

    //getmemorypool

    public static interface MiningInfo {
        public int blocks();
        public int currentBlockSize();
        public int currentBlockTx();
        public double difficulty();
        public String errors();
        public int genProcLimit();
        public double networkHashPs();
        public int pooledTx();
        public boolean testnet();
        public String chain();
        public boolean generate();
    }
    public MiningInfo getMiningInfo() throws QybcoinException;

    //getnewaddress
    public String getNewAddress() throws QybcoinException;
    public String getNewAddress(String account) throws QybcoinException;

    public static interface PeerInfo {
        public String addr();
        public String services();
        public int lastSend();
        public int lastRecv();
        public int bytesSent();
        public int bytesRecv();
        public int blocksRequested();
        public Date connTime();
        public int version();
        public String subver();
        public boolean inbound();
        public int startingHeight();
        public int banScore();
    }
    public PeerInfo getPeerInfo() throws QybcoinException;

    //getrawmempool

    public String getRawTransactionHex(String txId) throws QybcoinException;

    public interface RawTransaction {
        public String hex();
        public String txId();
        public int version();
        public long lockTime();
        /*
         *
         */
        public interface In extends TxInput {
            public Map<String, Object> scriptSig();
            public long sequence();

            public RawTransaction getTransaction();
            public Out getTransactionOutput();
        }

        /**
         * This method should be replaced someday
         */
        public List<In> vIn(); // TODO : Create special interface instead of this

        public interface Out {
            public double value();
            public int n();

            public interface ScriptPubKey {
                public String asm();
                public String hex();
                public int reqSigs();
                public String type();
                public List<String> addresses();
            }

            public ScriptPubKey scriptPubKey();

            public TxInput toInput();

            public RawTransaction transaction();

        }

        /**
         * This method should be replaced someday
         */
        public List<Out> vOut(); // TODO : Create special interface instead of this
        public String blockHash();
        public int confirmations();
        public Date time();
        public Date blocktime();
    }

    public RawTransaction getRawTransaction(String txId) throws QybcoinException;

    public double getReceivedByAccount(String account) throws QybcoinException;

    /**
     * Returns the total amount received by &lt;account&gt; in transactions with at least [minconf] confirmations. While some might
     * consider this obvious, value reported by this only considers *receiving* transactions. It does not check payments that have been made
     * *from* this account. In other words, this is not "getaddressbalance". Works only for addresses in the local wallet, external
     * addresses will always show 0.
     *
     * @param account
     * @param minConf
     * @return the total amount received by &lt;account&gt;
     */
    public double getReceivedByAccount(String account, int minConf) throws QybcoinException;

    public double getReceivedByAddress(String address) throws QybcoinException;

    /**
     * Returns the total amount received by &lt;bitcoinaddress&gt; in transactions with at least [minconf] confirmations. While some might
     * consider this obvious, value reported by this only considers *receiving* transactions. It does not check payments that have been made
     * *from* this address. In other words, this is not "getaddressbalance". Works only for addresses in the local wallet, external
     * addresses will always show 0.
     *
     * @param address
     * @param minConf
     * @return the total amount received by &lt;bitcoinaddress&gt;
     */
    public double getReceivedByAddress(String address, int minConf) throws QybcoinException;

    public RawTransaction getTransaction(String txId) throws QybcoinException;

    public static interface TxOutSetInfo {
        public int height();
        public String bestBlock();
        public int transactions();
        public int txOuts();
        public int bytesSerialized();
        public String hashSerialized();
        public double totalAmount();
    }
    public TxOutSetInfo getTxOutSetInfo() throws QybcoinException;

    public static interface Work {
        public String midstate();
        public String data();
        public String hash1();
        public String target();
    }
    public Work getWork() throws QybcoinException;

    //help

    public void importPrivKey(String bitcoinPrivKey) throws QybcoinException;
    public void importPrivKey(String bitcoinPrivKey, String label) throws QybcoinException;
    public void importPrivKey(String bitcoinPrivKey, String label, boolean rescan) throws QybcoinException;

    //keypoolrefill

    /**
     * listaccounts [minconf=1]
     *
     * @return Map that has account names as keys, account balances as values
     * @throws QybcoinException
     */
    public Map<String, Number> listAccounts() throws QybcoinException;
    public Map<String, Number> listAccounts(int minConf) throws QybcoinException;

    public static interface ReceiveAddress {

        public String address();
        public double balance();
        public String account();

    }

    public List<List<ReceiveAddress>> listAddressGroupings() throws QybcoinException;

    public List<ReceivedAddress> listReceivedByAccount() throws QybcoinException;
    public List<ReceivedAddress> listReceivedByAccount(int minConf) throws QybcoinException;
    public List<ReceivedAddress> listReceivedByAccount(int minConf, boolean includeEmpty) throws QybcoinException;

    public static interface ReceivedAddress {
        public String address();
        public String account();
        public double amount();
        public int confirmations();
    }

    public List<ReceivedAddress> listReceivedByAddress() throws QybcoinException;
    public List<ReceivedAddress> listReceivedByAddress(int minConf) throws QybcoinException;
    public List<ReceivedAddress> listReceivedByAddress(int minConf, boolean includeEmpty) throws QybcoinException;

    /**
     * returned by listsinceblock and  listtransactions
     */
    public static interface Transaction {
        public String account();
        public String address();
        public String category();
        public double amount();
        public double fee();
        public int confirmations();
        public String blockHash();
        public int blockIndex();
        public Date blockTime();
        public String txId();
        public Date time();
        public Date timeReceived();
        public String comment();
        public String commentTo();

        public RawTransaction raw();
    }

    //listsinceblock
    public static interface TransactionsSinceBlock {
        public List<Transaction> transactions();
        public String lastBlock();
    }

    public TransactionsSinceBlock listSinceBlock() throws QybcoinException;
    public TransactionsSinceBlock listSinceBlock(String blockHash) throws QybcoinException;
    public TransactionsSinceBlock listSinceBlock(String blockHash, int targetConfirmations) throws QybcoinException;

    //listtransactions
    public List<Transaction> listTransactions() throws QybcoinException;
    public List<Transaction> listTransactions(String account) throws QybcoinException;
    public List<Transaction> listTransactions(String account, int count) throws QybcoinException;
    public List<Transaction> listTransactions(String account, int count, int from) throws QybcoinException;

    public interface Unspent extends TxInput, TxOutput {
        public String txid();
        public int vout();
        public String address();
        public String account();
        public String scriptPubKey();
        public double amount();
        public int confirmations();
    }

    public List<Unspent> listUnspent() throws QybcoinException;
    public List<Unspent> listUnspent(int minConf) throws QybcoinException;
    public List<Unspent> listUnspent(int minConf, int maxConf) throws QybcoinException;
    public List<Unspent> listUnspent(int minConf, int maxConf, String... addresses) throws QybcoinException;

    //listlockunspent

    //lockunspent

    //move

    public String sendFrom(String fromAccount, String toQybcoinAddress, double amount) throws QybcoinException;
    public String sendFrom(String fromAccount, String toQybcoinAddress, double amount, int minConf) throws QybcoinException;
    public String sendFrom(String fromAccount, String toQybcoinAddress, double amount, int minConf, String comment) throws QybcoinException;
    /**
     * Will send the given amount to the given address, ensuring the account has a valid balance using minConf confirmations.
     * @param fromAccount
     * @param toQybcoinAddress
     * @param amount is a real and is rounded to 8 decimal places
     * @param minConf
     * @return the transaction ID if successful
     * @throws QybcoinException
     */
    public String sendFrom(String fromAccount, String toQybcoinAddress, double amount, int minConf, String comment, String commentTo) throws QybcoinException;

    public String sendMany(String fromAccount, List<TxOutput> outputs) throws QybcoinException;
    public String sendMany(String fromAccount, List<TxOutput> outputs, int minConf) throws QybcoinException;
    public String sendMany(String fromAccount, List<TxOutput> outputs, int minConf, String comment) throws QybcoinException;

    public String sendRawTransaction(String hex) throws QybcoinException;

    public String sendToAddress(String toAddress, double amount) throws QybcoinException;
    public String sendToAddress(String toAddress, double amount, String comment) throws QybcoinException;
    /**
     *
     * @param toAddress
     * @param amount is a real and is rounded to 8 decimal places
     * @param comment
     * @param commentTo
     * @return the transaction ID &lt;txid&gt; if successful
     * @throws QybcoinException
     */
    public String sendToAddress(String toAddress, double amount, String comment, String commentTo) throws QybcoinException;

    //setaccount

    //setgenerate

    public String signMessage(String address, String message) throws QybcoinException;

    public String signRawTransaction(String hex) throws QybcoinException;

    //settxfee

    public void stop() throws QybcoinException;

    public static interface AddressValidationResult {
        public boolean isValid();
        public String address();
        public boolean isMine();
        public boolean isScript();
        public String pubKey();
        public boolean isCompressed();
        public String account();
    }

    public AddressValidationResult validateAddress(String address) throws QybcoinException;

    public boolean verifyMessage(String address, String signature, String message) throws QybcoinException;

    //walletlock

    //walletpassphrase

    //walletpassphrasechange

}
