package com.init.panjj.otherclasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import com.wang.avi.indicators.LineScalePartyIndicator;

public class Bluer {
    Context activity;

    public Bluer(Context mContext) {
        this.activity = mContext;
    }

    public Bitmap blur(Bitmap image) {
        if (image == null) {
            return null;
        }
        Bitmap outputBitmap = Bitmap.createBitmap(image);
        RenderScript renderScript = RenderScript.create(this.activity);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(10.0f);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

    public Bitmap blur1(Bitmap bkg) {
        int inputWidth = bkg.getWidth();
        int inputHeight = bkg.getHeight();
        Bitmap overlay = Bitmap.createBitmap((int) (((float) inputWidth) / 15.0f), (int) (((float) inputHeight) / 15.0f), Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(LineScalePartyIndicator.SCALE / 15.0f, LineScalePartyIndicator.SCALE / 15.0f);
        Paint paint = new Paint();
        paint.setFlags(2);
        canvas.drawBitmap(bkg, 0.0f, 0.0f, paint);
        return getResizedBitmap(FastBlur.doBlur(overlay, 10, true), inputWidth, inputHeight, true);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth, boolean willDelete) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / ((float) width);
        float scaleHeight = ((float) newHeight) / ((float) height);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        if (willDelete) {
            bm.recycle();
        }
        return resizedBitmap;
    }
}
