package org.droidex.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;

import org.droidex.R;
import org.droidex.util.L;
import org.droidex.util.NameOrderedDict;
import org.droidex.util.text.WeekDay;
import org.droidex.util.text.Meal;

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

		//L.d(String.format("menu: %s", menu));
	}

	private void processMenu() {
		weekDays = menu.keySet().toArray(new WeekDay[0]);
	}

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
		WeekDay weekDay = (WeekDay) getItem(pos);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);
		View dayItem = inflater.inflate(resource, null);
		TextView dayLabel = (TextView) dayItem.findViewById(R.id.menuListItem_labelText);
		String dayName = weekDay.getDisplayName();
		dayLabel.setText(dayName);
		TextView dateText = (TextView) dayItem.findViewById(R.id.menuListItem_dateText);
		String dateString = weekDay.getDate();
		dateText.setText(dateString);
		return dayItem;
	}

	public boolean isEnabled(int pos) {
		return true;
	}

	public int findByDayOfWeek()
	{
		int dow = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		return Arrays.binarySearch(weekDays, new WeekDay(dow));
	}

	public NameOrderedDict<Meal> getDayMenu(int pos)
	{
		WeekDay day = (WeekDay) getItem(pos);
		return (NameOrderedDict<Meal>) menu.get(day);
	}
}
