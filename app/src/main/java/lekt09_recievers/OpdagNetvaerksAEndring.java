package lekt09_recievers;

import androidx.appcompat.app.AppCompatActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Jacob Nordfalk
 */
public class OpdagNetvaerksAEndring extends AppCompatActivity implements OnClickListener {

  static BroadcastReceiver reciever = new BroadcastReceiver() {

    @Override
    public void onReceive(Context context, Intent intent) {
      Log.d("xxx", this + " " + intent);
      Toast.makeText(context, this + " " + intent, Toast.LENGTH_LONG).show();
      // Se http://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html
      // for flere muligheder
    }
  };
  Button registrer, afregistrer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


    TableLayout tl = new TableLayout(this);
    TextView tv = new TextView(this);
    tv.setText("Broadcastreciever der opdager når netværkskonnektivitet ændrer sig");
    tl.addView(tv);

    registrer = new Button(this);
    registrer.setText("Registrer reciever");
    tl.addView(registrer);

    afregistrer = new Button(this);
    afregistrer.setText("Afregistrer reciever");
    tl.addView(afregistrer);

    registrer.setOnClickListener(this);
    afregistrer.setOnClickListener(this);

    setContentView(tl);
  }

  public void onClick(View klikPåHvad) {
    if (klikPåHvad == registrer) {
      IntentFilter intentFilter = new IntentFilter();
      intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
      intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
      registerReceiver(reciever, intentFilter);
      Toast.makeText(this, "Registreret", Toast.LENGTH_LONG).show();
    } else if (klikPåHvad == afregistrer) {
      try {
        unregisterReceiver(reciever);
        Toast.makeText(this, "Afregistreret", Toast.LENGTH_LONG).show();
      } catch (Exception e) {
        Toast.makeText(this, "Fejl: " + e, Toast.LENGTH_LONG).show();
      }
    }
  }
}
