package holauser.lea.holauser.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import holauser.lea.holauser.R;

public class PaymentActivity extends AppCompatActivity {

    @BindView(R.id.cardInput)
    CardInputWidget mCardInputWidget;

    private Card cardToSave;

    private static final String API_KEY = "pk_test_Se6oic42rRszkogQl8PuxDcT";
    private static final String API_SECRET_KEY = "sk_test_BaMCaFrl0msH3bkCA0UYImDu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.donateBtn)
    public void onDonateClick(View v) {
        cardToSave = mCardInputWidget.getCard();
        if (cardToSave == null) {
            Toast.makeText(PaymentActivity.this, getString(R.string.mercadopago_fail), Toast.LENGTH_SHORT).show();
        } else {
            Stripe stripe = new Stripe(this, API_KEY);
            stripe.createToken(cardToSave, new TokenCallback() {
                        public void onSuccess(Token token) {
                            createCharge(token.getId());
                        }
                        public void onError(Exception error) {
                            Toast.makeText(PaymentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        }
    }

    private void createCharge(final String token) {
        new CreateChargeTask().execute(token);
    }

    static class CreateChargeTask extends AsyncTask<String, Void, Integer> {

        protected Integer doInBackground(String... urls) {
            com.stripe.Stripe.apiKey = API_SECRET_KEY;

            Map<String, Object> params = new HashMap<>();
            params.put("amount", 999);
            params.put("currency", "usd");
            params.put("description", "Reiki Sound contribution");
            params.put("source", urls[0]);

            try {
                Charge.create(params);
            } catch (AuthenticationException e) {
                e.printStackTrace();
            } catch (InvalidRequestException e) {
                e.printStackTrace();
            } catch (APIConnectionException e) {
                e.printStackTrace();
            } catch (CardException e) {
                e.printStackTrace();
            } catch (APIException e) {
                e.printStackTrace();
            }

            return 0;
        }
    }
}
