package holauser.lea.holauser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    float alpha = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.contentLayout);

    }

    @Override
    protected void onStart () {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void sonarCuenco (View v) {
        Toast.makeText(this, "Sonando cuenco...", Toast.LENGTH_SHORT).show();
        final GlobalVars globalVariable = (GlobalVars) getApplicationContext();
        globalVariable.setSonido(R.raw.cuenco);
        globalVariable.setTiempo(Integer.parseInt(((EditText) findViewById(R.id.cuadro_segundos)).getText().toString()));
        startService(new Intent(MainActivity.this, SoundService.class));
    }

    public void sonarKoshi (View v) {
        Toast.makeText(this, "Sonando koshi...", Toast.LENGTH_SHORT).show();
        final GlobalVars globalVariable = (GlobalVars) getApplicationContext();
        globalVariable.setSonido(R.raw.koshi);
        globalVariable.setTiempo(Integer.parseInt(((EditText) findViewById(R.id.cuadro_segundos)).getText().toString()));
        startService(new Intent(MainActivity.this, SoundService.class));
    }

    public void detener (View v) {
        stopService(new Intent(MainActivity.this, SoundService.class));
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        stopService(new Intent(MainActivity.this, SoundService.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
