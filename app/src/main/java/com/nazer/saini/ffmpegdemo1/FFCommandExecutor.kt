package com.nazer.saini.ffmpegdemo1

import android.util.Log
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * https://json2video.com/how-to/ffmpeg-course/ffmpeg-add-audio-to-video.html
 */
class FFCommandExecutor {
    private val TAG = "CommandExecutor"
    private var ffCommanderCallback: FFCommanderCallback? = null

    private constructor() {}
    constructor(ffCommanderCallback: FFCommanderCallback?) {
        this.ffCommanderCallback = ffCommanderCallback
    }

    fun callCompressVideo(file: File, newFile: String, video: VIDEO_EXTENSIONS?) {
        when (video) {
            VIDEO_EXTENSIONS._3GP -> compressVideo(file, "$newFile.3gp")
            VIDEO_EXTENSIONS.MP4 -> compressVideo(file, "$newFile.mp4")
            else -> {}
        }
    }

    fun callCompressAudio(file: File?, newFile: String?, audio: AUDIO?) {
        when (audio) {
            AUDIO.MP3 -> {}
            else -> {}
        }
    }

    fun callGetAudioFromVideo(file: File, newFile: String, audio: AUDIO?) {
        when (audio) {
            AUDIO.MP3 -> getAudioFromVideo(file, "$newFile.mp3")
            else -> {}
        }
    }

    fun callReplaceAudioWithVideo(
        videoFilePath: File,
        audioPath: File,
        newFileNamePath: String,
        videoExt: VIDEO_EXTENSIONS?
    ) {
        var newFileNamePathDir = newFileNamePath
        when (videoExt) {
            VIDEO_EXTENSIONS.MP4 -> newFileNamePathDir = "$newFileNamePathDir.mp4"
            VIDEO_EXTENSIONS._3GP -> newFileNamePathDir = "$newFileNamePathDir.3gp"
            else -> {}
        }
        Log.e(TAG, "getAudioFromVideo: " + videoFilePath.path)
        Log.e(TAG, "getAudioFromVideo: " + audioPath.path)
        Log.e(TAG, "setAudioFromVideo: $newFileNamePathDir")
        val command =
            "-i " + videoFilePath.path + " -i " + audioPath.path + " -c:v copy -map 0:v -map 1:a -shortest -y " + newFileNamePathDir
        val executorService = Executors.newFixedThreadPool(10)
        executeCommandNew(command, executorService)
    }

    private fun compressVideo(videoFile: File, newFileNamePath: String) {
        val command =
            "-i " + videoFile.path + " -r 20 -s 352x288 -vb 400k -acodec aac -strict experimental -ac 1 -ar 8000 -ab 24k " + newFileNamePath

//            FFmpegSession session = FFmpegKit.execute("-i file1.mp4 -c:v mpeg4 file2.mp4");
        val executorService = Executors.newFixedThreadPool(10)
        executeCommandNew(command, executorService)
    }

    private fun getAudioFromVideo(videoFilePath: File, newFileNamePath: String) {
        Log.e(TAG, "getAudioFromVideo: " + videoFilePath.path)
        Log.e(TAG, "setAudioFromVideo: $newFileNamePath")
        val command =
            "-i " + videoFilePath.path + " -c:v mpeg4 -b:v 600k -c:a libmp3lame " + newFileNamePath
        val executorService = Executors.newFixedThreadPool(10)
        executeCommandNew(command, executorService)
    }

    private fun compressAudio() {}
    private fun compressImage(imageFilePath: File, newFileNamePath: String) {
        val command =
            "-i " + imageFilePath.path + " -r 20 -s 352x288 -vb 400k -acodec aac -strict experimental -ac 1 -ar 8000 -ab 24k " + newFileNamePath

//            FFmpegSession session = FFmpegKit.execute("-i file1.mp4 -c:v mpeg4 file2.mp4");
        val executorService = Executors.newFixedThreadPool(10)
        executeCommandNew(command, executorService)
    }

    fun callFfmpegCommand(file: File, newFileName: String) {
        try {
            //                    String cmd ="-i "+file.getAbsolutePath()+" /storage/emulated/0/"+ newFileName+".mp3";
//                    String cmd = new String("-i {filepath}{filenameRaw}.mp4 -i {filepath}{filenameRaw}.mp4 -i {filepath}{filenameRaw}.mp4 -i {filepath}{filenameRaw}.mp4 -filter_complex \"[0:v][1:v]hstack[top]; [2:v][3:v]hstack[bottom]; [top][bottom]vstack\" {filenameCropped}.mp4");
//                    String cmd =new String("ffmpeg -i "+file.getPath()+" -codec copy -an /storage/emulated/0/"+newFileName+".mp4");
            val cmd: String = "ffmpeg -i " + file.path

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
            val command =
                "-i " + file.path + " -r 20 -s 352x288 -vb 400k -acodec aac -strict experimental -ac 1 -ar 8000 -ab 24k " + newFileName + ".3gp"

//            FFmpegSession session = FFmpegKit.execute("-i file1.mp4 -c:v mpeg4 file2.mp4");
            val executorService = Executors.newFixedThreadPool(10)
            executeCommandNew(command, executorService)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun executeCommandNew(command: String, executorService: ExecutorService) {
        val session = FFmpegKit.executeAsync(command, { session ->
            val state = session.state
            val returnCode = session.returnCode
            if (ReturnCode.isSuccess(returnCode)) {
                Log.e(
                    TAG,
                    "callFfmpegCommand: ====>=>>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=> success"
                )
                ffCommanderCallback!!.onSuccess()
                // SUCCESS
            } else if (ReturnCode.isCancel(returnCode)) {
                Log.e(
                    TAG,
                    "callFfmpegCommand: ==>=>=>==>>==>=>=>=>>==>>==>==>=>=>=>=>=>=>=> cancel"
                )
                // CANCEL
                ffCommanderCallback!!.onCanceled()
            } else {
                // FAILURE
                ffCommanderCallback!!.onFailed()
                Log.d(
                    TAG,
                    String.format(
                        "Command failed with state %s and rc %s.%s",
                        session.state,
                        session.returnCode,
                        session.failStackTrace
                    )
                )
            }
        }, { log ->
            Log.i(TAG, "apply: =====> " + log.level)
            Log.i(TAG, "apply: =====> " + log.message)
        }, { statistics -> Log.w(TAG, "apply: staistics $statistics") }, executorService)
    }
}