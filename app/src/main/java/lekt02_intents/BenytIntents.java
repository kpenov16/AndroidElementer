package lekt02_intents;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Jacob Nordfalk
 */
public class BenytIntents extends AppCompatActivity implements OnClickListener {
  EditText tekstfelt, nummerfelt;
  Button ringOp, sendSms, delApp, sendEpost, webadresse, wifiIndstillinger;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    tekstfelt = new EditText(this);
    tekstfelt.setText("Skriv tekst her");
    tl.addView(tekstfelt);

    nummerfelt = new EditText(this);
    nummerfelt.setHint("evt telefonnummer her");
    nummerfelt.setInputType(InputType.TYPE_CLASS_PHONE);
    tl.addView(nummerfelt);

    ringOp = new Button(this);
    ringOp.setText("Ring op");
    tl.addView(ringOp);

    sendSms = new Button(this);
    sendSms.setText("Åbn send SMS");
    tl.addView(sendSms);

    sendEpost = new Button(this);
    sendEpost.setText("Åbn og send epostbesked");
    tl.addView(sendEpost);

    delApp = new Button(this);
    delApp.setText("Del app...");
    tl.addView(delApp);

    wifiIndstillinger = new Button(this);
    wifiIndstillinger.setText("Åbn indstillinger (f.eks. wifi)");
    tl.addView(wifiIndstillinger);

    webadresse = new Button(this);
    webadresse.setText("Webadresse (om intents)");
    tl.addView(webadresse);

    TextView tv = new TextView(this);
    tv.setText("Man kan også bruge klassen Linkify til at putte intents ind i tekst.\n" + "Mit telefonnummer er 26206512, min e-post er jacob.nordfalk@gmail.com og " + "jeg har en hjemmeside på https://javabog.dk.");
    Linkify.addLinks(tv, Linkify.ALL);
    tl.addView(tv);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);


    ringOp.setOnClickListener(this);
    sendSms.setOnClickListener(this);
    delApp.setOnClickListener(this);
    sendEpost.setOnClickListener(this);
    webadresse.setOnClickListener(this);
    wifiIndstillinger.setOnClickListener(this);
  }


  /**
   * Ofte har man som udvikler brug for info om den telefon brugeren har.
   * Denne metode giver telefonmodel, Androidversion og programversion etc.
   */
  public String lavTelefoninfo() throws Exception {
    PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
    return "\nProgram: " + getPackageName() + " version " + pi.versionName
            + "\nTelefonmodel: " + Build.MODEL + "\n" + Build.PRODUCT
            + "\nAndroid version " + Build.VERSION.RELEASE + "\nsdk: " + Build.VERSION.SDK_INT;
  }


  public void onClick(View klikPåHvad) {
    String nummer = nummerfelt.getText().toString();
    String tekst = tekstfelt.getText().toString();
    nummerfelt.setError(null);
    if (nummer.length() == 0 && klikPåHvad == ringOp) {
      nummerfelt.setError("Skriv et telefonnummer");
      Toast.makeText(this, "Skriv et telefonnummer", Toast.LENGTH_LONG).show();
      return;
    }

    try {
      if (klikPåHvad == ringOp) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + nummer));
        startActivity(intent);
        // eller blot:
        //     startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+nummer)));
      } else if (klikPåHvad == sendSms) {
        // Åbner et SMS-vindue og lader brugeren sende SMS'en
        // Kilde: https://developer.android.com/guide/components/intents-common.html#Messaging
        tekst = tekst + lavTelefoninfo();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:"+nummer));
        intent.putExtra("sms_body", tekst);
        startActivity(intent);
      } else if (klikPåHvad == sendEpost) {
        tekst = tekst + lavTelefoninfo();
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{tekst});
        i.putExtra(Intent.EXTRA_SUBJECT, nummer);
        i.putExtra(Intent.EXTRA_TEXT, lavTelefoninfo());
        i.putExtra(Intent.EXTRA_CC, new String[]{"jacob.nordfalk@gmail.com"});
        startActivity(Intent.createChooser(i, "Send e-post..."));
      } else if (klikPåHvad == delApp) {
        Intent i = new Intent(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_SUBJECT, "Prøv AndroidElementer")
                .putExtra(Intent.EXTRA_TEXT, "Hej!\n\n" +
                "Hvis du programmerer til Android så prøv denne her eksempelsamling\n" +
                "AndroidElementer\n" +
                "https://play.google.com/store/apps/details?id=dk.nordfalk.android.elementer").setType("text/plain");
        startActivity(Intent.createChooser(i, "Del via"));
      } else if (klikPåHvad == wifiIndstillinger) {
        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
      } else if (klikPåHvad == webadresse) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com/guide/components/intents-common.html"));
        startActivity(intent);
      }
    } catch (Exception e) {
      Toast.makeText(this, "Denne telefon mangler en funktion:\n" + e.getMessage(), Toast.LENGTH_LONG).show();
      e.printStackTrace();
    }
  }
}
