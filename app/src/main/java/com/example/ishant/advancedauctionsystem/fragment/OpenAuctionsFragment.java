package com.example.ishant.advancedauctionsystem.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ishant.advancedauctionsystem.ItemActivity;
import com.example.ishant.advancedauctionsystem.Constants;
import com.example.ishant.advancedauctionsystem.DBManager;
import com.example.ishant.advancedauctionsystem.R;
import com.example.ishant.advancedauctionsystem.modelauction.AuctionItem;

import java.sql.SQLException;
import java.util.List;

public class OpenAuctionsFragment extends BaseFragment {

	private TextView emptyText;
	private ListView listOpenAuctions;
	private List<AuctionItem> auctionItems;
	private AuctionsAdapter auctionsAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_open_auctions, null);
		emptyText = (TextView) rootView.findViewById(R.id.empty);
		listOpenAuctions = (ListView) rootView.findViewById(R.id.listOpenAuctions);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		updateTitle(getString(R.string.title_live_auctions));		
		DBManager dbHelper = getDBHelper();
		try {
			if (dbHelper != null) {
				auctionItems = dbHelper.getOpenAuctions(getUser());
			}
		}
		catch (SQLException ex) {
			
		}
		showOpenAuctions();
	}

	private void showOpenAuctions() {
		if (auctionItems == null || auctionItems.isEmpty()) {
			emptyText.setVisibility(View.VISIBLE);
			listOpenAuctions.setVisibility(View.GONE);
		} else {
			emptyText.setVisibility(View.GONE);
			listOpenAuctions.setVisibility(View.VISIBLE);
			Context context = getActivity().getApplicationContext();
			auctionsAdapter = new AuctionsAdapter(context, auctionItems, getDisplayOptions());
			listOpenAuctions.setAdapter(auctionsAdapter);
			listOpenAuctions.setOnItemClickListener(onItemClick);
		}
	}
	
	private OnItemClickListener onItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
			Intent intent = new Intent(getActivity(), ItemActivity.class);
			intent.putExtra(Constants.EXTRA_ITEM_ID, id);
			startActivity(intent);
		}
	};
}