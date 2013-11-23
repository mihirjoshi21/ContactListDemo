/*******************************************************************************
 * Copyright 2012, 2013 Mihir Joshi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.example.contactform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by
 * @author Mihir (MJ)
 * @since 23/11/2013
 *
 */
public class MainActivity extends Activity implements OnScrollListener{

	ListView 			  listView;
	ArrayList<PhoneModel> phoneModels;
	ArrayList<PhoneModel> phoneModelsSection;
	LinearLayout 		  sideIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listView = (ListView)findViewById(R.id.list_contact);

		/**
		 * ArrayList Of Phone Contacts Model
		 */
		phoneModels = new ArrayList<PhoneModel>();
		new getContactTask().execute((Void[])null);

	}

	private class getContactTask extends AsyncTask<Void, Void, Void>
	{
		ProgressDialog progressDialog;
		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(MainActivity.this);
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		@SuppressLint("DefaultLocale")
		@Override
		protected Void doInBackground(Void... params) {
			Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
			while (phones.moveToNext())
			{
				PhoneModel phoneModel = new PhoneModel();
				phoneModel.setName(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).toUpperCase(Locale.ENGLISH));
				phoneModel.setPhone(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
				phoneModels.add(phoneModel);
			}
			phones.close();
			Collections.sort(phoneModels, new PhoneModel());
			/**
			 * Get Sections
			 */
			String section = "";
			phoneModelsSection = new ArrayList<PhoneModel>();
			for(PhoneModel model : phoneModels)
			{
				if(!section.equals(model.getName().substring(0, 1)))
				{					
					section = model.getName().substring(0, 1);
					PhoneModel phoneModel = new PhoneModel();
					phoneModel.setName(section);
					phoneModel.setSection(true);
					phoneModelsSection.add(phoneModel);
				}
			}
			phoneModels.addAll(phoneModelsSection);
			Collections.sort(phoneModels, new PhoneModel());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			sideView(phoneModelsSection);
			progressDialog.dismiss();
			listView.setAdapter(new ContactListAdapter(getApplicationContext(), phoneModels));
		}

	}


	/**
	 * Implement Side View
	 * @param phoneModelsSection 
	 */
	@SuppressWarnings("deprecation")
	public void sideView(ArrayList<PhoneModel> phoneModelsSection)
	{
		sideIndex = (LinearLayout) findViewById(R.id.sideIndex);
		int sideIndexHeight = MainActivity.this.getWindowManager().getDefaultDisplay().getHeight();
		sideIndex.removeAllViews();

		// TextView for every visible item
		TextView sectors = null;

		// maximal number of item, which could be displayed
		int indexMaxSize = (int) Math.floor(sideIndexHeight / phoneModelsSection.size());


		for (int i = 0; i < phoneModelsSection.size(); i++)
		{
			sectors = new TextView(this);
			sectors.setText(phoneModelsSection.get(i).getName());
			//sectors.setTextSize(indexMaxSize);
			sectors.setGravity(Gravity.CENTER);
			sectors.setTextColor(Color.BLACK);
			sectors.setOnClickListener(new SideSectorClick());
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, indexMaxSize, 1);
			sectors.setLayoutParams(params);
			sideIndex.addView(sectors);
		}
		
		listView.setOnScrollListener(this);
	}

	/**
	 * Onlick Sector Class
	 */

	private class SideSectorClick implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			/**
			 * Set Child back to unselected
			 */
			for(int i=0;i<sideIndex.getChildCount();i++)
				sideIndex.getChildAt(i).setBackgroundColor(Color.WHITE);

			TextView textView = (TextView) v;
			for(int i=0; i<phoneModels.size(); i++)
			{
				if(phoneModels.get(i).isSection() && phoneModels.get(i).getName().startsWith(textView.getText().toString()))
				{
					listView.setSelection(i);
					textView.setBackgroundResource(R.drawable.selected_side_view_bg);
					break;
				}
			}			
		}

	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if(phoneModels.get(firstVisibleItem).isSection())
		{
			for(int i=0;i<sideIndex.getChildCount();i++)
			{
				if(((TextView)sideIndex.getChildAt(i)).getText().toString().equals(phoneModels.get(firstVisibleItem).getName()))
					sideIndex.getChildAt(i).setBackgroundResource(R.drawable.selected_side_view_bg);
				else
					sideIndex.getChildAt(i).setBackgroundColor(Color.WHITE);
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}
}
