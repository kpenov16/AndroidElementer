package udeladt.lekt06_youtube;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import dk.nordfalk.android.elementer.R;

public class BenytVideoView extends AppCompatActivity implements OnClickListener {

  //private String videoUrl = "file:///sdcard/DCIM/100MEDIA/VIDEO0025.3gp";
  private String videoUrl = "https://javabog.dk/billeder/AK2008/15022008012.mp4";
  private String link = "https://javabog.dk";
  private VideoView videoView;
  private TextView overskrift;
  private TextView beskrivelse;

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.ufaerdigt_youtube_videoview);

    videoView = findViewById(R.id.youtube_videoView);
    overskrift = findViewById(R.id.youtube_overskrift);
    beskrivelse = findViewById(R.id.youtube_beskrivelse);

    Bundle e = getIntent().getExtras();
    if (e != null) {
      videoUrl = e.getString("videourl");
      link = e.getString("link");
      overskrift.setText(e.getString("titel"));
      beskrivelse.setText(e.getString("beskrivelse"));
    } else {
      overskrift.setText("Mangler data");
      beskrivelse.setText("Send et intent med med hvad der skal afspilles");
    }
    System.out.println("url = " + videoUrl);

    videoView.setVideoURI(Uri.parse(videoUrl));
    videoView.setMediaController(new MediaController(this));
    videoView.requestFocus();
    videoView.start();

    findViewById(R.id.youtube_infoknap).setOnClickListener(this);
  }

  /**
   * For at få videoen til at spille jævnt håndterer vi selv vending af skærmen
   * Se evt http://stackoverflow.com/questions/4434027/android-videoview-orientation-change-with-buffered-video
   *
   * @param newConfig
   */
  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

    System.out.println("newConfig = " + newConfig);
    boolean fuldskærm = (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE);

    beskrivelse.setVisibility(fuldskærm ? View.GONE : View.VISIBLE);
    overskrift.setVisibility(fuldskærm ? View.GONE : View.VISIBLE);
    findViewById(R.id.youtube_infoknap).setVisibility(fuldskærm ? View.GONE : View.VISIBLE);
    videoView.getParent().requestLayout();
  }

  public void onClick(View arg0) {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
    startActivity(intent);
  }
}
