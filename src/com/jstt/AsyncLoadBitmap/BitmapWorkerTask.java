package com.jstt.AsyncLoadBitmap;

import java.lang.ref.WeakReference;

import com.jstt.PlayPhoto.GalleryFlow;
import com.jstt.PlayPhoto.ImageAdapter;
import com.jstt.PlayVideo.VideoShow;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

public class BitmapWorkerTask extends AsyncTask<Integer, Bitmap, Bitmap> {
	private final static String tag = "bitmapworkertask";
	private final WeakReference imageViewReference;
	public int pid = 0;
	private Resources res;
	public static int typeFlag = 0;
	public static Context mContext = null;

	public static void init(Context c, int i) {
		typeFlag = i;
		mContext = c;
	}

	public BitmapWorkerTask(ImageView imageView) {
		// Use a WeakReference to ensure the ImageView can be garbage collected
		imageViewReference = new WeakReference(imageView);
	}

	// Decode image in background.
	@Override
	public Bitmap doInBackground(Integer... params) {
		pid = params[0];
		switch (typeFlag) {
		case 1:
			Log.d(tag, "图片异步加载");
			return createReflectedBitmap(ImageAdapter.mImageIds, pid);
		case 2:
			Log.d(tag, "音乐异步加载");
			return decodeSampledBitmapFromResource(res, pid, 100, 100);
		case 3:
			Log.d(tag, "视频缩略图异步加载");
			Log.d(tag, "data = " + pid);
			return getVideoThumbnail(VideoShow.playList.get(pid).path, 110, 80,
					MediaStore.Images.Thumbnails.MICRO_KIND);
		default:
			return decodeSampledBitmapFromResource(res, pid, 100, 100);
		}
	}

	// Once complete, see if ImageView is still around and set bitmap.
	public void onPostExecute(Bitmap bitmap) {
		if (isCancelled()) {
			bitmap = null;
		}
		if (imageViewReference != null && bitmap != null) {
			final ImageView imageView = (ImageView) imageViewReference.get();
			final BitmapWorkerTask bitmapWorkerTask = AsyncDrawable
					.getBitmapWorkerTask(imageView);
			if (this == bitmapWorkerTask && imageView != null) {
				imageView.setImageBitmap(bitmap);
			}
		}
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}

	/**
	 * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
	 * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
	 * 
	 * @param videoPath
	 *            视频的路径
	 * @param width
	 *            指定输出视频缩略图的宽度
	 * @param height
	 *            指定输出视频缩略图的高度度
	 * @param kind
	 *            参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
	 *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
	 * @return 指定大小的视频缩略图
	 */
	private Bitmap getVideoThumbnail(String videoPath, int width, int height,
			int kind) {
		Bitmap bitmap = null;
		// 获取视频的缩略图
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/*
	 * 异步load图片及创建倒影效果
	 */
	private Bitmap createReflectedBitmap(String[] paths, int position) {
		// 倒影图和原图之间的距离
		final int reflectionGap = 4;

		/*
		 * 获取成比例的图片
		 * BitmapFactory类提供了几个解码图片的方法（decodeByteArray(),decodeFile(),decodeResource
		 * ()等）， 它们都可以通过BitmapFactory.Options指定解码选项。
		 * 设置inJustDecodeBounds属性为true时解码并不会生成Bitmap对象，
		 * 而是返回图片的解码信息（图片分辨率及类型：outWidth,outHeight,outMimeType）然后通过分辨率可以算出缩放值，
		 * 再将inJustDecodeBounds设置为false
		 * ,传入缩放值缩放图片，值得注意的是inJustDecodeBounds可能小于0，需要做判断
		 */
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Log.d(tag, "imageId" + paths[position]);
		Bitmap bitmap = BitmapFactory.decodeFile(paths[position], options);
		options.inJustDecodeBounds = false;
		int be = (int) (options.outHeight / (float) 400);
		if (be <= 0)
			be = 1;
		options.inSampleSize = be;
		Log.d(tag, "be = " + be);
		bitmap = BitmapFactory.decodeFile(paths[position], options);
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		// 创建矩阵对象
		Matrix matrix = new Matrix();

		// 指定一个角度以0,0为坐标进行旋转
		// matrix.setRotate(30);

		// 指定矩阵(x轴不变，y轴相反)
		matrix.preScale(1, -1);

		// 将矩阵应用到该原图之中，返回一个宽度不变，高度为原图1/2的倒影位图
		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
				width, height / 2, matrix, false);

		// 创建一个宽度不变，高度为原图+倒影图高度的位图
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);

		// 将上面创建的位图初始化到画布
		Canvas canvas = new Canvas(bitmapWithReflection);
		// Draw in the original image
		canvas.drawBitmap(bitmap, 0, 0, null);
		// Draw in the gap
		Paint deafaultPaint = new Paint();
//		canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);
		// Draw in the reflection
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		// Create a shader that is a linear gradient that covers the
		// reflection
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		
		/**
		 * 参数一:为渐变起初点坐标x位置， 参数二:为y轴位置， 参数三和四:分辨对应渐变终点， 最后参数为平铺方式，
		 * 这里设置为镜像Gradient是基于Shader类，所以我们通过Paint的setShader方法来设置这个渐变
		 */
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		// 设置阴影
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// 用已经定义好的画笔构建一个矩形阴影渐变效果
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}
}