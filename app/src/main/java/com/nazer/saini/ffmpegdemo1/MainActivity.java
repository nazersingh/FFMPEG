package com.nazer.saini.ffmpegdemo1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.util.Arrays;


/**
 * Commands
 * https://gist.github.com/protrolium/e0dbd4bb0f1a396fcb55
 * https://www.catswhocode.com/blog/19-ffmpeg-commands-for-all-needs
 */
public class MainActivity extends AppCompatActivity {

    String TAG=this.getClass().getSimpleName();
    FFmpeg ffmpeg;
    String newFileName="/storage/emulated/0/new";
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        file=new File("/storage/emulated/0/WhatsApp/Media/WhatsApp Video/VID-20180815-WA0002.mp4");
//        file=new File("/storage/emulated/0/DCIM/Camera/VID_20180707_023942.mp4");
//        file=new File("/storage/emulated/0/music/Aisi Taisi.mp3");
//        file=new File("/storage/emulated/0/Download/images.jpeg");
        long size=file.length();
        long filesize=size/1024;
        Log.e(TAG, "onCreate: File  "+filesize+"    "+file.length()+" "+file.getAbsolutePath());
        initialize(MainActivity.this);

//        String[] cmd = {"-ss", String.valueOf(currentVideoPosition), "-i", "/storage/emulated/0/Videos/01.mp4",
//                "-frames:v", "1", "/storage/emulated/0/Videos/out"+ count +".jpg"};
//        String cmd = new String("-i {filepath}{filenameRaw}.mp4 -i {filepath}{filenameRaw}.mp4 -i {filepath}{filenameRaw}.mp4 -i {filepath}{filenameRaw}.mp4 -filter_complex \"[0:v][1:v]hstack[top]; [2:v][3:v]hstack[bottom]; [top][bottom]vstack\" {filenameCropped}.mp4");

        findViewById(R.id.btn_media).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                    String cmd ="-i "+file.getAbsolutePath()+" /storage/emulated/0/"+ newFileName+".mp3";
//                    String cmd = new String("-i {filepath}{filenameRaw}.mp4 -i {filepath}{filenameRaw}.mp4 -i {filepath}{filenameRaw}.mp4 -i {filepath}{filenameRaw}.mp4 -filter_complex \"[0:v][1:v]hstack[top]; [2:v][3:v]hstack[bottom]; [top][bottom]vstack\" {filenameCropped}.mp4");
//                    String cmd =new String("ffmpeg -i "+file.getPath()+" -codec copy -an /storage/emulated/0/"+newFileName+".mp4");
                String cmd =new String("ffmpeg -i "+file.getPath());


                /**
                 * working commands
                 *
                 * commpressed
                 * final String[] compressed = new String[]{"-y", "-i", file.getPath(), "-strict", "experimental", "-vcodec", "libx264", "-preset", "ultrafast", "-crf", "24", "-acodec", "aac", "-ar", "44100", "-ac", "2", "-b:a", "96k","-s", "1280" + "x" + "720", "-aspect", "16:9", "/storage/emulated/0/"+newFileName+".mp4"};
                 *
                 * video to mp3
                 * final String[] compressed = new String[]{"-i", file.getPath(), "-c:v", "mpeg4","-b:v","600k","-c:a","libmp3lame",newFileName+".mp3"};
                 *
                 * video to another format
                 * final String[] compressed = new String[]{"-i", file.getPath(),"-r","20","-s","352x288","-vb","400k","-acodec","aac","-strict","experimental","-ac","1","-ar","8000","-ab","24k" ,newFileName+".3gp"};
                 */


//                    final String[] compressed = new String[]{"-y", "-i", file.getPath(), "-strict", "experimental", "-vcodec", "libx264", "-preset", "ultrafast", "-crf", "24", "-acodec", "aac", "-ar", "44100", "-ac", "2", "-b:a", "96k","-s", "1280" + "x" + "720", "-aspect", "16:9", "/storage/emulated/0/"+newFileName+".mp4"};

                final String[] compressed = new String[]{"-i", file.getPath(),"-r","20","-s","352x288","-vb","400k","-acodec","aac","-strict","experimental","-ac","1","-ar","8000","-ab","24k" ,newFileName+".3gp"};
                executeFFmpegCmd(compressed);
            }
        });

    }

    private void executeFFmpegCmd(String[] cmd) {
        try {
            Log.e(TAG, "executeFFmpegCmd: "+ cmd +" "+ Arrays.toString(cmd));
//            Log.e(TAG, "executeFFmpegCmd: "+ cmd );

            ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {
                    Log.e(TAG, "onStart: " );
                }

                @Override
                public void onProgress(String message) {
                    Log.e(TAG, "onProgress: "+message );
                }

                @Override
                public void onFailure(String message) {
                    Log.e(TAG, "onFailure: "+message );
                }

                @Override
                public void onSuccess(String message) {
                    Log.e(TAG, "onSuccess: "+message );
                }

                @Override
                public void onFinish() {
                    Log.e(TAG, "onFinish: " );
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }

    public void initialize(Context context) {
        ffmpeg=FFmpeg.getInstance(context);
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onSuccess() {
                    // FFmpeg is supported by device
                    Log.e(TAG, "onSuccess: Supported" );
                }
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
        }
    }

}

