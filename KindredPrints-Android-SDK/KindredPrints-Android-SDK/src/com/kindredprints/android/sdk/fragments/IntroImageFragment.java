package com.kindredprints.android.sdk.fragments;

import com.kindredprints.android.sdk.KURLPhoto;
import com.kindredprints.android.sdk.R;
import com.kindredprints.android.sdk.data.Size;
import com.kindredprints.android.sdk.helpers.cache.ImageManager;
import com.kindredprints.android.sdk.helpers.cache.ImageManager.ImageManagerCallback;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class IntroImageFragment extends KindredFragment {

	private ImageManager imManager_;
	private ImageView imgBackground_;
	private ProgressBar progressBar_;
	private String imageUrl_;
	private boolean drawn_;
	
	private ImageManagerCallback imageSetCallback_;
	
	public IntroImageFragment() { }

	public void init(Context context, KindredFragmentHelper fragmentHelper) {
		this.imManager_ = ImageManager.getInstance(context);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = (ViewGroup) inflater.inflate(R.layout.fragment_intro_page, container, false);

		this.imgBackground_ = (ImageView) view.findViewById(R.id.imgBackground);
		imgBackground_.setImageBitmap(null);
		
		this.progressBar_ = (ProgressBar) view.findViewById(R.id.progressBar);

		setImageVisible(false);
		
		this.drawn_ = false;
		view.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@SuppressLint("NewApi")
			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				loadImage();
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
					view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				} else {
					view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}
			}
		});
		
		return view;
	}
	
	public void setBackgroundImage(String url) {
		this.imageUrl_ = url;
		if (this.drawn_) {
			loadImage();
		}
	}
	
	private void setImageVisible(boolean visible) {
		if (visible) {
			this.imgBackground_.setVisibility(View.VISIBLE);
			this.progressBar_.setVisibility(View.INVISIBLE);
		} else {
			this.imgBackground_.setVisibility(View.INVISIBLE);
			this.progressBar_.setVisibility(View.VISIBLE);
		}
	}
	
	private void loadImage() {
		this.drawn_ = true;
		if (this.imageUrl_ != null) {
			String[] sections = this.imageUrl_.split("/");
			String pid = sections[sections.length-1];
			
			this.imageSetCallback_ = new ImageManagerCallback() {
				@Override
				public void imageAssigned(Size size) {
					setImageVisible(true);
				}
			};
			
			this.imManager_.setImageAsync(this.imgBackground_, new KURLPhoto(this.imageUrl_), pid, new Size(this.imgBackground_.getWidth(), this.imgBackground_.getHeight()), this.imageSetCallback_);
		}
	}
}