package com.jstt.PlayPhoto;

import java.io.File;
import java.util.LinkedList;

import com.jstt.multimedia.R;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private LinkedList<ImageAlbumsInfo> ImageAlbumsInfo;
	private Context mContext;

	public ListViewAdapter(Context context, LinkedList<ImageAlbumsInfo> ImageAlbumsInfos) {
		mInflater = LayoutInflater.from(context);
		ImageAlbumsInfo = ImageAlbumsInfos;
		mContext = context;
	}

	public int getCount() {
		return ImageAlbumsInfo.size();
	}

	public Object getItem(int position) {
		return ImageAlbumsInfo.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_row, null);
			// convertView.setDrawingCacheBackgroundColor(Color.TRANSPARENT);
			holder = new ViewHolder();

			holder.icon = (ImageView) convertView.findViewById(R.id.icon);

			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.path = (TextView) convertView.findViewById(R.id.path);
			// holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.picturecount = (TextView) convertView
					.findViewById(R.id.picturecount);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (ImageAlbumsInfo == null) {
			Log.i("ListViewAdapter", "ImageAlbumsInfo is null!");
			return convertView;
		}
		if (ImageAlbumsInfo.get(position) == null) {
			Log.i("ListViewAdapter", "ImageAlbumsInfo.get(position) is null!");
			return convertView;
		}
		File f = new File(ImageAlbumsInfo.get(position).path);
		String fName = f.getName();
		holder.icon.setImageBitmap(ImageAlbumsInfo.get(position).icon);

		holder.name.setText(fName);
		holder.path.setText(ImageAlbumsInfo.get(position).path);
		holder.picturecount.setText(ImageAlbumsInfo.get(position).picturecount + " "
				+ (String) mContext.getText(R.string.picture_count));
		return convertView;
	}

	/* class ViewHolder */
	private class ViewHolder {
		TextView name;
		TextView path;
		// TextView date;
		TextView picturecount;
		ImageView icon;
	}
}
