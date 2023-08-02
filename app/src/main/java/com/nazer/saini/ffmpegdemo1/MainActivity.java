package com.nazer.saini.ffmpegdemo1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;


import java.io.File;
import java.util.Objects;

/**
 * Commands
 * https://github.com/arthenica/ffmpeg-kit/tree/main/android
 * https://gist.github.com/protrolium/e0dbd4bb0f1a396fcb55
 * https://www.catswhocode.com/blog/19-ffmpeg-commands-for-all-needs
 */
public class MainActivity extends AppCompatActivity implements FFCommanderCallback {

    String TAG = this.getClass().getSimpleName();
    //FFmpeg ffmpeg;
    String newFileName = "";
    private ProgressBar mProgressBar;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progress_bar);
        // initialize(MainActivity.this);

//        String[] cmd = {"-ss", String.valueOf(currentVideoPosition), "-i", "/storage/emulated/0/Videos/01.mp4",
//                "-frames:v", "1", "/storage/emulated/0/Videos/out"+ count +".jpg"};
//        String cmd = new String("-i {filepath}{filenameRaw}.mp4 -i {filepath}{filenameRaw}.mp4 -i {filepath}{filenameRaw}.mp4 -i {filepath}{filenameRaw}.mp4 -filter_complex \"[0:v][1:v]hstack[top]; [2:v][3:v]hstack[bottom]; [top][bottom]vstack\" {filenameCropped}.mp4");

        findViewById(R.id.btn_video_).setOnClickListener(this::onVideoAudioClicked);
        findViewById(R.id.btn_media).setOnClickListener(this::onVideoClicked);
        findViewById(R.id.btn_audio).setOnClickListener(this::onAudioClicked);
        findViewById(R.id.btn_image).setOnClickListener(this::onImageClicked);
        findViewById(R.id.btn_doc).setOnClickListener(this::onDocumentClicked);

    }
    private void onVideoOverlayClicked(View view) {
        openInternalFilePicker("video/*", SET_OVERLAY_IMAGE_ON_VIDEO);
    }
    private void onSetAudioOnVideoClicked(View view) {
        openInternalFilePicker("video/*", SELECT_VIDEO_FOR_AUDIO_VIDEO);
    }
    private void onVideoAudioClicked(View view) {
        onSetAudioOnVideoClicked(view);
//        openInternalFilePicker("video/*", SELECT_FILE_PICKER_VIDEO_TO_AUDIO);
    }

    private void onVideoClicked(View view) {
        openInternalFilePicker("video/*", SELECT_FILE_PICKER_VIDEO);
    }
    private void onAudioClicked(View view) {
        openInternalFilePicker("audio/*", SELECT_FILE_PICKER_AUDIO);
    }

    private void onImageClicked(View view) {
        openInternalFilePicker("image/*", SELECT_FILE_PICKER_IMAGE);
    }

    private void onDocumentClicked(View view) {
        Intent intent;
        if (android.os.Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
            intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
            intent.putExtra("CONTENT_TYPE", "*/*");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
        } else {

            String[] mimeTypes =
                    {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                            "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                            "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                            "text/plain",
                            "application/pdf",
                            "application/zip", "application/vnd.android.package-archive"};

            intent = new Intent(Intent.ACTION_GET_CONTENT); // or ACTION_OPEN_DOCUMENT
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        }
        startActivityForResult(Intent.createChooser(intent, "Select file "), SELECT_FILE_PICKER_DOCUMENT);
        setResult(Activity.RESULT_OK);
    }


    private static final int SET_OVERLAY_IMAGE_ON_VIDEO = 1010;
    private static final int SELECT_VIDEO_FOR_AUDIO_VIDEO = 1011;
    private static final int SELECT_FILE_PICKER_VIDEO_TO_AUDIO = 1012;
    private static final int SELECT_FILE_PICKER_VIDEO = 1013;
    private static final int SELECT_FILE_PICKER_AUDIO = 1014;
    private static final int SELECT_FILE_PICKER_IMAGE = 1015;
    private static final int SELECT_FILE_PICKER_DOCUMENT = 1016;
    private static final int SELECT_AUDIO_FOR_AUDIO_VIDEO = 1017;

    private void openInternalFilePicker(String mediaType, int filePicker) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");
        intent.setType(mediaType);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select file "), filePicker);
        setResult(Activity.RESULT_OK);
    }
    File mVideoFileSelected=null;
    File mAudioFileSelected=null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: " + requestCode);

        if (requestCode == SET_OVERLAY_IMAGE_ON_VIDEO && resultCode == Activity.RESULT_OK) {
            onResultSuccess(data, MediaType.SET_OVERLAY_ON_VIDEO);
        } else if (requestCode == SELECT_VIDEO_FOR_AUDIO_VIDEO && resultCode == Activity.RESULT_OK) {
            mVideoFileSelected = getFileFromIntent(data);
            openInternalFilePicker("audio/*", SELECT_AUDIO_FOR_AUDIO_VIDEO);
        }else if (requestCode == SELECT_AUDIO_FOR_AUDIO_VIDEO && resultCode == Activity.RESULT_OK) {
            mAudioFileSelected=getFileFromIntent(data);
            showDialogFileName(mVideoFileSelected,mAudioFileSelected,MediaType.SET_AUDIO_ON_VIDEO);
        }
        else if (requestCode == SELECT_FILE_PICKER_VIDEO && resultCode == Activity.RESULT_OK) {
            onResultSuccess(data, MediaType.VIDEO);
        } else if (requestCode == SELECT_FILE_PICKER_VIDEO_TO_AUDIO && resultCode == Activity.RESULT_OK) {
            onResultSuccess(data, MediaType.GET_AUDIO_FROM_VIDEO);
        } else if (requestCode == SELECT_FILE_PICKER_AUDIO && resultCode == Activity.RESULT_OK) {
            onResultSuccess(data, MediaType.AUDIO);
        } else if (requestCode == SELECT_FILE_PICKER_IMAGE && resultCode == Activity.RESULT_OK) {
            onResultSuccess(data, MediaType.IMAGE);
        } else if (requestCode == SELECT_FILE_PICKER_DOCUMENT && resultCode == Activity.RESULT_OK) {
            onResultSuccess(data, MediaType.DOCUMENT);
        }

    }
    private File getFileFromIntent(Intent data)
    {
        File yourFile=null;
        try {
            String Fpath = data.getDataString();
            Log.e(TAG, "onActivityResult: path ==> " + Fpath);
            Log.e(TAG, "onActivityResult: data ===> " + data.getData());
            Uri pdfUri = data.getData();
            yourFile = new File(Objects.requireNonNull(FileUtils.getFilePath(this, pdfUri)));
            Log.e(TAG, "onActivityResult: " + yourFile);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return yourFile;
    }

    private void onResultSuccess(Intent data, MediaType mediaType) {
        try {

            File yourFile = getFileFromIntent(data);
            FileUtils.showFileDetail(yourFile);
            showDialogFileName(yourFile, mediaType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialogFileName(File selectedMediaFile, MediaType mediaType) {
        CustomAlertDialog.showEditTextAlert(this, fileName -> {
            newFileName = FileUtils.getSavedPdfFilePath("FFMPEG", fileName) + "_" + System.currentTimeMillis();
            callMediaTypeCommand(selectedMediaFile, newFileName, mediaType);
        });
    }
    private void showDialogFileName(File selectedVideoFile,File selectedAudioFile, MediaType mediaType) {
        CustomAlertDialog.showEditTextAlert(this, fileName -> {
            newFileName = FileUtils.getSavedPdfFilePath("FFMPEG", fileName) + "_" + System.currentTimeMillis();
            callMediaTypeCommand(selectedVideoFile,selectedAudioFile,newFileName, mediaType);
        });
    }
    private void callMediaTypeCommand(File selectedVideoFile,File AudioFile, String fileNamePath, MediaType mediaType) {
        mProgressBar.setVisibility(View.VISIBLE);
        FFCommandExecutor ffCommandExecutor = new FFCommandExecutor(this);
        switch (mediaType) {
            case SET_OVERLAY_ON_VIDEO:
                break;
            case SET_AUDIO_ON_VIDEO:
                ffCommandExecutor.callReplaceAudioWithVideo(selectedVideoFile, AudioFile,fileNamePath, VIDEO_EXTENSIONS.MP4);
                break;
        }
    }
    private void callMediaTypeCommand(File selectedMediaFile, String fileNamePath, MediaType mediaType) {
        mProgressBar.setVisibility(View.VISIBLE);
        FFCommandExecutor ffCommandExecutor = new FFCommandExecutor(this);
        switch (mediaType) {
            case SET_OVERLAY_ON_VIDEO:
                break;
            case VIDEO:
                ffCommandExecutor.callCompressVideo(selectedMediaFile, fileNamePath, VIDEO_EXTENSIONS._3GP);
                break;
            case GET_AUDIO_FROM_VIDEO:
                ffCommandExecutor.callGetAudioFromVideo(selectedMediaFile, fileNamePath, AUDIO.MP3);
                break;
            case AUDIO:
                break;
            case IMAGE:
                break;
            case DOCUMENT:
                break;
        }
    }



//    private void executeFFmpegCmd(String[] cmd) {
//        try {
//            Log.e(TAG, "executeFFmpegCmd: "+ Arrays.toString(cmd));
////            Log.e(TAG, "executeFFmpegCmd: "+ cmd );
//
//            ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {
//
//                @Override
//                public void onStart() {
//                    Log.e(TAG, "onStart: " );
//                }
//
//                @Override
//                public void onProgress(String message) {
//                    Log.e(TAG, "onProgress: "+message );
//                }
//
//                @Override
//                public void onFailure(String message) {
//                    Log.e(TAG, "onFailure: "+message );
//                }
//
//                @Override
//                public void onSuccess(String message) {
//                    Log.e(TAG, "onSuccess: "+message );
//                }
//
//                @Override
//                public void onFinish() {
//                    Log.e(TAG, "onFinish: " );
//                }
//            });
//        } catch (FFmpegCommandAlreadyRunningException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void initialize(Context context) {
//        ffmpeg=FFmpeg.getInstance(context);
//        try {
//            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
//                @Override
//                public void onSuccess() {
//                    // FFmpeg is supported by device
//                    Log.e(TAG, "onSuccess: Supported" );
//                }
//            });
//        } catch (FFmpegNotSupportedException e) {
//            // Handle if FFmpeg is not supported by device
//        }
//    }


    @Override
    public void onSuccess() {
        hideProgress();
    }

    private void hideProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onCanceled() {
        hideProgress();
    }

    @Override
    public void onFailed() {
        hideProgress();
    }
}

