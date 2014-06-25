package com.mist.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by gerard on 20/04/14.
 */
public class HolderAdapter<T> extends BaseAdapter {
    protected final LayoutInflater inflater;
    protected final Factory<? extends Holder<T>> factory;
    protected final List<T> content = new ArrayList<T>();

    public HolderAdapter(Context context, Factory<? extends Holder<T>> factory) {
        super();
        this.inflater = LayoutInflater.from(context);
        this.factory = factory;
    }

    public HolderAdapter(Context context, Factory<? extends Holder<T>> factory, Collection<T> content) {
        super();
        this.inflater = LayoutInflater.from(context);
        this.factory = factory;
        addAll(content);
    }

    public void add(T object) {
        content.add(object);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        content.remove(index);
        notifyDataSetChanged();
    }

    public Collection<T> getContent() {
        return Collections.unmodifiableList(content);
    }

    public void addAll(Collection<? extends T> collection) {
        content.addAll(collection);
        notifyDataSetChanged();
    }

    public void removeAll(Collection<? extends T> collection) {
        content.removeAll(collection);
        notifyDataSetChanged();
    }

    public void clear() {
        content.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return content.size();
    }

    @Override
    public final Object getItem(int position) {
        return content.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public final T getItemAt(int position) {
        if (position < content.size()) {
            return content.get(position);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        T item = getItemAt(position);
        if (convertView == null) {
            Holder<T> holder = factory.create();
            convertView = holder.getView(inflater, parent, item);
            convertView.setTag(holder);
        }
        Holder<T> holder = (Holder<T>) convertView.getTag();
        holder.setContent(item);
        return convertView;
    }
}
