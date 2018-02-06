package holauser.lea.holauser.ui;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mercadopago.core.MercadoPago;

import java.math.BigDecimal;

import butterknife.ButterKnife;
import butterknife.OnClick;
import holauser.lea.holauser.R;

/**
 * Created by leandro on 6/2/18.
 */

public class AmmountDonationDialogFragment extends DialogFragment {

    private Activity activity;

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
        BigDecimal decimal = BigDecimal.valueOf(ammount);

        new MercadoPago.StartActivityBuilder()
                .setActivity(activity)
                .setAmount(decimal)
                .setPublicKey(activity.getString(R.string.mercadopago_public_key))
                .startCardVaultActivity();
        dismiss();
    }
}
