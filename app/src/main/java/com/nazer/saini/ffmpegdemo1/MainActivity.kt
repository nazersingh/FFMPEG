package com.nazer.saini.ffmpegdemo1

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.util.Objects

/**
 * Commands
 * https://github.com/arthenica/ffmpeg-kit/tree/main/android
 * https://gist.github.com/protrolium/e0dbd4bb0f1a396fcb55
 * https://www.catswhocode.com/blog/19-ffmpeg-commands-for-all-needs
 */
class MainActivity : AppCompatActivity(), FFCommanderCallback {
    var TAG = this.javaClass.simpleName

    //FFmpeg ffmpeg;
    var newFileName = ""
    private var mProgressBar: ProgressBar? = null
    var mVideoFileSelected: File? = null
    var mAudioFileSelected: File? = null
    companion object {
        private const val SET_OVERLAY_IMAGE_ON_VIDEO = 1010
        private const val SELECT_VIDEO_FOR_AUDIO_VIDEO = 1011
        private const val SELECT_FILE_PICKER_VIDEO_TO_AUDIO = 1012
        private const val SELECT_FILE_PICKER_VIDEO = 1013
        private const val SELECT_FILE_PICKER_AUDIO = 1014
        private const val SELECT_FILE_PICKER_IMAGE = 1015
        private const val SELECT_FILE_PICKER_DOCUMENT = 1016
        private const val SELECT_AUDIO_FOR_AUDIO_VIDEO = 1017
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mProgressBar = findViewById(R.id.progress_bar)
        // initialize(MainActivity.this);

//        String[] cmd = {"-ss", String.valueOf(currentVideoPosition), "-i", "/storage/emulated/0/Videos/01.mp4",
//                "-frames:v", "1", "/storage/emulated/0/Videos/out"+ count +".jpg"};
//        String cmd = new String("-i {filepath}{filenameRaw}.mp4 -i {filepath}{filenameRaw}.mp4 -i {filepath}{filenameRaw}.mp4 -i {filepath}{filenameRaw}.mp4 -filter_complex \"[0:v][1:v]hstack[top]; [2:v][3:v]hstack[bottom]; [top][bottom]vstack\" {filenameCropped}.mp4");
        findViewById<View>(R.id.btn_video_to_over_logo).setOnClickListener { view: View -> onVideoOverlayClicked(view) }
        findViewById<View>(R.id.btn_set_audio_on_video).setOnClickListener { view: View -> onSetAudioOnVideoClicked(view) }
        findViewById<View>(R.id.btn_video_).setOnClickListener { view: View -> onVideoAudioClicked(view) }
        findViewById<View>(R.id.btn_media).setOnClickListener { view: View -> onCompressVideoClick(view) }
        findViewById<View>(R.id.btn_audio).setOnClickListener { view: View -> onAudioClicked(view) }
        findViewById<View>(R.id.btn_image).setOnClickListener { view: View -> onImageClicked(view) }
        findViewById<View>(R.id.btn_doc).setOnClickListener { view: View -> onDocumentClicked(view) }
    }

    private fun onVideoOverlayClicked(view: View) {
        openInternalFilePicker("video/*", SET_OVERLAY_IMAGE_ON_VIDEO)
    }

    private fun onSetAudioOnVideoClicked(view: View) {
        openInternalFilePicker("video/*", SELECT_VIDEO_FOR_AUDIO_VIDEO)
    }

    private fun onVideoAudioClicked(view: View) {
        openInternalFilePicker("video/*", SELECT_FILE_PICKER_VIDEO_TO_AUDIO);
    }

    private fun onCompressVideoClick(view: View) {
        openInternalFilePicker("video/*", SELECT_FILE_PICKER_VIDEO)
    }

    private fun onAudioClicked(view: View) {
        openInternalFilePicker("audio/*", SELECT_FILE_PICKER_AUDIO)
    }

    private fun onImageClicked(view: View) {
        openInternalFilePicker("image/*", SELECT_FILE_PICKER_IMAGE)
    }

    private fun onDocumentClicked(view: View) {
        val intent: Intent
        if (Build.MANUFACTURER.equals("samsung", ignoreCase = true)) {
            intent = Intent("com.sec.android.app.myfiles.PICK_DATA")
            intent.putExtra("CONTENT_TYPE", "*/*")
            intent.addCategory(Intent.CATEGORY_DEFAULT)
        } else {
            val mimeTypes = arrayOf(
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",  // .doc & .docx
                "application/vnd.ms-powerpoint",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation",  // .ppt & .pptx
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",  // .xls & .xlsx
                "text/plain",
                "application/pdf",
                "application/zip",
                "application/vnd.android.package-archive"
            )
            intent = Intent(Intent.ACTION_GET_CONTENT) // or ACTION_OPEN_DOCUMENT
            intent.type = "*/*"
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        }
        startActivityForResult(
            Intent.createChooser(intent, "Select file "),
            SELECT_FILE_PICKER_DOCUMENT
        )
        setResult(RESULT_OK)
    }

    private fun openInternalFilePicker(mediaType: String, filePicker: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        //        intent.setType("*/*");
        intent.type = mediaType
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(Intent.createChooser(intent, "Select file "), filePicker)
        setResult(RESULT_OK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e(TAG, "onActivityResult: $requestCode")
        if (requestCode == SET_OVERLAY_IMAGE_ON_VIDEO && resultCode == RESULT_OK) {
            onResultSuccess(data, MediaType.SET_OVERLAY_ON_VIDEO)
        } else if (requestCode == SELECT_VIDEO_FOR_AUDIO_VIDEO && resultCode == RESULT_OK) {
            mVideoFileSelected = getFileFromIntent(data)
            openInternalFilePicker("audio/*", SELECT_AUDIO_FOR_AUDIO_VIDEO)
        } else if (requestCode == SELECT_AUDIO_FOR_AUDIO_VIDEO && resultCode == RESULT_OK) {
            mAudioFileSelected = getFileFromIntent(data)
            showDialogFileName(mVideoFileSelected, mAudioFileSelected, MediaType.SET_AUDIO_ON_VIDEO)
        } else if (requestCode == SELECT_FILE_PICKER_VIDEO && resultCode == RESULT_OK) {
            onResultSuccess(data, MediaType.VIDEO)
        } else if (requestCode == SELECT_FILE_PICKER_VIDEO_TO_AUDIO && resultCode == RESULT_OK) {
            onResultSuccess(data, MediaType.GET_AUDIO_FROM_VIDEO)
        } else if (requestCode == SELECT_FILE_PICKER_AUDIO && resultCode == RESULT_OK) {
            onResultSuccess(data, MediaType.AUDIO)
        } else if (requestCode == SELECT_FILE_PICKER_IMAGE && resultCode == RESULT_OK) {
            onResultSuccess(data, MediaType.IMAGE)
        } else if (requestCode == SELECT_FILE_PICKER_DOCUMENT && resultCode == RESULT_OK) {
            onResultSuccess(data, MediaType.DOCUMENT)
        }
    }

    private fun getFileFromIntent(data: Intent?): File? {
        var yourFile: File? = null
        try {
            val Fpath = data!!.dataString
            Log.e(TAG, "onActivityResult: path ==> $Fpath")
            Log.e(TAG, "onActivityResult: data ===> " + data.data)
            val pdfUri = data.data
            yourFile = File(Objects.requireNonNull(FileUtils.getFilePath(this, pdfUri)))
            Log.e(TAG, "onActivityResult: $yourFile")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return yourFile
    }

    private fun onResultSuccess(data: Intent?, mediaType: MediaType) {
        try {
            val yourFile = getFileFromIntent(data)
            FileUtils.showFileDetail(yourFile)
            showDialogFileName(yourFile, mediaType)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showDialogFileName(selectedMediaFile: File?, mediaType: MediaType) {
        CustomAlertDialog.showEditTextAlert(this) { fileName: String? ->
            newFileName = FileUtils.getSavedPdfFilePath("FFMPEG", fileName) + "_" + System.currentTimeMillis()
            callMediaTypeCommand(selectedMediaFile, newFileName, mediaType)
        }
    }

    private fun showDialogFileName(
        selectedVideoFile: File?,
        selectedAudioFile: File?,
        mediaType: MediaType
    ) {
        CustomAlertDialog.showEditTextAlert(this) { fileName: String? ->
            newFileName =
                FileUtils.getSavedPdfFilePath("FFMPEG", fileName) + "_" + System.currentTimeMillis()
            callMediaTypeCommand(selectedVideoFile, selectedAudioFile, newFileName, mediaType)
        }
    }

    private fun callMediaTypeCommand(
        selectedVideoFile: File?,
        AudioFile: File?,
        fileNamePath: String,
        mediaType: MediaType
    ) {
        mProgressBar!!.visibility = View.VISIBLE
        val ffCommandExecutor = FFCommandExecutor(this)
        when (mediaType) {
            MediaType.SET_OVERLAY_ON_VIDEO -> {}
            MediaType.SET_AUDIO_ON_VIDEO -> selectedVideoFile?.let {
                if (AudioFile != null) {
                    ffCommandExecutor.callReplaceAudioWithVideo(
                        it,
                        AudioFile,
                        fileNamePath,
                        VIDEO_EXTENSIONS.MP4
                    )
                }
            }

            else -> {}
        }
    }

    private fun callMediaTypeCommand(
        selectedMediaFile: File?,
        fileNamePath: String,
        mediaType: MediaType
    ) {
        mProgressBar!!.visibility = View.VISIBLE
        val ffCommandExecutor = FFCommandExecutor(this)
        when (mediaType) {
            MediaType.SET_OVERLAY_ON_VIDEO -> {}
            MediaType.VIDEO -> selectedMediaFile?.let {
                ffCommandExecutor.callCompressVideo(
                    it,
                    fileNamePath,
                    VIDEO_EXTENSIONS._3GP
                )
            }

            MediaType.GET_AUDIO_FROM_VIDEO -> selectedMediaFile?.let {
                ffCommandExecutor.callGetAudioFromVideo(
                    it,
                    fileNamePath,
                    AUDIO.MP3
                )
            }

            MediaType.AUDIO -> {}
            MediaType.IMAGE -> {}
            MediaType.DOCUMENT -> {}
            else -> {}
        }
    }

    override fun onSuccess() {
        hideProgress()
    }

    private fun hideProgress() {
        runOnUiThread { mProgressBar!!.visibility = View.GONE }
    }

    override fun onCanceled() {
        hideProgress()
    }

    override fun onFailed() {
        hideProgress()
    }


}