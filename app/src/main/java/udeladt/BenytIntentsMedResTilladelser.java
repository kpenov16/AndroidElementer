package udeladt;

import android.annotation.TargetApi;
import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Demonstrerer hvordan man benytter intents til at vælg en
 * kontaktperson, et billede eller tage et billede med kameraet
 *
 * @author Jacob Nordfalk
 */
public class BenytIntentsMedResTilladelser extends AppCompatActivity implements OnClickListener {

  Button vælgBillede, tagBillede, beskærBillede;
  TextView resultatTextView;
  LinearLayout resultatHolder;
  private int VÆLG_BILLEDE = 2222;
  private int TAG_BILLEDE = 3333;
  private int BESKÆR_BILLEDE = 4444;
  private File filPåEksterntLager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TableLayout tl = new TableLayout(this);

    vælgBillede = new Button(this);
    vælgBillede.setText("Vælg billede fra galleri");
    vælgBillede.setOnClickListener(this);
    tl.addView(vælgBillede);

    tagBillede = new Button(this);
    tagBillede.setText("Tag billede med kameraet");
    tagBillede.setOnClickListener(this);
    tl.addView(tagBillede);

    beskærBillede = new Button(this);
    beskærBillede.setText("Beskær billedet");
    beskærBillede.setOnClickListener(this);
    tl.addView(beskærBillede);


    resultatTextView = new TextView(this);
    tl.addView(resultatTextView);

    resultatHolder = new LinearLayout(this);
    tl.addView(resultatHolder);

    ScrollView sv = new ScrollView(this);
    sv.addView(tl);
    setContentView(sv);
  }

  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
  public void onClick(View v) {
    try {
      if (v == vælgBillede) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(i, VÆLG_BILLEDE);

      } else if (v == tagBillede) {
        // Bemærk at jeg måtte have android:configChanges="orientation" for at aktiviteten
        // ikke blev vendt og jeg mistede billedet. I et rigtigt ville jeg forsyne mine views med
        // ID'er så deres indhold overlevede at skærmen skiftede orientering
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Hvis vi vil læse billedet i fuld opløsning fra ekstern lager/SD-kort skal vi give en URI
        filPåEksterntLager = new File(Environment.getExternalStorageDirectory(),"billede.jpg");
        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(filPåEksterntLager));
        startActivityForResult(i, TAG_BILLEDE);

      } else if (v == beskærBillede) {
        if (filPåEksterntLager==null) {
          Toast.makeText(this, "Vælg først et billede", Toast.LENGTH_LONG).show();
          return;
        }
        // Se http://stackoverflow.com/questions/18013406/is-com-android-camera-action-crop-not-available-for-android-jelly-bean-4-3
        // overvej i stedet at bruge f.eks. https://github.com/biokys/cropimage
        Toast.makeText(this, "Bemærk, der bruges et internt intent, der måske ikke fungerer nok ikke på alle telefoner", Toast.LENGTH_LONG).show();
        Uri billedeUri = Uri.fromFile(filPåEksterntLager);
        Intent i = new Intent("com.android.camera.action.CROP");
        i.setDataAndType(billedeUri, "image/*");
        i.putExtra("crop", "true");
        i.putExtra("aspectX", 2);
        i.putExtra("aspectY", 2);
        i.putExtra("outputX", 200);
        i.putExtra("outputY", 160);
        i.putExtra("return-data", true);
        startActivityForResult(i, BESKÆR_BILLEDE);

      } else {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://developer.android.com/guide/components/intents-common.html")));
      }
    } catch (Throwable e) {
      Toast.makeText(this, "Denne telefon mangler en funktion:\n" + e.getMessage(), Toast.LENGTH_LONG).show();
      e.printStackTrace();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    resultatTextView.setText("onActivityResult requestCode="+requestCode + " resultCode=" + resultCode + "\ndata=" + data);
    if (data!=null) {
      data.removeExtra("et eller andet"); // Gennemtving udpakning af at extra bundle
      resultatTextView.append("\nextras = "+data.getExtras());
      resultatTextView.append("\ndataURI = "+data.getData());
    }
    System.out.println(resultatTextView.getText());

    resultatHolder.removeAllViews();
    ContentResolver cr = getContentResolver();

    if (resultCode == Activity.RESULT_OK) {
      try {
        if (requestCode == VÆLG_BILLEDE) {
          AssetFileDescriptor filDS = getContentResolver().openAssetFileDescriptor(data.getData(), "r");
          Bitmap bmp = BitmapFactory.decodeStream(filDS.createInputStream());
          System.out.println("VÆLG_BILLEDE bmp = "+ bmp.getWidth());
          ImageView iv = new ImageView(this);
          iv.setImageBitmap(bmp);
          resultatHolder.addView(iv);
        } else if (requestCode == TAG_BILLEDE) {
          ImageView iv = new ImageView(this);
          if (filPåEksterntLager==null) {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            System.out.println("TAG_BILLEDE bmp = "+ bmp.getWidth());
            iv.setImageBitmap(bmp);
          } else { // læs billedet i fuld opløsning fra ekstern lager/SD-kort
            Bitmap bmp = BitmapFactory.decodeFile(filPåEksterntLager.getPath());
            System.out.println("TAG_BILLEDE bmp = "+ bmp.getWidth());
            iv.setImageBitmap(bmp);
          }
          resultatHolder.addView(iv);
        } else if (requestCode == BESKÆR_BILLEDE) {
          ImageView iv = new ImageView(this);
          Bitmap bmp = (Bitmap) data.getExtras().get("data");
          iv.setImageBitmap(bmp);
          resultatHolder.addView(iv);
        }

      } catch (IOException e) {
        e.printStackTrace();
        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
      }
    }
  }
}
