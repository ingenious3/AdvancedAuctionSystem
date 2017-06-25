package com.example.ishant.advancedauctionsystem.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ishant.advancedauctionsystem.BidsActivity;
import com.example.ishant.advancedauctionsystem.Constants;
import com.example.ishant.advancedauctionsystem.DBManager;
import com.example.ishant.advancedauctionsystem.R;
import com.example.ishant.advancedauctionsystem.modelauction.AuctionItem;

import java.sql.SQLException;
import java.util.List;

public class ItemsFragment extends BaseFragment {

	private TextView emptyText;
	private ListView listMyAuctions;
	private List<AuctionItem> auctionItems;
	private AuctionsAdapter auctionsAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_auction_items, null);
		emptyText = (TextView) rootView.findViewById(R.id.empty);
		listMyAuctions = (ListView) rootView.findViewById(R.id.listMyAuctions);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		updateTitle(getString(R.string.title_my_auctions));
		DBManager dbHelper = getDBHelper();
		try {
			if (dbHelper != null) {
				auctionItems = dbHelper.getMyAuctions(getUser());
			}
		}
		catch (SQLException ex) {
			}
		showMyAuctions();
	}

	private void showMyAuctions() {
		if (auctionItems == null || auctionItems.isEmpty()) {
			emptyText.setVisibility(View.VISIBLE);
			listMyAuctions.setVisibility(View.GONE);
		} else {
			emptyText.setVisibility(View.GONE);
			listMyAuctions.setVisibility(View.VISIBLE);
			Context context = getActivity().getApplicationContext();
			auctionsAdapter = new AuctionsAdapter(context, auctionItems, getDisplayOptions());
			listMyAuctions.setAdapter(auctionsAdapter);
			listMyAuctions.setOnItemClickListener(onItemClick);
		}
	}

	private OnItemClickListener onItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(getActivity(), BidsActivity.class);
			AuctionItem auctionItem = auctionItems.get(position);
			intent.putExtra(Constants.EXTRA_ITEM_ID, auctionItem.getId());
			intent.putExtra(Constants.EXTRA_TITLE, auctionItem.getTitle());
			startActivity(intent);
		}
	};
}
