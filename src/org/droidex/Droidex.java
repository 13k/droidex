package org.droidex;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

class Dict extends HashMap<String, Dict> {};

class FakeDict extends Dict
{
	private String data;

	public FakeDict(String data)
	{
		this.data = data;
	}
	
	public String getData()
	{
		return data;
	}
	
	public String toString()
	{
		return data;
    }
}

abstract class IndexedName
{
	public static final int INVALID = 0;

	private String name;
	private int number;

	public IndexedName(String name)
	{
		setName(name);
	}

	public IndexedName(int number)
	{
		setNumber(number);
	}
	
	public int getNumber()
	{
		return number;
	}
	
	public void setNumber(int number)
	{
		this.number = number;
		this.name = numberToName(number);
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
		this.number = nameToNumber(name);
	}

	public String toString()
	{
		return String.format("%s [%s]", name, number);
	}

	protected abstract int nameToNumber(String name);
	protected abstract String numberToName(int number);
}

class WeekDay extends IndexedName
{
	private String date;

	public WeekDay(String name)
	{
		super(name);
	}

	public WeekDay(int number)
	{
		super(number);
	}
	
	public String getDate()
	{
		return date;
	}
	
	public void setDate(String date)
	{
		this.date = date;
	}

	public String getDisplayName()
	{
		return DateUtils.getDayOfWeekString(getNumber(), DateUtils.LENGTH_LONG);
	}

	public String getFullDisplayName()
	{
		return getDisplayName() + ((date != null) ? String.format(" (%s)", date) : "");
	}

	protected String numberToName(int number)
	{
		switch(number) {
			case Calendar.MONDAY:    return "segunda";
			case Calendar.TUESDAY:   return "terca";
			case Calendar.WEDNESDAY: return "quarta";
			case Calendar.THURSDAY:  return "quinta";
			case Calendar.FRIDAY:    return "sexta";
			case Calendar.SATURDAY:  return "sabado";
			case Calendar.SUNDAY:    return "domingo";
			default:                 return "";
		}
	}

	protected int nameToNumber(String name)
	{
		if ("domingo".equals(name)) {
			return Calendar.SUNDAY;
		} else if ("segunda".equals(name)) {
			return Calendar.MONDAY;
		} else if ("terca".equals(name)) {
			return Calendar.TUESDAY;
		} else if ("quarta".equals(name)) {
			return Calendar.WEDNESDAY;
		} else if ("quinta".equals(name)) {
			return Calendar.THURSDAY;
		} else if ("sexta".equals(name)) {
			return Calendar.FRIDAY;
		} else if ("sabado".equals(name)) {
			return Calendar.SATURDAY;
		} else {
			return INVALID;
		}
	}
}

class Meal extends IndexedName
{
	public static final int LUNCH  = 1;
	public static final int DINNER = 2;

	public Meal(String name)
	{
		super(name);
	}

	public String getDisplayName() {
		switch(getNumber()) {
			case LUNCH:  return "Almoço";
			case DINNER: return "Jantar";
			default:     return "";
		}
	}

	protected String numberToName(int number)
	{
		switch(number) {
			case LUNCH:  return "almoco";
			case DINNER: return "jantar";
			default:     return "";
		}
	}

	protected int nameToNumber(String name)
	{
		if ("almoco".equals(name)) {
			return LUNCH;
		} else if ("jantar".equals(name)) {
			return DINNER;
		} else {
			return INVALID;
		}
	}
}

class Dish extends IndexedName
{
	public static final int SALAD   = 1;
	public static final int SIDE    = 2;
	public static final int MAIN    = 3;
	public static final int DESSERT = 4;

	public Dish(String name)
	{
		super(name);
	}

	public String getDisplayName() {
		switch(getNumber()) {
			case SALAD:   return "Salada";
			case SIDE:    return "Acompanhamento";
			case MAIN:    return "Principal";
			case DESSERT: return "Sobremesa";
			default: return "";
		}
	}

	protected String numberToName(int number)
	{
		switch(number) {
			case SALAD:   return "salada";
			case SIDE:    return "acompanhamento";
			case MAIN:    return "principal";
			case DESSERT: return "sobremesa";
			default:     return "";
		}
	}

	protected int nameToNumber(String name)
	{
		if ("salada".equals(name)) {
			return SALAD;
		} else if ("acompanhamento".equals(name)) {
			return SIDE;
		} else if ("principal".equals(name)) {
			return MAIN;
		} else if ("sobremesa".equals(name)) {
			return DESSERT;
		} else {
			return INVALID;
		}
	}
}

class IndexedNameComparator implements Comparator<IndexedName>
{
	public int compare(IndexedName first, IndexedName second)
	{
		return (new Integer(first.getNumber())).compareTo(new Integer(second.getNumber()));
	}

	public boolean equals(Object other)
	{
		return super.equals(other);
	}
}

class MenuParser
{
	private static final String MENU_FEED_URL = "http://www.pcasc.usp.br/restaurante.xml";
	private static final String MENU_XML_ENCODING = "ISO-8859-1";

	private URL url;

	public MenuParser()
		throws MalformedURLException
	{
		url = new URL(MENU_FEED_URL);
	}

	public Dict parse()
		throws IOException, XmlPullParserException
	{
		XmlPullParser xpp = getParser();
		Dict root = new Dict();
		Stack<Dict> nesting = new Stack<Dict>();
		nesting.push(root);
		String tag, text;

		try {
			int eventType = xpp.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
					case XmlPullParser.START_TAG:
						tag = xpp.getName().toLowerCase();
						Dict element = new Dict();
						nesting.peek().put(tag, element);
						nesting.push(element);
						break;
					case XmlPullParser.END_TAG:
						nesting.pop();
						break;
					case XmlPullParser.TEXT:
						text = xpp.getText().trim();
						if (!"".equals(text))
							nesting.peek().put("__text__", new FakeDict(text));
						break;
				}

				eventType = xpp.next();
			}
		} catch (Exception e) {
			return null;
		}

		return root;
	}

	private InputStream getFeedStream()
		throws IOException
	{
		return url.openStream();
	}

	private XmlPullParser getParser()
		throws XmlPullParserException, IOException
	{
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser xpp = factory.newPullParser();
		InputStream in = getFeedStream();
		xpp.setInput(in, MENU_XML_ENCODING);
		return xpp;
	}
}

public class Droidex extends ExpandableListActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		
		setContentView(R.layout.menu);
		TextView statusText = (TextView) findViewById(android.R.id.empty);
		ExpandableListView menu = (ExpandableListView) findViewById(android.R.id.list);
		//View loadingText = findViewById(R.id.loading_text);
		
		try {
			MenuExpandableListAdapter adapter = new MenuExpandableListAdapter();
			//loadingText.setVisibility(View.INVISIBLE);
			setListAdapter(adapter);
			menu.expandGroup(adapter.findGroupPositionByDayOfWeek());
		} catch (UnknownHostException e) {
			statusText.setText(R.string.err_network);
		} catch (Exception e) {
			statusText.setText(R.string.err_exception);
			//Log.e("droidex", "exception", e);
			//e.printStackTrace();
		}
    }

	private class MenuExpandableListAdapter extends BaseExpandableListAdapter
	{
		WeekDay[] weekDays;
		String[][] dailyMenus;

		public MenuExpandableListAdapter()
			throws MalformedURLException, IOException, XmlPullParserException
		{
			super();
			weekDays = new WeekDay[0];
			dailyMenus = new String[0][0];
			MenuParser parser = new MenuParser();
			Dict dom = parser.parse();
			dom = dom.get("restaurante");
			processDom(dom);
			//Log.d("droidex", String.format("dom: %s", dom));
		}

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
			
			TextView textView = new TextView(Droidex.this);
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

			TextView textView = new TextView(Droidex.this);
			textView.setLayoutParams(lp);
			textView.setPadding(36, 0, 0, 0);
			textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			textView.setText(((WeekDay)getGroup(groupPosition)).getFullDisplayName());

			return textView;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}

		public boolean hasStableIds() {
			return true;
		}
		
		private void processDom(Dict dom)
		{
			IndexedNameComparator orderByNameIndex = new IndexedNameComparator();

			// This whole shit is only to sort using IndexedName(Comparator)
			Map<WeekDay, Map<Meal, Map<Dish, String>>> menu = 
				new TreeMap<WeekDay, Map<Meal, Map<Dish, String>>>(orderByNameIndex);

			for (Map.Entry<String, Dict> entry : dom.entrySet()) {
				Map<Meal, Map<Dish, String>> dayMenu = 
					new TreeMap<Meal, Map<Dish, String>>(orderByNameIndex);

				String date = "";

				for (Map.Entry<String, Dict> mealEntry : entry.getValue().entrySet()) {
					if ("data".equals(mealEntry.getKey())) {
						date = mealEntry.getValue().get("__text__").toString();
						continue;
					}

					Map<Dish, String> meal =
						new TreeMap<Dish, String>(orderByNameIndex);

					for (Map.Entry<String, Dict> dishEntry : mealEntry.getValue().entrySet()) {
						if ("vazio".equals(dishEntry.getKey()))
							continue;

						meal.put(new Dish(dishEntry.getKey()), dishEntry.getValue().get("__text__").toString());
					}

					dayMenu.put(new Meal(mealEntry.getKey()), meal);
				}

				WeekDay weekDay = new WeekDay(entry.getKey());
				weekDay.setDate(date);
				menu.put(weekDay, dayMenu);
			}

			// Now this whole shit is to actually generate the weekDays/dailyMenus Arrays
			ArrayList<WeekDay> days = new ArrayList<WeekDay>();
			ArrayList<String[]> menus = new ArrayList<String[]>();

			for (Map.Entry<WeekDay, Map<Meal, Map<Dish, String>>> entry : menu.entrySet()) {
				days.add(entry.getKey());
				
				ArrayList<String> meals = new ArrayList<String>();

				for (Map.Entry<Meal, Map<Dish, String>> mealEntry : entry.getValue().entrySet()) {
					String meal = String.format("%s\n", mealEntry.getKey().getDisplayName());
					if (mealEntry.getValue().isEmpty()) {
						meal += "  Não tem bandex! Habib's?";
					} else {
						for (Map.Entry<Dish, String> dishEntry : mealEntry.getValue().entrySet()) {
							meal += String.format("  %s: %s\n", dishEntry.getKey().getDisplayName(), dishEntry.getValue());
						}
					}
					meals.add(meal);
				}

				menus.add(meals.toArray(new String[0]));
			}

			weekDays = days.toArray(weekDays);
			dailyMenus = menus.toArray(dailyMenus);
		}

		public int findGroupPositionByDayOfWeek()
		{
			int dow = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
			return Arrays.binarySearch(weekDays, new WeekDay(dow), new IndexedNameComparator());
		}
	}
}
