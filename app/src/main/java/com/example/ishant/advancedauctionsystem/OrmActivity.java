package com.example.ishant.advancedauctionsystem;

public class OrmActivity extends BaseActivity {

	private DBManager databaseHelper = null;

	public DBManager getDBHelper() {
		if (databaseHelper == null) {
			databaseHelper = OrmHelperManager.getHelper(TAG, this, DBManager.class);
		}
		return databaseHelper;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (databaseHelper != null) {
			OrmHelperManager.releaseHelper(TAG);
			databaseHelper = null;
		}
	}
}