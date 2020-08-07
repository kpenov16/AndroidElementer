package lekt04_fragmenter;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;

import dk.nordfalk.android.elementer.R;

/**
 * @author Jacob Nordfalk
 */
public class Hovedaktivitet extends AppCompatActivity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_ACTION_BAR);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lekt04_fragmenter_manuel_nav);

    if (savedInstanceState == null) {
      Fragment fragment = new Velkomst_frag();
      getSupportFragmentManager().beginTransaction()
              .add(R.id.fragmentindhold, fragment)  // tom container i layout
              .commit();
    }

    setTitle("Hovedaktivitet");
    // Man kan trykke på app-ikonet i øverste venstre hjørne
    // (og det betyder at brugeren vil navigere op i hierakiet)
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed(); // brugeren vil navigere op i hierakiet
    }
    return super.onOptionsItemSelected(item);
  }
}
