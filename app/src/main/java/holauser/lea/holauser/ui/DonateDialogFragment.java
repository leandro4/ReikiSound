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

import butterknife.ButterKnife;
import butterknife.OnClick;
import holauser.lea.holauser.R;

/**
 * Created by leandro on 6/2/18.
 */

public class DonateDialogFragment extends DialogFragment {

    private Activity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_donate, container,false);
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

    @OnClick(R.id.btn_ok)
    public void onOkClick(View v) {

        AmmountDonationDialogFragment dialog = new AmmountDonationDialogFragment();
        dialog.setActivity(activity);
        dialog.show(getFragmentManager(), "donate_choose");

        dismiss();
    }

    @OnClick(R.id.btn_no)
    public void onNoClick(View v) {
        dismiss();
    }
}
