package com.example.jv.jollyvolly.tabs.maps.carousel;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jv.jollyvolly.R;
import com.example.jv.jollyvolly.tabs.maps.MobiliuzCar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MyFragment extends Fragment {
	
	public static Fragment newInstance(Activity context, int pos, float scale, ArrayList<MobiliuzCar> carList) {
		Bundle b = new Bundle();
		b.putInt("pos", pos);
		b.putFloat("scale", scale);
		b.putInt("id", carList.get(pos).getId());
		b.putString("status", carList.get(pos).getStatus());
//		b.putLong("lat_lang", carList.get(pos).getLatLang());
		b.putString("address", carList.get(pos).getAddress());
		b.putString("time", carList.get(pos).getTime());
		b.putString("imageUrl", carList.get(pos).getImageUrl());
		return Fragment.instantiate(context, MyFragment.class.getName(), b);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		
		RelativeLayout l = (RelativeLayout) inflater.inflate(R.layout.mf, container, false);


		int pos = this.getArguments().getInt("pos");
		TextView textViewTime = (TextView) l.findViewById(R.id.textViewTime);
		TextView textViewAddress = (TextView) l.findViewById(R.id.textAddress);
		ImageView iv = (ImageView) l.findViewById(R.id.imageViewCar);
		int id = this.getArguments().getInt("id");

		textViewAddress.setText(this.getArguments().getString("address"));
		textViewTime.setText(this.getArguments().getString("time"));
		Picasso.with(getActivity())
				.load(getArguments().getString("imageUrl"))
				.into(iv);

		MyLinearLayout root = (MyLinearLayout) l.findViewById(R.id.root);

//		iv.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getArguments().getString("objectUrl")));
//				startActivity(browserIntent);
//			}
//		});
		float scale = this.getArguments().getFloat("scale");
		root.setScaleBoth(scale);
		
		return l;
	}
}
