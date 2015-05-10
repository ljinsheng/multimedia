package com.jstt.multimedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;

public class FilePathReader extends ListActivity
{
	private static final String TAG= "FilePathReader";
	private ArrayList<String> items = null;//存放名称
	private ArrayList<String> paths = null;//存放路径
	private String rootPath = "/";
	private Button btn_start = null;
	private Button btn_stop = null;
	private CheckBox cb = null;
	private Button btn_back = null;
	private TextView tv;
	private String[] path = null;
	private ListView lv = null;
	private int checkNum = 0;
	private boolean checkAll = false;
	private ListViewAdapter myAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filespath);
		lv = (ListView)findViewById(R.id.lv);
		tv = (TextView)findViewById(R.id.pathtext);
		btn_start = (Button)findViewById(R.id.btn_start);
		btn_stop = (Button)findViewById(R.id.btn_stop);
		btn_back = (Button)findViewById(R.id.btn_back);
		cb = (CheckBox)findViewById(R.id.cbox);
		initDate();
	//	getFileDir(rootPath);//获取rootPath目录下的文件
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) 
			{
				// TODO Auto-generated method stub
				if(isChecked)
				{
					checkAll = true;
					
				}
				else
				{
					checkAll = false;
				}
			}
		});
//		lv.setOnItemClickListener(new OnItemClickListener()
//		{
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
//					long arg3) 
//			{
//				// TODO Auto-generated method stub
//				ViewHolder holder = (ViewHolder)view.getTag();
//				holder.cb_cell.toggle();
//				ListViewAdapter.getIsSelected().put(arg2,holder.cb_cell.isChecked());
//				if(holder.cb_cell.isChecked()==true)
//				{
//					checkNum++;
//				}
//				else
//				{
//					checkNum--;
//				}
//				Log.d(TAG,"++++ljsh++++"+checkNum+"被选中");
//			}
//			
//		});
	}
	private void initDate()
	{
		// TODO Auto-generated method stub
		getFileDir(rootPath);
	}
	private void datachanged()
	{
		myAdapter.notifyDataSetChanged();
		Log.d(TAG,"++++ljsh++++"+checkNum+"被选中");
	}
	public void getFileDir(String filePath)
	{
		try
		{
			tv.setText("当前路径："+filePath);
			items = new ArrayList<String>();
			paths = new ArrayList<String>();
			File f = new File(filePath);
			File[] files = f.listFiles();// 列出所有文件 
			// 如果不是根目录,则列出返回根目录和上一目录选项  
//			if(!filePath.equals(rootPath))
//			{
//				items.add("返回根目录");
//				paths.add(rootPath);
//				items.add("返回上一层目录");
//				paths.add(f.getParent());
//			}
			if(files!=null)
			{
				int count = files.length;
				for(int i=0;i<count;i++)
				{
					File file =files[i];
					if(file.isDirectory())
					{
						items.add(file.getName());
						paths.add(file.getPath());
					}
					else
					{
						Log.d(TAG,"++++ljsh++++"+file.getName()+" is not a directory!");
					}
					
				}
			}
			//可以去查一相这个类  
            //this 上下文  
            //android.R.layout.simple_list_item_1 是Android显示列表每一项自己的主题  
            //item则就是根据你自己的内容来显示  
//			ArrayAdapter<String> adapter = new ArrayAdapter<String>
//			(this,R.layout.filepath_cell,items);
//			this.setListAdapter(adapter);
			myAdapter = new ListViewAdapter(items,this);
			lv.setAdapter(myAdapter);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	@Override
	protected void onListItemClick(ListView l,View view,int position,long id)
	{
		super.onListItemClick(l,view,position,id);
		String path = paths.get(position);
		final File file = new File(path);
		if(file.isDirectory())
		{
			this.getFileDir(path);
		}
		else
		{
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			alertDialog.setTitle("提示");
			alertDialog.setMessage(file.getName()+"是一个文件，你要删除这个文件吗");
			//设置左面确定
			alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					File delFile = new File(file.getAbsolutePath());
					if(delFile.exists())
					{
						Log.i("FilePath",delFile.getAbsolutePath());
						delFile.delete();
						getFileDir(file.getParent());
					}
				}
			});
			alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					getFileDir(file.getParent());
				}
			});
			alertDialog.show();
			
			ViewHolder holder = (ViewHolder)view.getTag();
			holder.cb_cell.toggle();
			ListViewAdapter.getIsSelected().put(position,holder.cb_cell.isChecked());
			if(holder.cb_cell.isChecked()==true)
			{
				checkNum++;
			}
			else
			{
				checkNum--;
			}
			Log.d(TAG,"++++ljsh++++"+checkNum+"被选中");
		}
	}
//	public static class Viewholder
//	{
//		public static TextView text;
//		public static CheckBox cb_cell;
//	}
}
