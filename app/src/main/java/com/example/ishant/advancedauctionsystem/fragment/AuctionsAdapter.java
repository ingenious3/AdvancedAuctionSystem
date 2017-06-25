package com.example.ishant.advancedauctionsystem.fragment;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ishant.advancedauctionsystem.R;
import com.example.ishant.advancedauctionsystem.BasicUtility;
import com.example.ishant.advancedauctionsystem.modelauction.AuctionItem;
import com.example.ishant.advancedauctionsystem.modelauction.ItemPic;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class AuctionsAdapter extends BaseAdapter {

	private List<AuctionItem> auctions;
	private LayoutInflater inflater;
	private String auctionEndsFormat;
	private DisplayImageOptions displayImageOptions;
	private ImageLoader imageLoader;

	public AuctionsAdapter(Context context, List<AuctionItem> items, DisplayImageOptions displayImageOptions) {
		this.auctions = items;
		this.displayImageOptions = displayImageOptions;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		auctionEndsFormat = context.getString(R.string.bidding_ends_on);
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return auctions.size();
	}

	@Override
	public Object getItem(int position) {
		return auctions.get(position);
	}

	@Override
	public long getItemId(int position) {
		return auctions.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AuctionItem item = (AuctionItem) getItem(position);
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.auctions_item_list, null);
			holder = new ViewHolder();
			holder.itemImage = (ImageView) convertView.findViewById(R.id.ivItem);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.itemTitle);
			holder.tvDesc = (TextView) convertView.findViewById(R.id.itemDesc);
			holder.tvEndsOn = (TextView) convertView.findViewById(R.id.itemAuctionEnd);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Collection<ItemPic> itemPhotos = item.getItemPhotos();
		if(itemPhotos != null) {
			Iterator<ItemPic> iterator = itemPhotos.iterator();
			while(iterator.hasNext()) {
				ItemPic photo = iterator.next();
				imageLoader.displayImage(Uri.fromFile(new File(photo.getPath())).toString(), holder.itemImage, displayImageOptions);
				break;
			}						
		}
		holder.tvTitle.setText(item.getTitle());
		holder.tvDesc.setText(item.getDescription());
		holder.tvEndsOn.setText(String.format(auctionEndsFormat, BasicUtility.getFormattedDate(item.getBiddingClosesOn())));
		convertView.setTag(holder);
		return convertView;
	}

	static class ViewHolder {
		ImageView itemImage;
		TextView tvTitle, tvDesc, tvEndsOn;
	}
}
