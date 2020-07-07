package lekt01_views;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;

/**
 * @author Jacob Nordfalk
 */
public class BenytKnapperProgrammatisk extends AppCompatActivity implements OnClickListener {
  // Vi erklærer variabler herude så de huskes fra metode til metode
  Button knap1, knap2, knap3;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Havde vi brugt deklarativt layout i XML havde vi her skrevet
    //setContentView(R.layout.tre_knapper);
    //knap1 = (Button) findViewById(R.id.knap1);
    //knap2 = (Button) findViewById(R.id.knap2);
    //knap3 = (Button) findViewById(R.id.knap3);

    // Nu bruger vi programmatisk layout, og det ser således ud
    TableLayout tl = new TableLayout(this);

    knap1 = new Button(this);
    knap1.setText("En knap");
    tl.addView(knap1);

    knap2 = new Button(this);
    knap2.setText("En anden knap");
    tl.addView(knap2);

    knap3 = new Button(this);
    knap3.setText("En tredje knap");
    tl.addView(knap3);

    setContentView(tl);

    knap1.setOnClickListener(this);
    knap2.setOnClickListener(this);
    knap3.setOnClickListener(this);
  }

  public void onClick(View klikPåHvad) {
    System.out.println("Der blev trykket på en knap");

    // Vis et tal der skifter så vi kan se hver gang der trykkes
    long etTal = System.currentTimeMillis();

    if (klikPåHvad == knap1) {

      knap1.setText("Du trykkede på mig. Tak! \n" + etTal);

    } else if (klikPåHvad == knap2) {

      knap3.setText("Nej nej, tryk på mig i stedet!\n" + etTal);

    } else if (klikPåHvad == knap3) {

      knap2.setText("Hey, hvis der skal trykkes, så er det på MIG!\n" + etTal);

    }

  }
}
