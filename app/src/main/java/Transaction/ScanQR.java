package Transaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Arrays;

import Main.MainActivity;


public class ScanQR extends AppCompatActivity {

    private IntentIntegrator qrScan;

    ScwService scwServiceInstance;
    private static final String ABIJson = "[\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"count\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"uint256\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [],\n" +
            "\t\t\"name\": \"getCurrentGPSInfo\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": false,\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"_personalInfo\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"insertInformation\",\n" +
            "\t\t\"outputs\": [],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"nonpayable\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"constant\": true,\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"address\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"gpsContainer\",\n" +
            "\t\t\"outputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"payable\": false,\n" +
            "\t\t\"stateMutability\": \"view\",\n" +
            "\t\t\"type\": \"function\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"anonymous\": false,\n" +
            "\t\t\"inputs\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"indexed\": true,\n" +
            "\t\t\t\t\"name\": \"_sender\",\n" +
            "\t\t\t\t\"type\": \"address\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"indexed\": false,\n" +
            "\t\t\t\t\"name\": \"_personalInfo\",\n" +
            "\t\t\t\t\"type\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"name\": \"GPSInsertion\",\n" +
            "\t\t\"type\": \"event\"\n" +
            "\t}\n" +
            "]";


    TextView address;
    Button smart;
    String sendResult;
    String address_string;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanqr);

        address = findViewById(R.id.address_text);
        smart = findViewById(R.id.smart);


        if (android.os.Build.VERSION.SDK_INT > 9) { StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); StrictMode.setThreadPolicy(policy); }

        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
        qrScan.initiateScan();
        qrScan.setPrompt("QR 코드를 사각형 안에 맞춰주세요.");



        smart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeContractFunction(sendResult);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
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

    public String jsonParsing(String result) throws JSONException {
        JSONObject jObject = new JSONObject(result);

        String address = jObject.getString("addressName");

        return address;
    }
    public void executeContractFunction(String result) {
        Caver caver = new Caver(Caver.BAOBAB_URL);
        SingleKeyring executor = KeyringFactory.createFromPrivateKey("0x6fcc47c6988c2f1ef5b7e65121215d26a1d9483d1b46a18e4841d07d9a233df1");
        caver.wallet.add(executor);

        try {
            Contract contract = new Contract(caver, ABIJson, "0x07638a88d529c964d5ef5b6242fe11431abdc0ca");

            SendOptions sendOptions = new SendOptions();
            sendOptions.setFrom(executor.getAddress());
            sendOptions.setGas(BigInteger.valueOf(4000000));

            Log.d("result",result);
            //여기서 함수 이름 설정
            TransactionReceipt.TransactionReceiptData receipt = contract.getMethod("insertInformation").send(Arrays.asList(result), sendOptions);
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
                sendResult = result.getContents();
                try {
                    address_string = jsonParsing(sendResult);
                    address.setText(address_string);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


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