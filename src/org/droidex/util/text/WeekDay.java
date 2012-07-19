package org.droidex.util.text;

import android.text.format.DateUtils;
import java.util.Calendar;
import java.util.ArrayList;

public class WeekDay
    extends IndexedName
{
    private static final ArrayList<String> weekDays;

    static {
        Calendar cal = Calendar.getInstance();
        int min = cal.getMaximum(Calendar.DAY_OF_WEEK) + 1;
        //org.droidex.util.L.d("min: " + min);
        weekDays = new ArrayList<String>(min);
        weekDays.ensureCapacity(min);
        //org.droidex.util.L.d("size: " + weekDays.size());
        for (int i = 0; i < min; ++i)
            weekDays.add("");
        weekDays.add( Calendar.SUNDAY,    "domingo" );
        weekDays.add( Calendar.MONDAY,    "segunda" );
        weekDays.add( Calendar.TUESDAY,   "terca"   );
        weekDays.add( Calendar.WEDNESDAY, "quarta"  );
        weekDays.add( Calendar.THURSDAY,  "quinta"  );
        weekDays.add( Calendar.FRIDAY,    "sexta"   );
        weekDays.add( Calendar.SATURDAY,  "sabado"  );
    }

    private String date;

    public WeekDay(String name) {
        super(weekDays);
        setName(name);
    }

    public WeekDay(int i) {
        super(weekDays);
        setIndex(i);
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String d) {
        date = d;
    }

    @Override
    public String getDisplayName() {
        return DateUtils.getDayOfWeekString(getIndex(), DateUtils.LENGTH_LONG);
    }

    public String getFullDisplayName() {
        return getDisplayName() + ((date != null) ? String.format(" (%s)", date) : "");
    }
}
