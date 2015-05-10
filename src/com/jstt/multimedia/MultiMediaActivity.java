package com.jstt.multimedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jstt.PlayPhoto.PhotoAlbums;
import com.jstt.PlayVideo.VideoShow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class MultiMediaActivity extends Activity implements OnItemSelectedListener, OnItemClickListener
{
	private String namestr[]=new String[]{"图片","音乐","视频"};
	private int imgstr[]=new int[]{R.drawable.item1, R.drawable.item2, R.drawable.item3};

    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        GridView grid = (GridView)findViewById(R.id.gridview);
        List<Map<String,Object>> cells = new ArrayList<Map<String,Object>>();
        for(int i=0;i<imgstr.length;i++)
        {
        	Map<String,Object> cell = new HashMap<String,Object>();
        	cell.put("imageview", imgstr[i]);
        	cell.put("textview",namestr[i]);
        	cells.add(cell);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,cells,
        		R.layout.cell,new String[]
        				{"imageview","textview"},new int[]
        						{R.id.imageview1,R.id.textview});
        grid.setAdapter(simpleAdapter);
        grid.setOnItemSelectedListener(this);
		grid.setOnItemClickListener(this);
		grid.requestFocus();
		grid.requestFocusFromTouch();
    }

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		Intent intent = new Intent();
		switch(position)
		{
		case 0:
			intent.setClass(this, PhotoAlbums.class);
			startActivity(intent);
			break;
		case 1:
			intent.setClass(this, MusicShow.class);
			startActivity(intent);
//			finish();
			break;
		case 2:
			intent.setClass(this, VideoShow.class);
			startActivity(intent);
//			finish();
			break;
			default:
				break;
		}
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
}
