package Transaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chaina.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.type.ValueTransfer;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import com.samsung.android.sdk.coldwallet.ScwService;

import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Arrays;


public class ScanQR extends AppCompatActivity {

    private IntentIntegrator qrScan;

    ScwService scwServiceInstance;
    private static final String ABIJson = "[{\"constant\":true,\"inputs\":[{\"name\":\"key\",\"type\":\"string\"}],\"name\":\"get\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"key\",\"type\":\"string\"},{\"name\":\"value\",\"type\":\"string\"}],\"name\":\"set\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]\n";


    Button sign;
    Button smart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanqr);
        if (android.os.Build.VERSION.SDK_INT > 9) { StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); StrictMode.setThreadPolicy(policy); }



        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
        qrScan.initiateScan();
        qrScan.setPrompt("QR 코드를 사각형 안에 맞춰주세요.");

        sign = findViewById(R.id.signtransaction);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    signTransaction();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        smart = findViewById(R.id.smart);

        smart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeContractFunction();
            }
        });
    }
    public void signTransaction() throws IOException {
        Caver caver = new Caver(Caver.BAOBAB_URL);
// Add a keyring to caver.wallet
        SingleKeyring keyring = KeyringFactory.createFromPrivateKey("0x6fcc47c6988c2f1ef5b7e65121215d26a1d9483d1b46a18e4841d07d9a233df1");
        caver.wallet.add(keyring);

// Create a value transfer transaction
        ValueTransfer valueTransfer = new ValueTransfer.Builder()
                .setKlaytnCall(caver.rpc.klay)
                .setFrom(keyring.getAddress())
                .setTo("0xc80b031986e54a596460c5ffb0a460a41e40db60") //0x07638a88d529c964d5ef5b6242fe11431abdc0ca
                .setValue(BigInteger.valueOf(1))
                .setGas(BigInteger.valueOf(30000))
                .build();

// Sign the transaction via caver.wallet.sign


        caver.wallet.sign(keyring.getAddress(), valueTransfer);

        String rlpEncoded = valueTransfer.getRLPEncoding();
        String txHash = null;
        System.out.println("RLP-encoded string: " + rlpEncoded);
        Bytes32 sendResult = caver.rpc.klay.sendRawTransaction(rlpEncoded).send();
        if (sendResult.hasError()) {
            //do something to handle error
        }
        txHash = sendResult.getResult();

        System.out.println("Txhash: " + txHash);
    }


    public void executeContractFunction() {
        Caver caver = new Caver(Caver.BAOBAB_URL);
        SingleKeyring executor = KeyringFactory.createFromPrivateKey("0x6fcc47c6988c2f1ef5b7e65121215d26a1d9483d1b46a18e4841d07d9a233df1");
        caver.wallet.add(executor);

        try {
            Contract contract = new Contract(caver, ABIJson, "0x07638a88d529c964d5ef5b6242fe11431abdc0ca");

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(executor.getAddress());
            sendOptions.setGas(BigInteger.valueOf(4000000));

            //여기서 함수 이름 설정
            TransactionReceipt.TransactionReceiptData receipt = contract.getMethod("set").send(Arrays.asList("test","testValue"), sendOptions);
        } catch (IOException | TransactionException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            //handle exception..
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                backToMainActivity();// todo
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void backToMainActivity()
    {
        finish();
    }
}