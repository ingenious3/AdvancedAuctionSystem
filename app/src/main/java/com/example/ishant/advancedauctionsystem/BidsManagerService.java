package com.example.ishant.advancedauctionsystem;

import android.app.IntentService;
import android.content.Intent;

import com.example.ishant.advancedauctionsystem.modelauction.AuctionItem;
import com.example.ishant.advancedauctionsystem.modelauction.Bid;
import com.example.ishant.advancedauctionsystem.modelauction.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class BidsManagerService extends IntentService {

	private static final String TAG = BidsManagerService.class.getSimpleName();
	private DBManager dbManager;

	public BidsManagerService() {
		super(TAG);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (dbManager == null) {
			dbManager = OrmHelperManager.getHelper(TAG, this, DBManager.class);
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		long itemId = intent.getLongExtra(Constants.EXTRA_ITEM_ID, -1);
		double baseAmount = intent.getDoubleExtra(Constants.EXTRA_BASE_AMOUNT, 0.0);
		
		try {
			List<User> users = dbManager.getSystemUser();
			if (users != null && users.size() == 1) {
				User user = users.get(0);
				AuctionItem bidFor = new AuctionItem();
				bidFor.setId(itemId);
				Bid newBid = new Bid();
				newBid.setBidFor(bidFor);
				newBid.setBidder(user);
				newBid.setQuotePrice(generateBidAmount(baseAmount));
				newBid.setBidNotes("How's our offer?");
				dbManager.getBidRuntimeDao().create(newBid);
			}
		}
		catch (SQLException ex) {
		}
	}

	private double generateBidAmount(double baseAmount) {
		double bidAmount = 0.0;
		Random random = new Random();
		bidAmount = random.nextInt(101) + baseAmount;
		return bidAmount;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (dbManager != null) {
			OrmHelperManager.releaseHelper(TAG);
			dbManager = null;
		}
	}
}
