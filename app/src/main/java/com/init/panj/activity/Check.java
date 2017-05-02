package com.init.panj.activity;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.init.panj.R;
import com.init.panj.clases.CompressImage;

import java.io.File;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

public class Check extends AppCompatActivity {
ImageView im;
    CompressImage ci;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        ci=new CompressImage();
        im= (ImageView) findViewById(R.id.im);
        viewPager= (ViewPager) findViewById(R.id.pager);
        Log.e("datapath","im "+getFilePaths().size()+ci.compressImage(getFilePaths().get(0)));
       im.setImageBitmap(ci.compressImage(getFilePaths().get(0)));
        getTag();
        /*LinearLayout root = new LinearLayout(this);
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/UCDownloads");
        if(file.isDirectory() == false)
        {
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            return;
        }
        File[] files = file.listFiles();
        int i = 1;
        for(File f : files)
        {
            if(f.isFile() || f.isDirectory())
            {
                try
                {
                    LinearLayout layout = new LinearLayout(this);
                    layout.setId(i);
                    Button text = new Button(this);
                    text.setText(f.getName());
                    text.setMinWidth(400);
                    layout.addView(text);
                    root.addView(layout);
                    i++;
                }
                catch(Exception e){}
            }
        }
        LinearLayout layout = new LinearLayout(this);
        HorizontalScrollView scroll = new HorizontalScrollView(this);
        scroll.addView(root);
        layout.addView(scroll);
        setContentView(layout);
       */
    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            Log.e("error",e.toString());
            return null;
        }
    }
    public ArrayList<String> getFilePaths()
    {


        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA};
        Cursor c = null;
        SortedSet<String> dirList = new TreeSet<String>();
        ArrayList<String> resultIAV = new ArrayList<String>();

        String[] directories = null;
        if (u != null)
        {
            c = managedQuery(u, projection, null, null, null);
        }

        if ((c != null) && (c.moveToFirst()))
        {
            do
            {
                String tempDir = c.getString(0);
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
                try{
                    dirList.add(tempDir);
                }
                catch(Exception e)
                {

                }
            }
            while (c.moveToNext());
            directories = new String[dirList.size()];
            dirList.toArray(directories);

        }

        for(int i=0;i<dirList.size();i++)
        {
            File imageDir = new File(directories[i]);
            File[] imageList = imageDir.listFiles();
            if(imageList == null)
                continue;
            for (File imagePath : imageList) {
                try {

                    if(imagePath.isDirectory())
                    {
                        imageList = imagePath.listFiles();

                    }
                    if ( imagePath.getName().contains(".jpg")|| imagePath.getName().contains(".JPG")
                            || imagePath.getName().contains(".jpeg")|| imagePath.getName().contains(".JPEG")
                            || imagePath.getName().contains(".png") || imagePath.getName().contains(".PNG")
                            || imagePath.getName().contains(".gif") || imagePath.getName().contains(".GIF")
                            || imagePath.getName().contains(".bmp") || imagePath.getName().contains(".BMP")
                            )
                    {



                        String path= imagePath.getAbsolutePath();
                        resultIAV.add(path);

                    }
                }
                //  }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return resultIAV;


    }
    public void getTag(){
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(Environment.getExternalStorageDirectory().getPath()+"/mu/ik.mp3");

        byte [] data = mmr.getEmbeddedPicture();
        String s=mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        Log.e("artist",""+s+"  "+mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)+" "+mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        //coverart is an Imageview object
        // convert the byte array to a bitmap
        if(data != null)
        {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            im.setImageBitmap(bitmap); //associated cover art in bitmap
            im.setAdjustViewBounds(true);
            im.setLayoutParams(new LinearLayout.LayoutParams(500, 500));
        }
        else
        {
            im.setImageResource(R.mipmap.ic_launcher); //any default cover resourse folder
            im.setAdjustViewBounds(true);
            im.setLayoutParams(new LinearLayout.LayoutParams(500,500 ));
        }
    }

}
