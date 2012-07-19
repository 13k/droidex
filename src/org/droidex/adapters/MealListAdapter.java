package org.droidex.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Map;

import org.droidex.R;
import org.droidex.util.L;
import org.droidex.util.NameOrderedDict;
import org.droidex.util.DictNesting;
import org.droidex.util.text.Meal;
import org.droidex.util.text.Dish;

public class MealListAdapter
    extends BaseAdapter
{
    private Context context;
    private NameOrderedDict<Meal> dayMenu;
    private Meal[] meals;
    private int mealItemRes;
    private int dishItemRes;

    public MealListAdapter(Context ctx, int mealRes, int dishRes, NameOrderedDict data)
    {
        super();
        context = ctx;
        mealItemRes = mealRes;
        dishItemRes = dishRes;
        dayMenu = data;
        meals = dayMenu.keySet().toArray(new Meal[0]);
        L.d("meals: " + Arrays.toString(meals));
    }

    public int getCount() {
        return meals.length;
    }

    public Object getItem(int pos) {
        return meals[pos];
    }

    public long getItemId(int pos) {
        return pos;
    }

    public View getView(int pos, View convertView, ViewGroup parent)
    {
        Meal meal = (Meal) getItem(pos);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View mealItem = inflater.inflate(mealItemRes, null);
        TextView mealLabel = (TextView) mealItem.findViewById(R.id.mealListItem_labelText);
        mealLabel.setText(meal.getDisplayName());
        LinearLayout dishList = (LinearLayout) mealItem.findViewById(R.id.mealListItem_dishListLayout);

        NameOrderedDict<Dish> dishes = (NameOrderedDict<Dish>) dayMenu.get(meal);

        if (dishes.isEmpty()) {
            TextView emptyText = (TextView) mealItem.findViewById(R.id.mealListItem_dishListEmptyText);
            emptyText.setText(R.string.closed);
            emptyText.setVisibility(View.VISIBLE);
            dishList.setVisibility(View.GONE);
        } else {
            for (Map.Entry<Dish, DictNesting> dishEntry : dishes.entrySet()) {
                View dishItem = inflater.inflate(dishItemRes, null);
                /*
                TextView dishLabel = (TextView) dishItem.findViewById(R.id.dishListItem_labelText);
                dishLabel.setText(dishEntry.getKey().getDisplayName());
                */
                TextView dishName = (TextView) dishItem.findViewById(R.id.dishListItem_nameText);
                dishName.setText(((NameOrderedDict.End<String>)dishEntry.getValue()).getData());
                dishList.addView(dishItem);
            }
        }

        return mealItem;
    }

    public boolean isEnabled(int pos) {
        return false;
    }
}
