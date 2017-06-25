package com.example.ishant.advancedauctionsystem.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.ishant.advancedauctionsystem.Constants;
import com.example.ishant.advancedauctionsystem.DBManager;
import com.example.ishant.advancedauctionsystem.OrmActivity;
import com.example.ishant.advancedauctionsystem.UserPref;
import com.example.ishant.advancedauctionsystem.R;
import com.example.ishant.advancedauctionsystem.modelauction.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class BaseFragment extends Fragment {

	protected static final String TAG = BaseFragment.class.getSimpleName();
	protected ProgressDialog mProgressDialog;

	protected void showProgressDialog(String title, String message) {
		try {
			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
			mProgressDialog = ProgressDialog.show(getActivity(), title, message);
		}
		catch (Exception ex) {
			
		}
	}

	protected void hideProgressDialog() {
		try {
			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
			mProgressDialog = null;
		}
		catch (Exception ex) {
			Log.e(TAG, "##### --> " + ex);
		}
	}

	protected DBManager getDBHelper() {
		DBManager dbHelper = null;
		Activity mActivity = getActivity();
		if (mActivity != null && mActivity instanceof OrmActivity) {
			dbHelper = ((OrmActivity) mActivity).getDBHelper();
		}
		return dbHelper;
	}

	protected DisplayImageOptions getDisplayOptions() {
		return new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.no_photo).showImageOnFail(R.drawable.no_photo).showImageOnLoading(R.drawable.no_photo).displayer(new FadeInBitmapDisplayer(500)).cacheOnDisk(true).handler(new Handler()).build();
	}

	protected User getUser() {
		User user = new User();
		UserPref userPref = new UserPref(getActivity());
		user.setId(Long.valueOf(userPref.readPreference(Constants.PREF_LOGGED_USER_ID, "-1")));
		return user;
	}

	protected void updateTitle(String title) {
		Activity mActivity = getActivity();
		if (mActivity != null && mActivity instanceof ActionBarActivity) {
			mActivity.setTitle(title);
			((ActionBarActivity) mActivity).getSupportActionBar().setTitle(title);
		}
	}

	protected void showToast(String message) {
		Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
	}
}
