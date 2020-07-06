package lekt06_asynkron;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;

import dk.nordfalk.android.elementer.R;


/**
 * Dette eksempel viser en AsyncTask der er knyttet korrekt til en aktivitet.
 * Hvis skærmen vendes knyttes AsyncTask'en korrekt til næste aktivitet og
 * fortsætter med at fungere der.
 * Se også diskussionen på
 * http://groups.google.com/group/android-developers/browse_thread/thread/e1d5b8f8a3142892
 */
public class Asynk4AsyncTaskStatic extends AppCompatActivity implements OnClickListener {

  ProgressBar progressBar;
  Button knap, annullerknap;

  static Asynk4AsyncTaskStatic synligAktivitet;
  static AsyncTaskUdskifteligAktivitet asyncTask;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    EditText editText = new EditText(this);
    editText.setText("Prøv at redigere her efter du har trykket på knapperne");
    editText.setId(R.id.editText); // Giv viewet et ID så dets indhold overlever en skærmvending
    tl.addView(editText);

    progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
    progressBar.setMax(99);
    editText.setId(R.id.enKnap); // Giv viewet et ID så dets indhold overlever en skærmvending
    tl.addView(progressBar);

    knap = new Button(this);
    knap.setText("AsyncTask med løbende opdatering og resultat");
    tl.addView(knap);

    annullerknap = new Button(this);
    annullerknap.setText("Annullér AsyncTask");
    annullerknap.setVisibility(View.GONE); // Skjul knappen
    tl.addView(annullerknap);

    setContentView(tl);

    knap.setOnClickListener(this);
    annullerknap.setOnClickListener(this);

    synligAktivitet = this;

    // Hvis der er sket en konfigurationsændring så kan det være vi har en gammel
    // asynctask som vi skal genbruge
    if (asyncTask != null) {
      knap.setEnabled(false);
      annullerknap.setVisibility(View.VISIBLE);
    }
  }

  @Override
  protected void onDestroy() {
    synligAktivitet = null; // Vigtigt, ellers bliver aktivitetsinstansen hængende i hukommelsen
    super.onDestroy();
  }


  public void onClick(View klikPåHvad) {

    if (klikPåHvad == knap) {
      asyncTask = new AsyncTaskUdskifteligAktivitet();
      asyncTask.execute(500, 50);
      knap.setEnabled(false);
      annullerknap.setVisibility(View.VISIBLE);
    } else if (klikPåHvad == annullerknap) {
      asyncTask.cancel(false);
    }
  }

  /**
   * en AsyncTask hvor input er int, progress er double, resultat er String
   */
  static class AsyncTaskUdskifteligAktivitet extends AsyncTask {
    int antalSkridt = 500;
    int ventPrSkridtMs = 50;
    double procent;
    double resttidISekunder;

    @Override
    protected Object doInBackground(Object[] objects) {
      for (int i = 0; i < antalSkridt; i++) {
        SystemClock.sleep(ventPrSkridtMs);
        if (isCancelled()) {
          return null; // stop uden resultat
        }
        procent = i * 100.0 / antalSkridt;
        resttidISekunder = (antalSkridt - i) * ventPrSkridtMs / 100 / 10.0;
        publishProgress(procent, resttidISekunder); // sendes som parameter til onProgressUpdate()
      }
      return "færdig med doInBackground()!"; // resultat (String) sendes til onPostExecute()
    }

    @Override
    protected void onProgressUpdate(Object... progress) {
      if (synligAktivitet == null) return;
      String tekst = "arbejder - " + procent + "% færdig, mangler " + resttidISekunder + " sekunder endnu";
      Log.d("AsyncTask", tekst);
      synligAktivitet.knap.setText(tekst);
      synligAktivitet.progressBar.setProgress((int) procent);
    }

    @Override
    protected void onPostExecute(Object resultat) {
      if (synligAktivitet == null) return;
      synligAktivitet.knap.setText((String) resultat);
      synligAktivitet.knap.setEnabled(true);
      synligAktivitet.annullerknap.setVisibility(View.GONE); // Skjul knappen
      asyncTask = null;
    }

    @Override
    protected void onCancelled() {
      if (synligAktivitet == null) return;
      synligAktivitet.knap.setText("Annulleret før tid");
      synligAktivitet.knap.setEnabled(true);
      synligAktivitet.annullerknap.setVisibility(View.GONE); // Skjul knappen
      asyncTask = null;
    }
  }
}

