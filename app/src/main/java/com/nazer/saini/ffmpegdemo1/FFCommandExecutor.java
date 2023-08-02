package com.nazer.saini.ffmpegdemo1;

import android.util.Log;

import com.arthenica.ffmpegkit.FFmpegKit;
import com.arthenica.ffmpegkit.FFmpegSession;
import com.arthenica.ffmpegkit.FFmpegSessionCompleteCallback;
import com.arthenica.ffmpegkit.LogCallback;
import com.arthenica.ffmpegkit.ReturnCode;
import com.arthenica.ffmpegkit.SessionState;
import com.arthenica.ffmpegkit.Statistics;
import com.arthenica.ffmpegkit.StatisticsCallback;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * https://json2video.com/how-to/ffmpeg-course/ffmpeg-add-audio-to-video.html
 */
public class FFCommandExecutor {
    private final String TAG="CommandExecutor";
    private FFCommanderCallback ffCommanderCallback;
    private FFCommandExecutor()
    {

    }
    public FFCommandExecutor(FFCommanderCallback ffCommanderCallback)
    {
        this.ffCommanderCallback=ffCommanderCallback;
    }

    public void callCompressVideo(File file, String newFile, VIDEO_EXTENSIONS video)
    {
        switch (video)
        {
            case _3GP:
                compressVideo(file,newFile+".3gp");
                break;
            case MP4:
                compressVideo(file,newFile+".mp4");
                break;
        }
    }
    public void callCompressAudio(File file, String newFile, AUDIO audio)
    {
        switch (audio)
        {
            case MP3:
                break;
        }
    }
    public void callGetAudioFromVideo(File file, String newFile, AUDIO audio)
    {
        switch (audio)
        {
            case MP3:
                getAudioFromVideo(file,newFile+".mp3");
                break;
        }
    }
    public void callReplaceAudioWithVideo(File videoFilePath,File audioPath, String newFileNamePath, VIDEO_EXTENSIONS videoExt)
    {
        String newFileNamePath_=newFileNamePath;
        switch (videoExt)
        {
            case MP4:
                newFileNamePath_=newFileNamePath_+".mp4";
                break;
            case _3GP:
                newFileNamePath_=newFileNamePath_+".3gp";
                break;
        }

        Log.e(TAG, "getAudioFromVideo: "+videoFilePath.getPath() );
        Log.e(TAG, "getAudioFromVideo: "+audioPath.getPath() );
        Log.e(TAG, "setAudioFromVideo: "+newFileNamePath_ );
        String command = "-i "+videoFilePath.getPath()+" -i "+audioPath.getPath()+" -c:v copy -map 0:v -map 1:a -shortest -y "+newFileNamePath_;
        ExecutorService executorService= Executors.newFixedThreadPool(10);
        executeCommandNew(command,executorService);
    }
    private void compressVideo(File videoFile,String newFileNamePath)
    {
        String command="-i " +videoFile.getPath()+" -r 20 -s 352x288 -vb 400k -acodec aac -strict experimental -ac 1 -ar 8000 -ab 24k "+newFileNamePath;

//            FFmpegSession session = FFmpegKit.execute("-i file1.mp4 -c:v mpeg4 file2.mp4");
        ExecutorService executorService= Executors.newFixedThreadPool(10);
        executeCommandNew(command,executorService);
    }
    private void getAudioFromVideo(File videoFilePath, String newFileNamePath)
    {
        Log.e(TAG, "getAudioFromVideo: "+videoFilePath.getPath() );
        Log.e(TAG, "setAudioFromVideo: "+newFileNamePath );
        String command = "-i "+videoFilePath.getPath()+" -c:v mpeg4 -b:v 600k -c:a libmp3lame "+newFileNamePath;
        ExecutorService executorService= Executors.newFixedThreadPool(10);
        executeCommandNew(command,executorService);
    }
    private void compressAudio()
    {

    }
    private void compressImage(File imageFilePath,String newFileNamePath)
    {
        String command="-i " +imageFilePath.getPath()+" -r 20 -s 352x288 -vb 400k -acodec aac -strict experimental -ac 1 -ar 8000 -ab 24k "+newFileNamePath;

//            FFmpegSession session = FFmpegKit.execute("-i file1.mp4 -c:v mpeg4 file2.mp4");
        ExecutorService executorService= Executors.newFixedThreadPool(10);
        executeCommandNew(command,executorService);
    }
    public void callFfmpegCommand(File file, String newFileName) {
        try {
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

//            final String[] compressed = new String[]{"-i", file.getPath(),"-r","20","-s","352x288","-vb","400k","-acodec","aac","-strict","experimental","-ac","1","-ar","8000","-ab","24k" ,newFileName+".3gp"};
//            executeFFmpegCmd(compressed);

            String command="-i " +file.getPath()+" -r 20 -s 352x288 -vb 400k -acodec aac -strict experimental -ac 1 -ar 8000 -ab 24k "+newFileName+".3gp";

//            FFmpegSession session = FFmpegKit.execute("-i file1.mp4 -c:v mpeg4 file2.mp4");
            ExecutorService executorService= Executors.newFixedThreadPool(10);
            executeCommandNew(command,executorService);


        }  catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void executeCommandNew(String command,ExecutorService executorService)
    {
        FFmpegSession session = FFmpegKit.executeAsync(command, new FFmpegSessionCompleteCallback() {
            @Override
            public void apply(FFmpegSession session) {
                SessionState state = session.getState();
                ReturnCode returnCode = session.getReturnCode();
                if (ReturnCode.isSuccess(returnCode)) {
                    Log.e(TAG, "callFfmpegCommand: success" );
                    ffCommanderCallback.onSuccess();
                    // SUCCESS

                } else if (ReturnCode.isCancel(returnCode)) {
                    Log.e(TAG, "callFfmpegCommand: cancel" );
                    // CANCEL
                    ffCommanderCallback.onCanceled();

                } else {
                    // FAILURE
                    ffCommanderCallback.onFailed();
                    Log.d(TAG, String.format("Command failed with state %s and rc %s.%s", session.getState(), session.getReturnCode(), session.getFailStackTrace()));
                }
            }
        }, new LogCallback() {
            @Override
            public void apply(com.arthenica.ffmpegkit.Log log) {
                Log.i(TAG, "apply: =====> "+log.getLevel() );
                Log.i(TAG, "apply: =====> "+log.getMessage() );
            }
        }, new StatisticsCallback() {
            @Override
            public void apply(Statistics statistics) {
                Log.w(TAG, "apply: staistics "+statistics.toString() );
            }
        },executorService);
    }
}
