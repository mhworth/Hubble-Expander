package edu.utk.phys.astro;

import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.IntegratorException;

import edu.utk.phys.astro.hubble.HarmonicODE;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class HubbleExpander extends Activity implements OnClickListener {
	HubbleExpansionSim expansion;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        //findViewById(R.id.txt1).setOnClickListener(this);
        
        // Instantiate the class MotionRunner to define the entry screen display
        expansion = new HubbleExpansionSim(this);
        expansion.setLayoutParams(new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        setContentView(expansion);
        expansion.setOnClickListener(this);
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
            expansion.startLooper();
    }  


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Log.i("Result","Clicked!");
		expansion.setNumberOfGalaxies(50);
		
		expansion.startLooper();
//		HarmonicODE harm = new HarmonicODE();
//		try {
//			harm.compute(new double[] { 1.0, 1.0 }, 0, 16);
//			for (int i = 0; i < 16; i++) {
//				Log.i("DIFFEQ","i = " + i + "y = " + harm.getResult(i)[1]);
//			}
//		} catch (DerivativeException e) {
//			e.printStackTrace();
//		} catch (IntegratorException e) {
//			e.printStackTrace();
//		}
		
	}
}