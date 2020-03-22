package e.aman.videto10.audioPackage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import e.aman.videto10.R;
import e.aman.videto10.databinding.ActivityPlayAudioBinding;

public class PlayAudioActivity extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;
    private MediaPlayer mPlayer;
    private Handler mHandler;
    private Runnable mRunnable;
    private Uri uri;
    int length;

    private ActivityPlayAudioBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideNavigationBar();
        binding = ActivityPlayAudioBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        mContext = getApplicationContext();
        mActivity = PlayAudioActivity.this;


        // Initialize the handler
        mHandler = new Handler();

        SharedPreferences sharedPreferences = getSharedPreferences("url" , Context.MODE_PRIVATE);
        String url  = sharedPreferences.getString("url status" , "none");
        uri = Uri.parse(url);

        binding.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnPlay.setVisibility(View.GONE);
                binding.btnStop.setVisibility(View.VISIBLE);

                mPlayer = MediaPlayer.create(mContext, uri);
                mPlayer.seekTo(length);
                mPlayer.start();
                getAudioStats();
                // Initialize the seek bar
                initializeSeekBar();
            }
        });

        binding.btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.btnStop.setVisibility(View.GONE);
                binding.btnPlay.setVisibility(View.VISIBLE);
                mPlayer.pause();
                length=mPlayer.getCurrentPosition();
            }
        });

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mPlayer != null && fromUser) {
                    mPlayer.seekTo(progress * 1000);
                    length = mPlayer.getCurrentPosition();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        Toast.makeText(getApplicationContext() , "Saved to : " + url , Toast.LENGTH_LONG).show();



    }


    protected void stopPlaying() {
        // If media player is not null then try to stop it
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            if (mHandler != null) {
                mHandler.removeCallbacks(mRunnable);
            }
        }
    }

    protected void getAudioStats(){
        int duration  = mPlayer.getDuration()/1000; // In milliseconds
        int due = (mPlayer.getDuration() - mPlayer.getCurrentPosition())/1000;
        int pass = duration - due;

        binding.tvPass.setText("" + pass + " seconds");
        binding.tvDuration.setText("" + duration + " seconds");
        binding.tvDue.setText("" + due + " seconds");
    }



    protected void initializeSeekBar(){
        binding.seekBar.setMax(mPlayer.getDuration()/1000);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                if(mPlayer!=null){
                    int mCurrentPosition = mPlayer.getCurrentPosition()/1000; // In milliseconds
                    binding.seekBar.setProgress(mCurrentPosition);
                    getAudioStats();
                }
                mHandler.postDelayed(mRunnable,1000);
            }
        };
        mHandler.postDelayed(mRunnable,1000);
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
    protected void onResume() {
        super.onResume();
        hideNavigationBar();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideNavigationBar();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlaying();
    }



}
