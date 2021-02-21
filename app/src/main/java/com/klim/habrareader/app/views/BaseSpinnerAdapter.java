package com.klim.habrareader.app.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.klim.habrareader.R;

import java.util.ArrayList;
import java.util.List;

public class BaseSpinnerAdapter<T extends BaseSpinnerAdapter.Item> extends BaseAdapter {
    protected List<T> items = new ArrayList<>();

    protected Context context;
    protected final LayoutInflater mInflater;

    protected int mDropDownResource = android.R.layout.simple_spinner_item;
    protected int mDropDownHeaderResource = R.layout.simple_spinner_header;
    protected int tvId;
    protected int tvHeadId;
    protected Integer bgColor = null;
    protected Integer itemTextColor = null;
    protected Integer headTextColor = null;

    public BaseSpinnerAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (position < items.size()) {
            return items.get(position).getId();
        }
        return -1;
    }

    public int getPositionById(int id) {
        return getPositionById((long) id);
    }

    public int getPositionById(long id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == id) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        View view = View.inflate(context, android.R.layout.simple_spinner_item, parent);
//        return view;

        return createViewFromResource(mInflater, position, convertView, parent);
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public List<T> getItems() {
        return items;
    }

    public void setDropDownViewResource(@LayoutRes int resource, @IdRes int tvId) {
        this.mDropDownResource = resource;
        this.tvId = tvId;
        this.tvHeadId = tvId;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public void setItemTextColor(int itemTextColor) {
        this.itemTextColor = itemTextColor;
    }

    public void setHeadTextColor(int headTextColor) {
        this.headTextColor = headTextColor;
    }

    private LayoutInflater mDropDownInflater;

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final LayoutInflater inflater = mDropDownInflater == null ? mInflater : mDropDownInflater;
        return createViewFromResource(inflater, position, convertView, parent);
    }

    @Override
    public int getItemViewType(int position) {
        return ((Item) getItem(position)).getType().id;
    }

    public ItemTypes getItemViewTypeT(int position) {
        return ((Item) getItem(position)).getType();
    }

    private @NonNull
    View createViewFromResource(@NonNull LayoutInflater inflater, int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if (convertView != null && getItemViewTypeT(position) == convertView.getTag()) {
            view = convertView;
        } else {
            switch (getItemViewTypeT(position)) {
                case HEADER: {
                    view = inflater.inflate(mDropDownHeaderResource, parent, false);
                    if (headTextColor != null) {
                        view.findViewById(R.id.vDivider).setBackgroundColor(headTextColor);
                    }
                    break;
                }
                case ITEM: {
                    view = inflater.inflate(mDropDownResource, parent, false);
                    break;
                }
            }
            view.setTag(getItemViewTypeT(position));
            if (bgColor != null) {
                view.setBackgroundColor(bgColor);
            }
        }

        onBind(getItem(position), getItemViewTypeT(position), view);

        return view;
    }

    protected void onBind(T item, ItemTypes type, @NonNull View view) {
        TextView text = null;
        switch (type) {
            case HEADER: {
                if (tvHeadId == 0) {
                    text = (TextView) view;
                } else {
                    text = view.findViewById(tvHeadId);
                    if (text == null) {
                        throw new RuntimeException("Failed to find view with ID " + context.getResources().getResourceName(tvId) + " in item layout");
                    }
                }
                if (headTextColor != null) {
                    text.setTextColor(headTextColor);
                }
                break;
            }
            case ITEM: {
                if (tvId == 0) {
                    text = (TextView) view;
                } else {
                    text = view.findViewById(tvId);
                    if (text == null) {
                        throw new RuntimeException("Failed to find view with ID " + context.getResources().getResourceName(tvId) + " in item layout");
                    }
                }
                if (itemTextColor != null) {
                    text.setTextColor(itemTextColor);
                }
                break;
            }
        }

        if (item instanceof Item) {
            text.setText(item.getName());
        } else {
            text.setText(item.toString());
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewTypeT(position) == ItemTypes.ITEM;
    }

    public enum ItemTypes {
        HEADER(1),
        ITEM(0);

        int id = 0;

        ItemTypes(int id) {
            this.id = id;
        }
    }

    public interface Item {
        ItemTypes getType();

        String getName();

        long getId();
    }

    public static class ItemSimple implements Item {

        private int id;
        private String name;
        private ItemTypes type = ItemTypes.ITEM;

        public ItemSimple(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public ItemSimple(int id, String name, ItemTypes type) {
            this.id = id;
            this.name = name;
            this.type = type;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setType(ItemTypes type) {
            this.type = type;
        }

        @Override
        public long getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public ItemTypes getType() {
            return type;
        }
    }
}
