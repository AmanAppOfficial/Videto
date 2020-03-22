package e.aman.videto10.videoPackage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import e.aman.videto10.R;
import e.aman.videto10.databinding.ActivityShowVideoBinding;

public class ShowVideoActivity extends AppCompatActivity {
    boolean isPlaying = false;
    private int duration;
    private ActivityShowVideoBinding binding;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideNavigationBar();
        binding = ActivityShowVideoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setListeners();
        SharedPreferences sharedPreferences = getSharedPreferences("url" , Context.MODE_PRIVATE);
        String url  = sharedPreferences.getString("url status" , "none");
        uri = Uri.parse(url);
        binding.cutScreenVideoview.setVideoURI(uri);
        binding.cutScreenVideoview.start();


        Toast.makeText(getApplicationContext() , "Saved to : " + url , Toast.LENGTH_LONG).show();



    }


    private void setListeners()
    {
        binding.cutPauseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    binding.cutPauseImage.setImageResource(R.drawable.play_icon);
                    binding.cutScreenVideoview.pause();
                    isPlaying = false;
                } else {
                    binding.cutScreenVideoview.start();
                    binding.cutPauseImage.setImageResource(R.drawable.pause_icon);
                    isPlaying = true;
                }
            }
        });

        binding.cutScreenVideoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                binding.cutScreenVideoview.start();
                duration = mp.getDuration()/1000;

                binding.tvvleft.setText("00.00.00");
                binding.tvvright.setText(getTime(mp.getDuration()/1000));
                mp.setLooping(true);
                binding.seekbar.setRangeValues(0,duration);
                binding.seekbar.setSelectedMaxValue(duration);
                binding.seekbar.setSelectedMinValue(0);
                binding.seekbar.setEnabled(true);
                binding.seekbar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
                    @Override
                    public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                        binding.cutScreenVideoview.seekTo((int)minValue*1000);
                        binding.tvvleft.setText(getTime((int)bar.getSelectedMinValue()));
                        binding.tvvright.setText(getTime((int)bar.getSelectedMaxValue()));
                    }
                });

            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(binding.cutScreenVideoview.getCurrentPosition() >= binding.seekbar.getSelectedMaxValue().intValue() * 1000)
                    binding.cutScreenVideoview.seekTo(binding.seekbar.getSelectedMinValue().intValue() * 1000);

            }
        },1000);
    }

    private String getTime(int seconds)
    {
        int hr = seconds/3600;
        int rem = seconds % 3600;
        int mn = rem/60;
        int sec = rem%60;
        return  String.format("%02d",hr) + ":" + String.format("%02d",mn) + ":" + String.format("%02d",sec);
    }

    void hideNavigationBar()
    {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideNavigationBar();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
   hideNavigationBar();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding.cutScreenVideoview.stopPlayback();
    }
}
