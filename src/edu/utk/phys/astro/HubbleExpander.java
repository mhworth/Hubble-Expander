package edu.utk.phys.astro;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class HubbleExpander extends Activity {

	public final int DIALOG_HELP = 1;
	public final String HUBBLE_ANALYSIS_URL = "file:///android_asset/hubble_analysis.html";
	HubbleExpansionSim expansion;
	boolean isDragging = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		expansion = (HubbleExpansionSim) findViewById(R.id.hubble_expansion);
		final TextView txt1 = (TextView) findViewById(R.id.txt1);
		final SeekBar seekbar = (SeekBar) findViewById(R.id.seekbar);
		
		
		txt1.setText("Expansion Constant = " + expansion.getExpansionConstant());
		txt1.invalidate();
		
	
		
		

		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar arg0, int progress,
					boolean arg2) {

				float percent = (float) progress / 100.0f;
				float min = 1.0f;
				float max = 2.5f;
				expansion.setExpansionFactor((max - min) * percent + min);
				txt1.setText("Expansion Constant = "
						+ expansion.getExpansionConstant());

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}
		});

		// Instantiate the class MotionRunner to define the entry screen display
		// expansion = new HubbleExpansionSim(this);
		//
		// expansion.setLayoutParams(new ViewGroup.LayoutParams(
		// ViewGroup.LayoutParams.FILL_PARENT,
		// ViewGroup.LayoutParams.FILL_PARENT));

		// Set the text

		// expansion.setOnClickListener(this);
		// setContentView(R.layout.main);

	}

	@Override
	public void onPause() {
		super.onPause();
		// Stop animation loop if going into background
		expansion.stopLooper();
	}

	@Override
	public void onResume() {
		super.onResume();
		// Resume animation loop
		// expansion.startLooper();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.expander_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.new_universe:
			expansion.newUniverse();
			return true;
		case R.id.help:
			help();
			return true;
		case R.id.hubble_analysis:
			hubbleAnalysis();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case DIALOG_HELP:
			// do the work to define the pause Dialog
			AlertDialog.Builder builder;
			final AlertDialog alertDialog;

			Context mContext = getApplicationContext();
			mContext = expansion.getContext();
			Log.i("Dialog", "Application context = " + mContext);
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			final View layout = inflater.inflate(R.layout.help,
					(ViewGroup) findViewById(R.id.help_root));

			TextView text = (TextView) layout.findViewById(R.id.help_text);

			builder = new AlertDialog.Builder(mContext);
			builder.setView(layout);
			alertDialog = builder.create();
			dialog = alertDialog;
			layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (arg0 == layout) {
						alertDialog.dismiss();
					}

				}

			});
			Log.i("Dialog", "Set dialog");
			break;
		default:
			dialog = null;
		}
		return dialog;
	}

	public void help() {
		showDialog(DIALOG_HELP);
	}

	public void hubbleAnalysis() {
		Intent j = new Intent(this, HubbleAnalysis.class);
		//j.putExtra(HubbleAnalysis.URL,
		//		HUBBLE_ANALYSIS_URL);
		j.putExtra("data", expansion.toHubbleAnalysisData());
		startActivity(j);
	}

}