package com.init.panjj.otherclasses;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import de.hdodenhof.circleimageview.BuildConfig;

public class MultipleShare {
    Activity f64a;
    File getImage;
    int f65i;
    File imagePath;
    Intent intent;

    public MultipleShare(Activity a) {
        this.f64a = a;
        this.intent = new Intent();
    }

    public Uri getImageUri(Bitmap inImage) {
        inImage.compress(CompressFormat.JPEG, 100, new ByteArrayOutputStream());
        return Uri.parse(Media.insertImage(this.f64a.getContentResolver(), inImage, BuildConfig.VERSION_NAME, BuildConfig.VERSION_NAME));
    }

    public void share(Bitmap image, Byte type) {
        Bitmap bm = image;
        Uri u = getImageUri(image);
        if (type == 0) {
            this.intent.setAction("android.intent.action.SEND");
            this.intent.putExtra("android.intent.extra.TEXT", BuildConfig.VERSION_NAME);
            this.intent.setType("text/plain");
            this.intent.setType("image/jpeg");
            this.intent.setPackage("com.whatsapp");
            this.intent.putExtra("android.intent.extra.STREAM", u);
            this.f64a.startActivity(this.intent);
        } else if (type == 1) {
            Intent share = new Intent("android.intent.action.SEND");
            share.setType("text/plain");
            share.setType("image/*");
            share.putExtra("android.intent.extra.TEXT", "sdfdf");
            share.putExtra("android.intent.extra.STREAM", u);
            this.f64a.startActivity(Intent.createChooser(share, "Share with these app"));
        }
    }

    public void share(String image, Byte type) {
        if (type == 0 && image != null) {
            this.intent.setAction("android.intent.action.SEND");
            this.intent.putExtra("android.intent.extra.TEXT", BuildConfig.VERSION_NAME);
            this.intent.setType("text/plain");
            this.intent.setType("image/jpeg");
            this.intent.setPackage("com.whatsapp");
            this.intent.putExtra("android.intent.extra.STREAM", Uri.parse(getImagePath(image)));
            this.f64a.startActivity(this.intent);
        } else if (type == 1 && image != null) {
            Intent share = new Intent("android.intent.action.SEND");
            share.setType("image/*");
            share.setType("text/*");
            share.putExtra("android.intent.extra.STREAM", Uri.parse(getImagePath(image)));
            share.putExtra("android.intent.extra.SUBJECT", "punj");
            this.f64a.startActivity(Intent.createChooser(share, "Share with these app"));
        }
    }

    public String getImagePath(String url) {
        File file;
        Bitmap bitmap = ImageLoader.getInstance().loadImageSync(url);
        try {
            File sharefile = new File(this.f64a.getApplicationContext().getExternalCacheDir(), "Photo-" + new Random().nextInt(50000) + ".jpg");
            try {
                FileOutputStream out;
                if (!sharefile.exists() || sharefile.delete()) {
                    out = new FileOutputStream(sharefile);
                    bitmap.compress(CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    Uri.parse("file://" + sharefile);
                } else {
                    out = new FileOutputStream(sharefile);
                    bitmap.compress(CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    Uri.parse("file://" + sharefile);
                }
                file = sharefile;
                return "file://" + sharefile;
            } catch (IOException e) {
                file = sharefile;
                return null;
            }
        } catch (Exception e2) {
            return null;
        }
    }

    public void saveBitmap(String image) {
        Bitmap bitmap = ImageLoader.getInstance().loadImageSync(image);
        this.imagePath = Environment.getExternalStorageDirectory();
        File dir = new File(this.imagePath.getAbsolutePath(), "/Gurubani/");
        if (!dir.exists()) {
            dir.mkdir();
        }
        this.getImage = new File(dir, "/" + System.currentTimeMillis() + ".jpg");
        try {
            FileOutputStream fos = new FileOutputStream(this.getImage);
            bitmap.compress(CompressFormat.JPEG, 100, fos);
            this.f64a.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(this.getImage)));
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(this.f64a, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (IOException e2) {
            Toast.makeText(this.f64a, "Error:" + e2.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (this.f65i == 0) {
            Toast.makeText(this.f64a, "View Gurubani folder in file-explorer", Toast.LENGTH_SHORT).show();
        }
    }
}
