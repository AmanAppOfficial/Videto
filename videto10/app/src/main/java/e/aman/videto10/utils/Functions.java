package e.aman.videto10.utils;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import e.aman.videto10.ProgressBarActivity;
import e.aman.videto10.R;

public class Functions
{
    static String filePrefix;
    static File dest;
    static String[] command;

    static AlertDialog.Builder alert;
    static Context ctx;
    static Uri uri;
    static int duration;
    static String imagepath;
    static float sticker_x , sticker_y;
    static int start_time_trim , end_time_trim;

    private static InterstitialAd interstitialAd;

    public static void extractAudio(final Context context , Uri uriMain , int duration_time , InterstitialAd mInterstitial)
    {
        interstitialAd = mInterstitial;
       alert = new AlertDialog.Builder(context);
       ctx = context;
       uri = uriMain;
       duration = duration_time;

                    LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params  = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT ,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(50 , 0 , 50 , 100);

            final EditText input = new EditText(context);
            input.setLayoutParams(params);
            input.setGravity(Gravity.TOP | Gravity.START);
            input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            linearLayout.addView(input , params);

            alert.setMessage("Set Audio Name");
            alert.setTitle("Change Audio Name");
            alert.setView(linearLayout);
            alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alert.setPositiveButton("submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    filePrefix = input.getText().toString().trim();

                    ExtractAudioFromVideo(filePrefix);

                    if (interstitialAd.isLoaded()) {
                        interstitialAd.show();
                    }
                    Intent intent = new Intent(context , ProgressBarActivity.class);
                    intent.putExtra("duration" , duration);
                    intent.putExtra("command" , command);
                    intent.putExtra("destination" , dest.getAbsolutePath());
                    context.startActivity(intent);


                    dialog.dismiss();



                }
            });

            alert.show();
    }

    private static void ExtractAudioFromVideo(String fileName)
    {
        String root = Environment.getExternalStorageDirectory().toString();

        File folder = new File(root + "/TrimVideos/");
        if(!folder.exists())
        {
            folder.mkdirs();
        }

        filePrefix = fileName;

        String fileExtension = ".mp3";
        dest = new File(folder , filePrefix + fileExtension);
        String original_path = getRealPathFromUri(ctx , uri);

        command = new String[] {"-y", "-i", original_path, "-vn", "-ar", "44100", "-ac", "2", "-b:a", "256k", "-f", "mp3", dest.getAbsolutePath()};

        String final_path = dest.getAbsolutePath().trim();
        SharedPreferences sharedPreferences =  ctx.getSharedPreferences("url" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("type" , "audio");
        editor.putString("url status" , final_path);
        editor.apply();
    }
    private static String getRealPathFromUri(Context context, Uri contentUri)
    {
        Cursor cursor = null;

        try {

            String proj[] = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri , proj , null , null , null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
        finally {
            if(cursor!=null)
            {
                cursor.close();
            }
        }



    }


    public static void storeSticker(Context context) {

        ctx = context;
        int counter= 9;
        String root = Environment.getExternalStorageDirectory().toString();

        if(root != null)
        {
            File folder = new File(root + "/MyVideosStickers/");

            if(!folder.exists())
                folder.mkdirs();

            try {
                Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(),
                        R.drawable.sticker);

                OutputStream fOut = null;
                File file = new File(folder, "Sticker_1" + counter +".png");
                if(!file.exists())
                {

                    file.createNewFile();
                    fOut = new FileOutputStream(file);

                    // 100 means no compression, the lower you go, the stronger the compression
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();

                    MediaStore.Images.Media.insertImage(ctx.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());


                }
            }
            catch (IOException e)
            {}


            try {
                Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(),
                        R.drawable.sticker2);

                OutputStream fOut = null;
                File file = new File(folder, "Sticker_2" + counter +".png");

                if(!file.exists())
                {
                    file.createNewFile();
                    fOut = new FileOutputStream(file);

                    // 100 means no compression, the lower you go, the stronger the compression
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();

                    MediaStore.Images.Media.insertImage(ctx.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
                }
            }
            catch (IOException e)
            {}

            try {
                Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(),
                        R.drawable.sticker3);

                OutputStream fOut = null;
                File file = new File(folder, "Sticker_3.png");
                if(!file.exists())
                {
                    file.createNewFile();
                    fOut = new FileOutputStream(file);

                    // 100 means no compression, the lower you go, the stronger the compression
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();

                    MediaStore.Images.Media.insertImage(ctx.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());

                }
            }
            catch (IOException e)
            {}

            try {
                Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(),
                        R.drawable.sticker5);

                OutputStream fOut = null;
                File file = new File(folder, "Sticker_4.png");
                if(!file.exists())
                {
                    file.createNewFile();
                    fOut = new FileOutputStream(file);

                    // 100 means no compression, the lower you go, the stronger the compression
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();

                    MediaStore.Images.Media.insertImage(ctx.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
                }
            }
            catch (IOException e)
            {}

        }


    }

    public static void applySticker(final Context context, Uri uriMain, int duration_main , String image_path , float x , float y , InterstitialAd minterstitial)
    {
        interstitialAd = minterstitial;
        sticker_x = x;
        sticker_y = y;
        alert = new AlertDialog.Builder(context);
        imagepath = image_path;
        ctx = context;
        uri = uriMain;
        duration = duration_main;

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params  = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT ,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(50 , 0 , 50 , 100);

        final EditText input = new EditText(context);
        input.setLayoutParams(params);
        input.setGravity(Gravity.TOP | Gravity.START);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        linearLayout.addView(input , params);

        alert.setMessage("Set Video Name");
        alert.setTitle("Change Video Name");
        alert.setView(linearLayout);
        alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.setPositiveButton("submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filePrefix = input.getText().toString().trim();

                ApplyStickerOnVideo(filePrefix);

                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }

                Intent intent = new Intent(context , ProgressBarActivity.class);
                intent.putExtra("duration" , duration);
                intent.putExtra("command" , command);
                intent.putExtra("destination" , dest.getAbsolutePath());
                context.startActivity(intent);


                dialog.dismiss();



            }
        });

        alert.show();
    }

    private static void ApplyStickerOnVideo(String fileName)
    {
        String root = Environment.getExternalStorageDirectory().toString();

        File folder = new File(root + "/TrimVideos/");
        if(!folder.exists())
        {
            folder.mkdirs();
        }

        filePrefix = fileName;

        String fileExtension = ".mp4";
        dest = new File(folder , filePrefix + fileExtension);
        String original_path = getRealPathFromUri(ctx , uri);

        Log.e("x--------" , (int)sticker_x + "");
        Log.e("y--------" , (int)sticker_y + "");
//        SharedPreferences sf = ctx.getSharedPreferences("imagepath" , Context.MODE_PRIVATE);
        command =new String[]{"-y","-i",original_path,"-i", imagepath,"-filter_complex","[1:v]scale=300:300 [ovrl], [0:v][ovrl]overlay=x=" + sticker_x + ":y=" + sticker_y + "","-codec:a","copy","-strict","-2","-c:v","libx264","-preset","ultrafast",dest.getAbsolutePath()};

        String final_path = dest.getAbsolutePath().trim();
        Log.e("final path" , final_path);
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("url" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("type" , "video");
        editor.putString("url status", final_path);
        editor.apply();

    }

    public static void trimVideo(final Context context, Uri uriMain, int startMs, int endMs , InterstitialAd minterstitial)
    {
        interstitialAd = minterstitial;
        alert = new AlertDialog.Builder(context);
        ctx = context;
        uri = uriMain;
        start_time_trim = startMs;
        end_time_trim = endMs;


        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params  = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT ,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(50 , 0 , 50 , 100);

        final EditText input = new EditText(context);
        input.setLayoutParams(params);
        input.setGravity(Gravity.TOP | Gravity.START);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        linearLayout.addView(input , params);

        alert.setMessage("Set Video Name");
        alert.setTitle("Change Video Name");
        alert.setView(linearLayout);
        alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.setPositiveButton("submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filePrefix = input.getText().toString().trim();

                trimVideoToRange(start_time_trim , end_time_trim , filePrefix);

                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }

                Intent intent = new Intent(context , ProgressBarActivity.class);
                intent.putExtra("duration" , duration);
                intent.putExtra("command" , command);
                intent.putExtra("destination" , dest.getAbsolutePath());
                context.startActivity(intent);


                dialog.dismiss();



            }
        });

        alert.show();
    }

    private static void trimVideoToRange(int startMs, int endMs,  String fileName)
    {
        String root = Environment.getExternalStorageDirectory().toString();

        File folder = new File(root + "/TrimVideos/");
        if(!folder.exists())
        {
            folder.mkdirs();
        }

        filePrefix = fileName;

        String fileExtension = ".mp4";
        dest = new File(folder , filePrefix + fileExtension);
        String original_path = getRealPathFromUri(ctx , uri);

        duration = (endMs - startMs)/1000 ;
       command = new String[]{"-y", "-i", original_path,"-ss", "" + startMs / 1000, "-t", "" + (endMs - startMs) / 1000, "-c","copy", dest.getAbsolutePath()};

        String final_path = dest.getAbsolutePath().trim();
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("url" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("type" , "video");
        editor.putString("url status", final_path);
        editor.apply();


    }

    public static void slowMotion(Context context , Uri uriMain, int duration_main , InterstitialAd minterstitialAd)
    {
        interstitialAd = minterstitialAd;
        alert = new AlertDialog.Builder(context);
        ctx = context;
        uri = uriMain;
        duration = duration_main;

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params  = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT ,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(50 , 0 , 50 , 100);

        final EditText input = new EditText(context);
        input.setLayoutParams(params);
        input.setGravity(Gravity.TOP | Gravity.START);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        linearLayout.addView(input , params);

        alert.setMessage("Set Video Name");
        alert.setTitle("Change Video Name");
        alert.setView(linearLayout);
        alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.setPositiveButton("submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filePrefix = input.getText().toString().trim();

                SlowMo(filePrefix);

                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
                Intent intent = new Intent(ctx , ProgressBarActivity.class);
                intent.putExtra("duration" , duration);
                intent.putExtra("command" , command);
                intent.putExtra("destination" , dest.getAbsolutePath());
                ctx.startActivity(intent);


                dialog.dismiss();



            }
        });

        alert.show();
    }

    private static void SlowMo(String fileName)
    {
        String root = Environment.getExternalStorageDirectory().toString();

        File folder = new File(root + "/TrimVideos/");
        if(!folder.exists())
        {
            folder.mkdirs();
        }

        filePrefix = fileName;

        String fileExtension = ".mp4";
        dest = new File(folder , filePrefix + fileExtension);
        String original_path = getRealPathFromUri(ctx , uri);

       command = new String[] {"-y", "-i", original_path,  "-filter:v" , "setpts=3.0*PTS" ,"-codec:a","copy","-strict","-2","-c:v","libx264","-preset","ultrafast" ,  dest.getAbsolutePath()};
        String final_path = dest.getAbsolutePath().trim();
        SharedPreferences sharedPreferences =  ctx.getSharedPreferences("url" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("type" , "video");
        editor.putString("url status" , final_path);
        editor.apply();

        SharedPreferences preferences = ctx.getSharedPreferences("slowmo" , Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("status" , "yes");
        edit.apply();


    }
}
