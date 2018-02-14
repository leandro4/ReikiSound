package holauser.lea.holauser.ui;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paypal.android.sdk.payments.PayPalConfiguration;

import butterknife.ButterKnife;
import butterknife.OnClick;
import holauser.lea.holauser.R;
import holauser.lea.holauser.Utils;

/**
 * Created by leandro on 6/2/18.
 */

public class AmmountDonationDialogFragment extends DialogFragment {

    private Activity activity;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Utils.PAYPAL_CLIENT_ID_LIVE);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_ammount_to_donate, container,false);
        ButterKnife.bind(this,view);

        try {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return view;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @OnClick(R.id.btn_1)
    public void on1Click(View v) {
        donate(1);
    }

    @OnClick(R.id.btn_5)
    public void on5Click(View v) {
        donate(5);
    }

    @OnClick(R.id.btn_20)
    public void on20Click(View v) {
        donate(20);
    }

    @OnClick(R.id.btn_50)
    public void on50Click(View v) {
        donate(50);
    }

    private void donate(int ammount) {

        activity.startActivity(new Intent(activity, PaymentActivity.class));

//        BigDecimal count = new BigDecimal(ammount);
//        Intent serviceConfig = new Intent(activity, PayPalService.class);
//        serviceConfig.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//        activity.startService(serviceConfig);
//
//        PayPalPayment payment = new PayPalPayment(count, "USD", "ReikiSound contribution", PayPalPayment.PAYMENT_INTENT_SALE);
//
//        Intent paymentConfig = new Intent(activity, PaymentActivity.class);
//        paymentConfig.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//        paymentConfig.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
//        activity.startActivityForResult(paymentConfig, PAYPAL_PAYMENT);

        dismiss();
    }
}
