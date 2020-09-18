package Transaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chaina.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.klaytn.caver.transaction.type.TransactionType;
import com.klaytn.caver.utils.BytesUtils;
import com.samsung.android.sdk.coldwallet.ScwCoinType;
import com.samsung.android.sdk.coldwallet.ScwService;

import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.util.ArrayList;
import java.util.List;


public class ScanQR extends AppCompatActivity {

    private IntentIntegrator qrScan;

    ScwService scwServiceInstance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanqr);

        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
        qrScan.initiateScan();
        qrScan.setPrompt("QR 코드를 사각형 안에 맞춰주세요.");

        //키스토어 SDK 사용 부분
        scwServiceInstance = ScwService.getInstance();

        if(scwServiceInstance == null )
        {
            Toast.makeText(this, "삼성 블록체인 Keystore가 지원되지 않는 기기입니다. 다른 Keystore를 사용해야 합니다.", Toast.LENGTH_LONG).show();
            backToMainActivity();
        }

        if(!getSeedHash()){
            Toast.makeText(this, "지갑이 없습니다. Keystore로 이동하여 지갑을 생성하세요.", Toast.LENGTH_LONG).show();
            backToMainActivity();
            //TODO: 딥링크 구현하기 ScwDeepLink.MAIN
        }
        ScwService.ScwCheckForMandatoryAppUpdateCallback callback =
                new ScwService.ScwCheckForMandatoryAppUpdateCallback() {
                    @Override
                    public void onMandatoryAppUpdateNeeded(boolean needed) {
                        if(needed){
                            //TODO: 갤릭시 스토어로 딥링크 구현하기 startDeepLink(ScwDeepLink.GALAXY_STORE);
                        }
                    }
                };

        ScwService.getInstance().checkForMandatoryAppUpdate(callback);

        ScwService.ScwSignKlayTransactionCallback callback_transaction =
                new ScwService.ScwSignKlayTransactionCallback() {
                    @Override
                    public void onSuccess(byte[] signedKlayTransaction) {

                    }

                    @Override
                    public void onFailure(int errorCode, String errorMessage) {
                        //handle error

                    }
                };


        String hdPath =  ScwService.getHdPath(ScwCoinType.KLAY, 0);


        byte[] unSignedTransaction = getUnsignedTx();
        int klaytnChainId = 1001;


        ScwService.getInstance().signKlayTransaction(callback_transaction,  unSignedTransaction, hdPath, klaytnChainId);


    }

    private byte[] getUnsignedTx() {

        //SigRLP = encode([encode([type, nonce, gasPrice, gas, to, value, from, input]), chainid, 0, 0])
        // Create a value transfer transaction
        // Create a value transfer transaction



        List<RlpType> rlpTypeList = new ArrayList<>();
        rlpTypeList.add(RlpString.create(Numeric.toBigInt("0")));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt("0")));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt("0")));

        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray("0x88be13423f935c1ff453633e23c369275a9e0532")));
        rlpTypeList.add(RlpString.create(Numeric.toBigInt("0")));
        rlpTypeList.add(RlpString.create(Numeric.hexStringToByteArray("03")));

        byte[] encodedTransaction = RlpEncoder.encode(new RlpList(rlpTypeList));
        byte[] type = new byte[] { (byte)TransactionType.TxTypeValueTransfer.getType() };
        byte[] rawTx = BytesUtils.concat(type, encodedTransaction);
        return rawTx;
    }

    public boolean getKeystoreApiLevel() {
        int keystoreApiLevel = ScwService.getInstance().getKeystoreApiLevel();
        boolean isKeystoreApiSupported = keystoreApiLevel > 0;
        return isKeystoreApiSupported;
    }

    public boolean getSeedHash() {
        String seedHash = ScwService.getInstance().getSeedHash();
        boolean initialized =  (seedHash != null && seedHash.length() > 0);

        return initialized;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                // todo
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                // todo
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