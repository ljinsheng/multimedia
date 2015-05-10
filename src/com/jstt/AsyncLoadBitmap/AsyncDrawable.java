package com.jstt.AsyncLoadBitmap;

import java.lang.ref.WeakReference;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class AsyncDrawable extends BitmapDrawable {
    private WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference=null;

    public AsyncDrawable(Resources res, Bitmap bitmap,
            BitmapWorkerTask bitmapWorkerTask) {
        super(res, bitmap);
        bitmapWorkerTaskReference =
            new WeakReference(bitmapWorkerTask);
    }
    

    public BitmapWorkerTask getBitmapWorkerTask() {
        return bitmapWorkerTaskReference.get();
    }
    
	public static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
	    if (imageView != null) {
	        final Drawable drawable = imageView.getDrawable();
	        if (drawable instanceof AsyncDrawable) {
	            final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
	            return asyncDrawable.getBitmapWorkerTask();
	        }
	     }
	     return null;
	 }

	public static boolean cancelPotentialWork(int data, ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

		if (bitmapWorkerTask != null) {
			final int bitmapData = bitmapWorkerTask.pid;
			if (bitmapData != data) {
				// Cancel previous task
				bitmapWorkerTask.cancel(true);
			} else {
				return false;
			}
		}
		// No task associated with the ImageView, or an existing task was
		// cancelled
		return true;
	}

}