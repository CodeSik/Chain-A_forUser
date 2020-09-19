package Transaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chaina.MainActivity;
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

import GPSInfo.GPS;
import GPSInfo.GPSDao;
import GPSInfo.GPSDatabase;


public class ScanQR extends AppCompatActivity {
    GPSDatabase db;
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
    String sendResult="{";
    String address_string;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanqr);
        db = GPSDatabase.getAppDatabase(this);
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
                new GetAsyncTask(db.gpsDao()).execute(sendResult);
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
            Contract contract = new Contract(caver, ABIJson, "0xd163eb0e3e0b6076c191b33d68d6dee5f3dfce8f");

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

    //메인스레드에서 데이터베이스에 접근할 수 없으므로 AsyncTask를 사용하도록 한다.
    public class GetAsyncTask extends AsyncTask<String, Void, Void> {
        private GPSDao mGpsDao;

        private String result;
        public  GetAsyncTask(GPSDao mGpsDao){
            this.mGpsDao = mGpsDao;
        }

        ProgressDialog asyncDialog = new ProgressDialog(ScanQR.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            asyncDialog.setMessage("전송 중 입니다..");

            asyncDialog.show();


            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            //추가만하고 따로 SELECT문을 안해도 라이브데이터로 인해
            //getAll()이 반응해서 데이터를 갱신해서 보여줄 것이다,  메인액티비티에 옵저버에 쓴 코드가 실행된다. (라이브데이터는 스스로 백그라운드로 처리해준다.)
            for (int i = 0; i < 5; i++) {

                asyncDialog.setProgress(i * 30);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }


            GPS[] gpsList = mGpsDao.getAll();
            for(int i=0 ; i < gpsList.length ; i++)
            {
                strings[0] += ",{";

                strings[0] += "\"lat\":\"";
                strings[0] += Double.toString(gpsList[i].getLatitude());
                strings[0] += "\",";

                strings[0] += "\"lng\":\"";
                strings[0] += Double.toString(gpsList[i].getLongitude());
                strings[0] += "\",";

                strings[0] += "\"addressName\":\"";
                strings[0] += " ";
                strings[0] += "\",";

                strings[0] += "\"companyName\":\"";
                strings[0] += " ";
                strings[0] += "\",";

                strings[0] += "\"time\":\"";
                strings[0] += gpsList[i].getTime();
                strings[0] += "\"}";
            }
            strings[0] += "}";
            this.result=strings[0];

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
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            asyncDialog.dismiss();
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
                sendResult +=result.getContents();


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