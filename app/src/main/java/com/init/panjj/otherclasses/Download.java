package com.init.panjj.otherclasses;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.BuildConfig;

public class Download extends AsyncTask<String, Void, String> {
    Context context;
    ProgressDialog mProgressDialog;
    String songname;
    String urlDownload;

    public Download(Context context, String url, String name) {
        this.context = context;
        this.urlDownload = url;
        this.songname = name;
    }

    protected void onPreExecute() {
        this.mProgressDialog = ProgressDialog.show(this.context, BuildConfig.VERSION_NAME, "Downloading Started...");
        this.mProgressDialog.setCancelable(true);
    }

    protected String doInBackground(String... params) {
        DownloadManager dm = (DownloadManager) this.context.getSystemService(Context.DOWNLOAD_SERVICE);
        Request request = new Request(Uri.parse(this.urlDownload));
        Log.e("urlDownload", this.urlDownload);
        long enqueue = dm.enqueue(request);
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(this.urlDownload).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            File folder = new File(Environment.getExternalStorageDirectory(), "panj");
            if (!folder.exists()) {
                boolean success = folder.mkdir();
            }
            FileOutputStream fileOutput = new FileOutputStream(new File(folder, this.songname + ".mp4"));
            InputStream inputStream = urlConnection.getInputStream();
            int totalSize = urlConnection.getContentLength();
            int downloadedSize = 0;
            byte[] buffer = new byte[AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT];
            while (true) {
                int bufferLength = inputStream.read(buffer);
                if (bufferLength <= 0) {
                    break;
                }
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                Log.w("DOWNLOAD", "progress " + downloadedSize + " / " + totalSize);
            }
            fileOutput.close();
        } catch (MalformedURLException e) {
            Log.e("DOWNLOAD", "ERROR : " + e);
        } catch (IOException e2) {
            Log.e("DOWNLOAD", "ERROR : " + e2);
        }
        return "done";
    }

    protected void onPostExecute(String result) {
        Toast.makeText(this.context, "Downloading Started", Toast.LENGTH_SHORT).show();
        this.mProgressDialog.dismiss();
    }
}
