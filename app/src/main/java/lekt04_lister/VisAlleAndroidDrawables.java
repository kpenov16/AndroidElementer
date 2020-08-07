package lekt04_lister;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dk.nordfalk.android.elementer.R;

/**
 * Hvis man har en lang liste, krav til jævn scrolling og/eller skal
 * vise listeelementer der kræver indlæsning af resurser.
 * På Android bruges det goe gamle designmønster med GUI-tråden: Al GUI
 * afvikles i ÉN tråd, både visning/scrolling og forberedelse af de ting
 * der skal vise. I visse tilfælde bliver det vigtigt at tænke sig godt
 * om når man programmerer listefunktioner.
 * Der skal simpelt hen ske så lidt som muligt (herunder ingen indlæsning
 * af resurser eller dekodning af bitmaps) synkront i GUI-tråden under
 * listevisningen.
 * Et andet tip er her:
 * http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
 * Et tredje tip er at tænke over hvilken baggrund man bruger. Jeg har
 * selv prøvet at gå fra forfærdelig til fantastisk performance ved at
 * kigge mine ListViews/ListActivities efter i sømmene og få luget ud i
 * baggrundene.
 *
 * @author j
 */
public class VisAlleAndroidDrawables extends AppCompatActivity implements AdapterView.OnItemClickListener {
  Executor bgThread = Executors.newSingleThreadExecutor(); // håndtag til en baggrundstråd
  Handler uiThread = new Handler(Looper.getMainLooper());  // håndtag til forgrundstråden
  /**
   * Om billeder og resurser skal indlæses i en baggrundstråd eller i hovedtråden
   */
  boolean asynkronIndlæsning = true;
  /**
   * Om views bliver genbrugt eller ej
   */
  boolean genbrugElementer = true;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ListView listView = new ListView(this);
    listView.setAdapter(new AndroidDrawablesAdapter());
    listView.setDividerHeight(3);
    listView.setOnItemClickListener(this);
    listView.setFastScrollEnabled(true);


		/*
    håndtering af baggrundsbilleder
		listView.setBackgroundResource(R.drawable.bil);

		// Sørg for at baggrunden bliver tegnet, også når listen scroller.
		// Se http://android-developers.blogspot.com/2009/01/why-is-my-list-black-android.html
		listView.setCacheColorHint(0x00000000);
		// Man kunne have en ensfarvet baggrund, det gør scroll mere flydende
		//getListView().setCacheColorHint(0xffe4e4e4);
		 */
    if (savedInstanceState == null) {
      // Ny aktivitet, vis hjælp
      Toast.makeText(this, "Dette eksempel viser også hvor stor forskel genbrug af elementer og asynkron indlæsning gør", Toast.LENGTH_LONG).show();
      Toast.makeText(this, "Tryk MENU for at slå disse forbedringer fra og mærk forskellen", Toast.LENGTH_LONG).show();
    }

    setContentView(listView);
  }

  public class AndroidDrawablesAdapter extends BaseAdapter {
    Resources res = getResources();

    public int getCount() {
      return 1500;
    } // der er omkring tusind drawables

    public Object getItem(int position) {
      return position;
    } // bruges ikke

    public long getItemId(int position) {
      return position;
    } // bruges ikke

    public View getView(final int position, View view, ViewGroup parent) {
      final ListeelemViewholder listeelem;
      if (view == null || !genbrugElementer) {
        view = getLayoutInflater().inflate(R.layout.lekt04_listeelement, null);

        // For at spare CPU-cykler cacher vi opslagene i findViewById(). Se
        // http://developer.android.com/training/improving-layouts/smooth-scrolling.html
        listeelem = new ListeelemViewholder();
        listeelem.overskrift = view.findViewById(R.id.listeelem_overskrift);
        listeelem.beskrivelse = view.findViewById(R.id.listeelem_beskrivelse);
        listeelem.billede = view.findViewById(R.id.listeelem_billede);
        view.setTag(listeelem);
      } else {
        listeelem = (ListeelemViewholder) view.getTag();
      }

      final int resurseId = android.R.drawable.alert_dark_frame + position; // første resurse
      listeelem.overskrift.setText(Integer.toString(resurseId));
      listeelem.beskrivelse.setText("Hex: " + Integer.toHexString(resurseId));

      // For at sikre flydende scroll kan vi IKKE indlæse resursen i GUI-tråden
      if (!asynkronIndlæsning) {
        try {
          Drawable drawable = res.getDrawable(resurseId);
          listeelem.billede.setImageDrawable(drawable);
        } catch (Exception e) {// sker hvis en drawable med det ID ikke findes
        }
      } else {
        listeelem.billede.setImageDrawable(null);
        listeelem.position = position;
        // Indlæs billedet i baggrunden

        bgThread.execute(() -> {
          if (listeelem.position != position) return; // der er gået lidt tid, det kan være position ikke mere er aktuel
          try {
            Drawable drawable = res.getDrawable(resurseId);
            uiThread.post(() -> {
              if (listeelem.position != position) return; // der er gået lidt tid, det kan være position ikke mere er aktuel
              listeelem.billede.setImageDrawable(drawable);
            });
          } catch (Exception e) {// sker hvis en drawable med det ID ikke findes
          }
        });
      }


      return view;
    }

    private class ListeelemViewholder {
      public int position;
      public TextView overskrift;
      public TextView beskrivelse;
      public ImageView billede;
    }
  }


  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    final int resurseId = android.R.drawable.alert_dark_frame + position; // første resurse
    Toast t = new Toast(this);
    ImageView im = new ImageView(this);
    im.setImageResource(resurseId);
    t.setView(im);
    t.show();
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(Menu.NONE, 100, Menu.NONE, "Genbrug elementer");
    menu.add(Menu.NONE, 101, Menu.NONE, "Asynkron indlæsning");
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == 100) {
      genbrugElementer = !genbrugElementer;
      Toast.makeText(this, "Genbrug elementer: " + genbrugElementer, Toast.LENGTH_SHORT).show();
    }
    if (item.getItemId() == 101) {
      asynkronIndlæsning = !asynkronIndlæsning;
      Toast.makeText(this, "Asynkron indlæsning: " + asynkronIndlæsning, Toast.LENGTH_SHORT).show();
    }
    return true;
  }
}
