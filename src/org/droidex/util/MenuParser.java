package org.droidex.util;

import android.util.Log;
import java.io.StringReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Stack;
import org.droidex.util.text.Dish;
import org.droidex.util.text.IndexedName;
import org.droidex.util.text.IndexedNameComparator;
import org.droidex.util.text.Meal;
import org.droidex.util.text.WeekDay;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class MenuParser
{
	private final Comparator orderByNameIndex = new IndexedNameComparator();

	public MenuParser() {};

	public NameOrderedDict parse(String xml)
		throws XmlPullParserException, IOException
	{
		XmlPullParser xpp = getParser(xml);
		NameOrderedDict<WeekDay> root = new NameOrderedDict<WeekDay>(orderByNameIndex);
		Stack<DictNesting> nesting = new Stack<DictNesting>();
		nesting.push(root);
		String tag, text;

		int eventType = xpp.getEventType();

		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
				case XmlPullParser.START_TAG:
					tag = xpp.getName().toLowerCase();

					if (("restaurante".equals(tag)) ||
							("vazio".equals(tag)))
						break;
					
					//d(nesting.size(), tag);

					DictNesting dict;
					IndexedName key;

					switch(nesting.size()) {
						case 1:
							key = new WeekDay(tag);
							dict = new NameOrderedDict<Meal>(orderByNameIndex);
							break;
						case 2:
							key = new Meal(tag);
							dict = new NameOrderedDict<Dish>(orderByNameIndex);
							break;
						case 3:
							key = new Dish(tag);
							dict = new NameOrderedDict.End<String>();
							break;
						default: // should not happen
							throw new XmlPullParserException("Arquivo XML inválido");
					}

					nesting.peek().put(key, dict);
					nesting.push(dict);
					break;

				case XmlPullParser.END_TAG:
					if (nesting.size() > 0)
						nesting.pop();
					break;

				case XmlPullParser.TEXT:
					text = xpp.getText().trim();
					
					if ("".equals(text))
						break;
					
					switch(nesting.size()) {
						case 1:
							//Log.d("droidex", "Hit text on level 1: " + text);
							break;
						case 2:
							//Log.d("droidex", "Hit text on level 2: " + text);
							break;
						case 3:
							//Log.d("droidex", "Hit text on level 3: " + text);
							break;
						case 4:
							//Log.d("droidex", "Hit text on level 4: " + text);
							nesting.peek().setData(text);
							break;
						default: // should not happen
							throw new XmlPullParserException("Arquivo XML inválido");
					}

					break;
			}

			eventType = xpp.next();
		}

		return root;
	}

	private XmlPullParser getParser(String xml)
		throws XmlPullParserException
	{
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser xpp = factory.newPullParser();
		xpp.setInput(new StringReader(xml));
		return xpp;
	}

	private void d(int level, String msg)
	{
		//Log.d(this.getClass().toString(), String.format(String.format("%%%ds%%s", level * 2), "", msg));
		Log.d(this.getClass().toString(), String.format("(%d) %s", level, msg));
	}
}
