package org.droidex.adapters;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import org.droidex.util.NameOrderedDict;
import org.droidex.util.text.IndexedNameComparator;
import org.droidex.util.text.WeekDay;
import org.droidex.R;

public class MenuListAdapter
	extends BaseAdapter
{
	private Context context;
	private WeekDay[] weekDays;
	private NameOrderedDict<WeekDay> menu;	
	private int resource;

	public MenuListAdapter(Context ctx, int res, NameOrderedDict data)
	{
		super();
		
		context = ctx;
		menu = data;
		resource = res;

		processMenu();

		//Log.d("droidex", String.format("menu: %s", menu));
	}

	private void processMenu()
	{
		weekDays = menu.keySet().toArray(new WeekDay[0]);
	}

	/*
	public Object getChild(int groupPosition, int childPosition) {
		return dailyMenus[groupPosition][childPosition];
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return dailyMenus[groupPosition].length;
	}
	
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
			View convertView, ViewGroup parent)
	{	
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		
		TextView textView = new TextView(context);
		textView.setLayoutParams(lp);
		textView.setPadding(18, 0, 0, 0);
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		textView.setText(getChild(groupPosition, childPosition).toString());
		
		return textView;
	}

	public Object getGroup(int groupPosition) {
		return weekDays[groupPosition];
	}

	public int getGroupCount() {
		return weekDays.length;
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
			ViewGroup parent)
	{
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, 64);

		TextView textView = new TextView(context);
		textView.setLayoutParams(lp);
		textView.setPadding(36, 0, 0, 0);
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		textView.setText(((WeekDay)getGroup(groupPosition)).getFullDisplayName());

		return textView;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	*/

	public int getCount() {
		return weekDays.length;
	}

	public Object getItem(int pos) {
		return weekDays[pos];
	}

	public long getItemId(int pos) {
		return pos;
	}

	public View getView(int pos, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater)context.getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(resource, null);
		TextView text = (TextView) row.findViewById(R.id.label);
		text.setText(((WeekDay)getItem(pos)).getFullDisplayName());
		return row;
	}

	/*
	public boolean hasStableIds() {
		return true;
	}

	public boolean isEnabled(int pos) {
		return true;
	}
	*/

	public int findByDayOfWeek()
	{
		int dow = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		return Arrays.binarySearch(weekDays, new WeekDay(dow), new IndexedNameComparator());
	}
}
