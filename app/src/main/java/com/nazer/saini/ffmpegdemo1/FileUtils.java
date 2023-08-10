package com.nazer.saini.ffmpegdemo1;

import static android.os.Build.VERSION_CODES.Q;


import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

public class FileUtils {
    private static final String TAG="FileUtils";
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    public static String getSavedPdfFilePath(String folderName, String fileName) {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
//        String filePath = Environment.getExternalStorageDirectory().toString();
        if (isExternalStorageWritable()) {
            if (Build.VERSION.SDK_INT > Q) {
                filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separatorChar + fileName;
            } else {
                File myDir = new File(filePath, File.separatorChar + folderName);
                if (!myDir.exists()) {
                    myDir.mkdirs();
                }
                filePath = myDir.getAbsolutePath() + File.separatorChar + fileName;
            }
        }

        Log.e("iTextPdfWriter", "getSavedPdfFilePath: " + isExternalStorageWritable());
        Log.e("iTextPdfWriter", "getSavedPdfFilePath: " + filePath);
        return filePath;
    }
    public static void showFileDetail(File file)
    {
//        file=new File("/storage/emulated/0/DCIM/Camera/VID_20180707_023942.mp4");
//        file=new File("/storage/emulated/0/music/Aisi Taisi.mp3");
//        file=new File("/storage/emulated/0/Download/images.jpeg");
        long size=file.length();
        long filesize=size/1024;
        Log.e("FileUtils", "onCreate: File  "+filesize+"    "+file.length()+" "+file.getAbsolutePath());
    }

    private static boolean copyFile(Context context, Uri uri, String dstPath) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            outputStream = new FileOutputStream(dstPath);

            byte[] buff = new byte[100 * 1024];
            int len;
            while ((len = inputStream.read(buff)) != -1) {
                outputStream.write(buff, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public static String getFileName(Context context, Uri uri) {

        Cursor cursor = context.getContentResolver().query(uri,null,null,null,null);
        int nameindex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();

        return  cursor.getString(nameindex);
    }
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        Log.e(TAG, "getFilePath: " +DocumentsContract.isDocumentUri(context.getApplicationContext(), uri));
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
//                final String id = DocumentsContract.getDocumentId(uri);
//                uri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));


                String dstPath = context.getCacheDir().getAbsolutePath() + File.separator + getFileName(context,uri);

                if (copyFile(context, uri, dstPath)) {
                    Log.d(TAG, "copy file success: " + dstPath);
                    return dstPath;

                } else {
                    Log.d(TAG, "copy file fail!");
                }
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {


            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
