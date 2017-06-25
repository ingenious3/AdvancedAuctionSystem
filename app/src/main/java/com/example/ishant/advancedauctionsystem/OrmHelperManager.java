package com.example.ishant.advancedauctionsystem;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

public class OrmHelperManager extends OpenHelperManager {

	public static <T extends OrmLiteSqliteOpenHelper> T getHelper(final String activityName, final Context context, final Class<T> openHelperClass) {
		
		return OpenHelperManager.getHelper(context, openHelperClass);
	}

	public static void releaseHelper(final String activityName) {
		
		OpenHelperManager.releaseHelper();
	}
}
