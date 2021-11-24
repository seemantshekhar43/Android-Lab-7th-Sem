package com.seemantshekhar.reminderapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.IntentFilter;
import android.os.Bundle;

import android.view.View;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public abstract class BaseActivity extends AppCompatActivity {

	private View _progressBar;


	protected Integer activityView;
	protected Integer progressBarLayout;
	protected Toolbar toolbar;




	protected void initialSetup() {}

	protected void initLayouts() {
		this.activityView = null;
		this.progressBarLayout = null;
	}

	protected void onCreate() {}

	protected void configureToolBar(Toolbar toolbar) {}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);



		this.initialSetup();




		this.initLayouts();

		assert this.activityView != null;
		this.setContentView(this.activityView);
		this.toolbar = this.findViewById(R.id.toolbar);

		this.configureToolBar(this.toolbar);

		//setSupportActionBar(toolbar);
		this.toolbar.setNavigationOnClickListener(bp -> onBackPressed());

		ActionBar actionBar = this.getSupportActionBar();


		if (this.progressBarLayout != null) {
			this._progressBar = this.findViewById(this.progressBarLayout);
		}

		this.onCreate();

		if (this._progressBar != null) {
			this._progressBar.setVisibility(View.GONE);
		}
	}

	private void toggleProgressBar(boolean show) {
		int shortAnimTime = this.getResources().getInteger(android.R.integer.config_shortAnimTime);
		this._progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
		this._progressBar.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(
			new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					_progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
				}
			}
		);
	}

	protected void showProgressBar() {
		this.toggleProgressBar(true);
	}

	protected void hideProgressBar() {
		this.toggleProgressBar(false);
	}


}
