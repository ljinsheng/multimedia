package com.jstt.PlayPhoto;

import com.jstt.AsyncLoadBitmap.AsyncDrawable;
import com.jstt.AsyncLoadBitmap.BitmapWorkerTask;
import com.jstt.multimedia.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private static final String tag = "ImageAdapter";
	int mGalleryItemBackground;
	private static Context mContext;

	public static String[] mImageIds;

	public static int photoWidth;
	public static int photoHeight;
	
	
	public ImageAdapter(Context c,String[] ImageIds,int width,int height)//context,paths
	{
		mContext = c;
		mImageIds = ImageIds;//pictures' paths
		photoWidth = width;
		photoHeight = height;
	}

	@Override
	public int getCount() {
		return mImageIds.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = new ImageView(mContext);
		Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.photo_icon);
		Log.d(tag,"bitmap set");
		imageView.setImageBitmap(bitmap);
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		/*
		 * 这里我们重点理解ImageView的属性android:scaleType，即ImageView.setScaleType(ImageView.ScaleType)。android:scaleType是控制图片如何resized/moved来匹对ImageView的size。ImageView.ScaleType / android:scaleType值的意义区别：
		 * CENTER /center  按图片的原来size居中显示，当图片长/宽超过View的长/宽，则截取图片的居中部分显示
		 * CENTER_CROP / centerCrop  按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽)
		 * CENTER_INSIDE / centerInside  将图片的内容完整居中显示，通过按比例缩小或原来的size使得图片长/宽等于或小于View的长/宽
		 * FIT_CENTER / fitCenter  把图片按比例扩大/缩小到View的宽度，居中显示
		 * FIT_END / fitEnd   把图片按比例扩大/缩小到View的宽度，显示在View的下部分位置
		 * FIT_START / fitStart  把图片按比例扩大/缩小到View的宽度，显示在View的上部分位置
		 * FIT_XY / fitXY  把图片不按比例扩大/缩小到View的大小显示
		 * MATRIX / matrix 用矩阵来绘制
		 */
	
		
		imageView.setLayoutParams(new Gallery.LayoutParams(photoWidth,photoHeight));
		imageView.setPadding(0, 0, 0, 0);

		mLoadBitmap(position,imageView);
		
		return imageView;
		
		
	}

	private void mLoadBitmap(int resId, ImageView imageView) {
		// TODO Auto-generated method stub
		if (AsyncDrawable.cancelPotentialWork(resId, imageView)) {
			final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
			Resources res = mContext.getResources();
			Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.photo_icon);
			final AsyncDrawable asyncDrawable = new AsyncDrawable(
				res,bitmap, task);
			imageView.setImageDrawable(asyncDrawable);
			task.execute(resId);
		}
	}
	
	

	/**
	 * Returns the size (0.0f to 1.0f) of the views depending on the 'offset' to
	 * the center.
	 */
	public float getScale(boolean focused, int offset) {
		/* Formula: 1 / (2 ^ offset) */
		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
	}

}
