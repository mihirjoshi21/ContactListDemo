package com.example.contactform;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ContactListAdapter extends BaseAdapter{

	private ArrayList<PhoneModel> phoneModels;
	private Context context;

	public ContactListAdapter(Context context, ArrayList<PhoneModel> phoneModels)
	{
		this.phoneModels = phoneModels;
		this.context = context;
	}

	@Override
	public int getCount() {
		return phoneModels.size();
	}

	@Override
	public Object getItem(int position) {
		return phoneModels.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewHolder holder;
		if(convertView==null)
		{
			holder = new ViewHolder();
			convertView   = inflater.inflate(R.layout.phone_list_items, null);
			holder.Name   = (TextView) convertView.findViewById(R.id.phone_name);
			holder.Phone  = (TextView) convertView.findViewById(R.id.phone_number);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.Name.setText(phoneModels.get(position).getName());
		if(phoneModels.get(position).isSection())
		{
			holder.Phone.setVisibility(View.GONE);
			holder.Name.setBackgroundColor(Color.GRAY);
		}
		else
		{
			holder.Phone.setVisibility(View.VISIBLE);
			holder.Phone.setText(phoneModels.get(position).getPhone());
			holder.Name.setBackgroundColor(Color.WHITE);
		}
		return convertView;
	}

	private static class ViewHolder
	{
		private TextView Name;
		private TextView Phone;
	}

}
