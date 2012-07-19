package org.droidex.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import org.droidex.util.text.Dish;
import org.droidex.util.text.IndexedName;
import org.droidex.util.text.Meal;
import org.droidex.util.text.WeekDay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class MenuParser
{
    private static final List<String> IGNORE_TAGS;

    static {
        IGNORE_TAGS = new ArrayList<String>(Arrays.asList("restaurante", "data", "vazio"));
    }

    public static NameOrderedDict<WeekDay> parseXML(String xml)
        throws XmlPullParserException, IOException
    {
        XmlPullParser xpp = getParser(xml);
        NameOrderedDict<WeekDay> root = new NameOrderedDict<WeekDay>();
        Stack<DictNesting> nesting = new Stack<DictNesting>();
        nesting.push(root);
        String tag = "";
        String text = "";
        WeekDay currentDay = null;

        int eventType = xpp.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    tag = xpp.getName().toLowerCase();

                    if (IGNORE_TAGS.contains(tag))
                        break;
                    
                    //L.d(nesting.size(), tag);

                    DictNesting dict = null;
                    IndexedName key = null;

                    switch(nesting.size()) {
                        case 1:
                            key = new WeekDay(tag);
                            currentDay = (WeekDay) key;
                            dict = new NameOrderedDict<Meal>();
                            break;

                        case 2:
                            key = new Meal(tag);
                            dict = new NameOrderedDict<Dish>();
                            break;

                        case 3:
                            key = new Dish(tag);
                            dict = new NameOrderedDict.End<String>();
                            break;

                        default: // should not happen
                            L.d("nesting level too deep: " + nesting.size() + " tag: " + tag);
                            throw new XmlPullParserException("Arquivo XML inválido");
                    }

                    if ((key == null) || (dict == null))
                        break;

                    nesting.peek().put(key, dict);
                    nesting.push(dict);
                    break;

                case XmlPullParser.END_TAG:
                    if ((nesting.size() > 0) && (!IGNORE_TAGS.contains(tag)))
                        nesting.pop();
                    break;

                case XmlPullParser.TEXT:
                    text = xpp.getText().trim();
                    
                    if ("".equals(text))
                        break;
                    
                    switch(nesting.size()) {
                        case 1:
                            break;
                        case 2:
                            if ("data".equals(tag))
                                currentDay.setDate(text);
                            break;
                        case 3:
                            break;
                        case 4:
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

    private static XmlPullParser getParser(String xml)
        throws XmlPullParserException
    {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(new StringReader(xml));
        return xpp;
    }
}
