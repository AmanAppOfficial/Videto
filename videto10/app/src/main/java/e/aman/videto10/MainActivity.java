package e.aman.videto10;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.List;

import e.aman.videto10.databinding.ActivityMainBinding;
import e.aman.videto10.utils.Functions;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    boolean isPlaying = false;
    private int duration;
    Uri uriMain;
    String imagepath = "";

    private ViewGroup rootLayout;
    private int _xDelta;
    private int _yDelta;
    float x;
    float y;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideNavigationBar();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        rootLayout  =(ViewGroup)findViewById(R.id.view_root);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(450 , 450);
        binding.floatingImageLayout.setLayoutParams(layoutParams);
        binding.floatingImageLayout.setOnTouchListener(new choiceTouchListener());

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1223827385642400/6657266631");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        setListeners();
        addExternalStoragePermission();

        binding.videoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickVideo();}
        });

        binding.audioLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.extractAudio(MainActivity.this , uriMain , duration , mInterstitialAd);
            }
        });

        binding.cancelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Functions.slowMotion(MainActivity.this , uriMain , duration , mInterstitialAd);}
        });


        binding.trimLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Functions.trimVideo(MainActivity.this , uriMain , binding.seekbar.getSelectedMinValue().intValue() * 1000 ,
                    binding.seekbar.getSelectedMaxValue().intValue() * 1000 , mInterstitialAd);}
        });

        binding.stickerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {selectSticker();
            }
        });

    } //onCreate Closed

    private void selectSticker()
    {
        final HorizontalScrollView sticker_scrollview = findViewById(R.id.sticker_scrollview);
        final HorizontalScrollView option_scrollview = findViewById(R.id.task_scrollview);
        option_scrollview.setVisibility(View.GONE);
        sticker_scrollview.setVisibility(View.VISIBLE);
        binding.tickIconView.setVisibility(View.VISIBLE);
        binding.cancelIconView.setVisibility(View.VISIBLE);

        binding.stickerview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                imagepath = "/storage/emulated/0/MyVideosStickers/Sticker_19.png";
                binding.floatingImageview.setImageResource(R.drawable.sticker);
                binding.floatingImageLayout.setVisibility(View.VISIBLE);
            }
        });
        binding.stickerview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagepath = "/storage/emulated/0/MyVideosStickers/Sticker_29.png";
                binding.floatingImageview.setImageResource(R.drawable.sticker2);
                binding.floatingImageLayout.setVisibility(View.VISIBLE);
            }
        });
        binding.stickerview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagepath = "/storage/emulated/0/MyVideosStickers/Sticker_3.png";
                binding.floatingImageview.setImageResource(R.drawable.sticker3);
                binding.floatingImageLayout.setVisibility(View.VISIBLE);
            }
        });
        binding.stickerview4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagepath = "/storage/emulated/0/MyVideosStickers/Sticker_4.png";
                binding.floatingImageview.setImageResource(R.drawable.sticker5);
                binding.floatingImageLayout.setVisibility(View.VISIBLE);
            }
        });

    }


    private void setListeners() {

        binding.cancelIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.floatingImageLayout.setVisibility(View.GONE);
                binding.stickerScrollview.setVisibility(View.GONE);
                binding.taskScrollview.setVisibility(View.VISIBLE);
                binding.tickIconView.setVisibility(View.GONE);
                binding.cancelIconView.setVisibility(View.GONE);
            }
        });

        binding.tickIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.floatingImageLayout.setVisibility(View.GONE);
                binding.stickerScrollview.setVisibility(View.GONE);
                binding.taskScrollview.setVisibility(View.VISIBLE);
                binding.tickIconView.setVisibility(View.GONE);
                binding.cancelIconView.setVisibility(View.GONE);

                Functions.applySticker(MainActivity.this , uriMain , duration , imagepath , x , y , mInterstitialAd);
            }
        });

        binding.removeStickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.floatingImageLayout.setVisibility(View.GONE);
            }
        });

        binding.pauseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    binding.pauseImage.setImageResource(R.drawable.play_icon);
                    binding.mainScreenVideoview.pause();
                    isPlaying = false;
                } else {
                    binding.mainScreenVideoview.start();
                    binding.pauseImage.setImageResource(R.drawable.pause_icon);
                    isPlaying = true;
                }
            }
        });

        binding.mainScreenVideoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                binding.mainScreenVideoview.start();
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
                        binding.mainScreenVideoview.seekTo((int)minValue*1000);
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
                if(binding.mainScreenVideoview.getCurrentPosition() >= binding.seekbar.getSelectedMaxValue().intValue() * 1000)
                    binding.mainScreenVideoview.seekTo(binding.seekbar.getSelectedMinValue().intValue() * 1000);

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

    public void pickVideo()
    {
        Intent i = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("video/*");
        startActivityForResult(i , 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100  && resultCode == RESULT_OK)
        {
            uriMain = data.getData();
            isPlaying = true;
            binding.mainScreenVideoview.setVideoURI(uriMain);
            binding.mainScreenVideoview.start();

        }
    }

    public void addExternalStoragePermission()
    {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();

    }

    private class choiceTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int X = (int) event.getRawX();
            final int Y = (int) event.getRawY();
            switch (event.getAction() & MotionEvent.ACTION_MASK)
            {
                case MotionEvent.ACTION_DOWN :
                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                    _xDelta = X - lParams.leftMargin;
                    _yDelta = Y - lParams.topMargin;
                    break;
                case MotionEvent.ACTION_UP:

                    x =  event.getRawX();
                    y =  event.getRawY();

                    Log.e("x" , x+ "");
                    Log.e("y" , y+ "");


                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                    layoutParams.leftMargin = X - _xDelta;
                    layoutParams.topMargin =  Y - _yDelta;
                    layoutParams.rightMargin = -250;
                    layoutParams.bottomMargin = -250;
                    v.setLayoutParams(layoutParams);
                    break;
            }
            rootLayout.invalidate();
            return true;
        }
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
    protected void onStart() {
        super.onStart();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Functions.storeSticker(MainActivity.this);
            }
        });

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
}
