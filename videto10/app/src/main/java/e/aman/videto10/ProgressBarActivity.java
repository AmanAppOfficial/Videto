package e.aman.videto10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import e.aman.videto10.audioPackage.PlayAudioActivity;
import e.aman.videto10.databinding.ActivityProgressBarBinding;
import e.aman.videto10.videoPackage.ShowVideoActivity;
import io.microshow.rxffmpeg.RxFFmpegInvoke;
import io.microshow.rxffmpeg.RxFFmpegSubscriber;

public class ProgressBarActivity extends AppCompatActivity {


    int duration;
    String[] command;
    String path;

    private ActivityProgressBarBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideNavigationBar();
        binding = ActivityProgressBarBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.circleProgressBar.setMax(100);

        Intent i = getIntent();

        if (i != null) {
            duration = i.getIntExtra("duration", 0);
            command = i.getStringArrayExtra("command");
            path = i.getStringExtra("destination");
        }



        SharedPreferences sharedPreferences = getSharedPreferences("url" , Context.MODE_PRIVATE);
        final String type = sharedPreferences.getString("type" , "null");

        RxFFmpegInvoke.getInstance().runCommandRxJava(command).subscribe(new RxFFmpegSubscriber() {
            @Override
            public void onFinish() {
                if(type.equals("audio"))
                {
                    Intent i = new Intent(ProgressBarActivity.this , PlayAudioActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
                else if(type.equals("video"))
                {
                    Intent i = new Intent(ProgressBarActivity.this , ShowVideoActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onProgress(int progress, long progressTime) {
                SharedPreferences preferences = getSharedPreferences("slowmo" , Context.MODE_PRIVATE);
                String fromSlowmo = preferences.getString("status" , "none");
                if(fromSlowmo.equals("yes"))
                {
                    binding.circleProgressBar.setProgress((int)progress/3);
                }
                else
                {binding.circleProgressBar.setProgress(progress);}
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String message) {

            }
        });


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
        SharedPreferences preferences = getSharedPreferences("slowmo" , Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("status" , "no");
        edit.apply();

    }
}
