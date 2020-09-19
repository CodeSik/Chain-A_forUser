package Transaction;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chaina.R;
import com.klaytn.caver.Caver;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.Quantity;
import com.klaytn.caver.transaction.type.ValueTransfer;
import com.klaytn.caver.wallet.keyring.KeyringFactory;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import com.samsung.android.sdk.coldwallet.ScwCoinType;
import com.samsung.android.sdk.coldwallet.ScwService;

import org.web3j.protocol.core.Request;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;

public class SignTransaction extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_transaction);
    }




}