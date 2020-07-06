package lekt09_recievers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;
import lekt03_diverse.VisNotifikation;

/**
 * Eksempel på en broadcast reciever
 *
 * @author Jacob Nordfalk
 */
public class OpdagAppInstallation extends AppCompatActivity implements OnClickListener {

  Button registrer, afregistrer;
  static InstallationsReciever reciever = new InstallationsReciever();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);
    TextView tv = new TextView(this);
    tv.setText("Broadcastreciever der opdager når der bliver installeret/afinstalleret apps");
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

  /**
   * @return filteret der opdager at pakker er blevet fjernet/tilføjet/opgraderet
   */
  public IntentFilter lavIntentFilter() {
    IntentFilter i = new IntentFilter();
    i.addCategory(Intent.CATEGORY_DEFAULT);
    i.addAction(Intent.ACTION_PACKAGE_CHANGED);
    i.addAction(Intent.ACTION_PACKAGE_REMOVED);
    i.addAction(Intent.ACTION_PACKAGE_ADDED);
    i.addAction(Intent.ACTION_PACKAGE_INSTALL);
    i.addAction(Intent.ACTION_PACKAGE_REPLACED);
    i.addDataScheme("package");
    return i;
  }

  public void onClick(View klikPåHvad) {
    if (klikPåHvad == registrer) {
      registerReceiver(reciever, lavIntentFilter());
      Toast.makeText(this, "Installer eller fjern nu en app", Toast.LENGTH_LONG).show();
    } else if (klikPåHvad == afregistrer) {
      try {
        unregisterReceiver(reciever);
        Toast.makeText(this, "Afregistreret", Toast.LENGTH_LONG).show();
      } catch (Exception e) {
        Toast.makeText(this, "Fejl: " + e, Toast.LENGTH_LONG).show();
      }
    }
  }

  public static class InstallationsReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context ctx, Intent i) {
      System.out.println("onReceive" + ctx + ":\n" + i);
      // I/System.out( 2870): Intent { act=android.intent.action.PACKAGE_REMOVED dat=package:dk.nordfalk.teoriproeve.ce flg=0x10000000 (has extras) }
      Toast.makeText(ctx, "onReceive" + ctx + ":\n" + i, Toast.LENGTH_LONG).show();

      // Vis også en notifikation så man kan komme hen og slå det fra
      Intent intent = new Intent(ctx, OpdagAppInstallation.class);
      PendingIntent aktivitet = PendingIntent.getActivity(ctx, 0, intent, 0);
      NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, VisNotifikation.opretNotifKanal(ctx))
              .setContentIntent(aktivitet)
              .setSmallIcon(R.drawable.bil)
              .setTicker("Installation")
              .setContentTitle(i.getAction())
              .setContentText(""+i.getExtras());

      NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
      notificationManager.notify(42, builder.build());

    }
  }
}
