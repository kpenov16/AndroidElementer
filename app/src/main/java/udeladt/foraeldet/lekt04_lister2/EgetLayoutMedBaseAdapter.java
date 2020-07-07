package udeladt.foraeldet.lekt04_lister2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import dk.nordfalk.android.elementer.R;

public class EgetLayoutMedBaseAdapter extends AppCompatActivity implements OnItemClickListener {

  String[] lande = {"Danmark", "Norge", "Sverige", "Finland", "Holland", "Italien", "Tyskland",
          "Frankrig", "Spanien", "Portugal", "Nepal", "Indien", "Kina", "Japan", "Thailand"};

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ListView listView = new ListView(this);
    listView.setOnItemClickListener(this);
    listView.setAdapter(adapter);

    listView.setSelector(android.R.drawable.ic_notification_overlay);

    setContentView(listView);
  }

  public void onItemClick(AdapterView<?> l, View v, int position, long id) {
    Toast.makeText(this, "Klik på " + position, Toast.LENGTH_SHORT).show();
    lande[position] = "Du klikkede her!";
    adapter.notifyDataSetChanged();
  }

  BaseAdapter adapter = new BaseAdapter() {
    @Override
    public int getCount() {
      return lande.length;
    }

    public Object getItem(int position) {
      return position;
    } // bruges ikke

    public long getItemId(int position) {
      return position;
    } // bruges ikke

    @Override
    public View getView(int position, View view, ViewGroup parent) {
      // view kan indeholde views fra et gammelt listeelement, der kan genbruges
      if (view==null) view = getLayoutInflater().inflate(R.layout.lekt04_listeelement, null);

      TextView overskrift = view.findViewById(R.id.listeelem_overskrift);
      overskrift.setText(lande[position]);

      TextView beskrivelse = view.findViewById(R.id.listeelem_beskrivelse);
      beskrivelse.setText("Land nummer " + position);
      ImageView billede = view.findViewById(R.id.listeelem_billede);
      if (position % 3 == 2) {
        billede.setImageResource(android.R.drawable.sym_action_call);
      } else {
        billede.setImageResource(android.R.drawable.sym_action_email);
      }

      return view;
    }
  };
}
