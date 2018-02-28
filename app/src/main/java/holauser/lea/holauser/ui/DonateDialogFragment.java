package holauser.lea.holauser.ui;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.OnClick;
import holauser.lea.holauser.GlobalVars;
import holauser.lea.holauser.R;
import holauser.lea.holauser.language.LanguageStrategy;

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

        if (((GlobalVars) activity.getApplicationContext()).wasTranslated)
            translateDialog(view);

        return view;
    }

    private void translateDialog(View v) {
        LanguageStrategy tranlator = ((GlobalVars) activity.getApplicationContext()).languageStrategy;
        ((TextView) v.findViewById(R.id.tvTitle)).setText(tranlator.getString("donate_title"));
        ((TextView) v.findViewById(R.id.tvSubtitle)).setText(tranlator.getString("donate_subtitle"));
        ((TextView) v.findViewById(R.id.tvBody)).setText(tranlator.getString("donate_body"));
        ((TextView) v.findViewById(R.id.tvThanks)).setText(tranlator.getString("donate_thanks"));
        ((TextView) v.findViewById(R.id.btn_no)).setText(tranlator.getString("donate_button_not"));
        ((Button) v.findViewById(R.id.btn_ok)).setText(tranlator.getString("donate_button"));
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @OnClick(R.id.btn_ok)
    public void onOkClick(View v) {

        Uri uri = Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=GSUQNP7SN6AZ4");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

        dismiss();
    }

    @OnClick(R.id.btn_no)
    public void onNoClick(View v) {
        dismiss();
    }
}
