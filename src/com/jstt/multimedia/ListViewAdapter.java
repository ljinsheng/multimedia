package com.jstt.multimedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter
{
	private LayoutInflater inflater = null;
	private ArrayList<String> list;
	private static HashMap<Integer,Boolean> isSelected;
	private Context context;
	public List<String> mList;
	public Map<String,?> mMap;
	
	public ListViewAdapter(ArrayList<String> list,Context context)
	{
		this.context=context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		isSelected = new HashMap<Integer,Boolean>();
		initDate();
	}
	private void initDate()
	{
		for(int i=0;i<list.size();i++)
		{
			getIsSelected().put(i,false);
		}
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		// TODO Auto-generated method stub
		return isSelected;
	}
	public static void setIsSelect(HashMap<Integer,Boolean> isSelected)
	{
		ListViewAdapter.isSelected = isSelected;
	}
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(view == null)
		{
			view = inflater.inflate(R.layout.filepath_cell, null);
			holder.text=(TextView)view.findViewById(R.id.fileName);
			holder.cb_cell=(CheckBox)view.findViewById(R.id.checkbox);
			view.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)view.getTag();
		}
		holder.text.setText(list.get(position));
		holder.cb_cell.setChecked(getIsSelected().get(position));
		
		return view;
	}

}
