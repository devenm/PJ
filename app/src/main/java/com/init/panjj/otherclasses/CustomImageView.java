package com.init.panjj.otherclasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class CustomImageView extends ImageView {
    int height;
    int width;

    /* renamed from: com.init.panj.clases.CustomImageView.1 */
    class C05311 implements Runnable {
        final /* synthetic */ String val$imgurl;

        /* renamed from: com.init.panj.clases.CustomImageView.1.1 */
        class C10701 extends SimpleImageLoadingListener {
            C10701() {
            }

            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                CustomImageView.this.setImageBitmap(loadedImage);
            }
        }

        C05311(String str) {
            this.val$imgurl = str;
        }

        public void run() {
            ImageLoader.getInstance().loadImage(this.val$imgurl, new ImageSize(CustomImageView.this.getWidth(), CustomImageView.this.getHeight()), new Builder().resetViewBeforeLoading(true).cacheOnDisk(true).imageScaleType(ImageScaleType.NONE_SAFE).bitmapConfig(Config.RGB_565).considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build(), new C10701());
        }
    }

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        Log.e("hw", h + " " + w);
    }

    public void scaledImage(String imgurl) {
        post(new C05311(imgurl));
    }

    public void addOnLayoutChangeListener(OnLayoutChangeListener listener) {
        super.addOnLayoutChangeListener(listener);
    }
}
