package edu.utk.phys.astro.hubble;

import org.apache.commons.math.stat.regression.OLSMultipleLinearRegression;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.utk.phys.astro.HubbleExpander;
import edu.utk.phys.astro.HubbleExpansionSim;

import android.content.Intent;
import android.util.Log;
import android.webkit.WebView;

public class HubbleAnalysisHandler {
	private WebView wv;
	private Intent intent;
	double[] X;
	double[] Y;
	float hubbleConstant;

	public HubbleAnalysisHandler(WebView wv, Intent intent) {
		this.wv = wv;
		this.intent = intent;
	}

	public String getGraphTitle() {
		return "Hubble Analysis";
	}
	
	public float getHubbleConstant() {
		return hubbleConstant;
	}
	
	public double[] linearRegression(double[] xx,double[] y) {
		double[] ab = new double[2];
		double[][] x = new double[y.length][];
		OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
		
		for(int i = 0; i<y.length;i++) {
			x[i] = new double[] {xx[i]};
		}
		
		regression.newSampleData(y, x);
		ab[0] =  regression.estimateRegressionParameters()[0];
		ab[1] = regression.estimateResiduals()[0];
		hubbleConstant = (float)ab[0];
		Log.i("Regression","a = " + regression.estimateRegressionParameters()[0] + ", b = " + regression.estimateResiduals()[0]);
		
		return ab;
	}
	
	
	public JSONArray getRawDataJSON() {
		JSONArray arr = new JSONArray();
		
		HubbleAnalysisData dat = (HubbleAnalysisData)intent.getSerializableExtra("data");
		float[] center = dat.universeCenter;
		float[][] galaxyPositions = dat.initialPositions;
		float[][] finalGalaxyPositions = dat.finalPositions;
		int nGalaxies = dat.numberOfGalaxies;
		Log.i("RawData","There are " + nGalaxies + " galaxies");
		
		X = new double[finalGalaxyPositions[0].length];
		Y = new double[X.length];
		for(int i =0 ;i < nGalaxies; i++) {
			JSONArray arr2 = new JSONArray();
			float Xrel = galaxyPositions[0][i] - center[0];
			float XrelFinal = finalGalaxyPositions[0][i] - center[0];
			float Yrel = galaxyPositions[1][i] - center[1];
			float YrelFinal = finalGalaxyPositions[1][i] - center[1];
			float R = (float)Math.sqrt(Math.pow(Xrel, 2) + Math.pow(Yrel,2));
			float Rfinal = (float)Math.sqrt(Math.pow(XrelFinal, 2) + Math.pow(YrelFinal,2));
			float v = (Rfinal - R);
			
			//try {
				X[i] = R;
				Y[i] = v;
				arr2.put(Math.round(R));
				arr2.put(Math.round(v));
			//} catch (JSONException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				//Log.i("RawData","Failed to put in array");
			//}
			arr.put(arr2);
		}
		return arr;
	}
	
	public JSONObject getLineOptionsJSON() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("show", true);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	public JSONObject getPointOptionsJSON() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("show", false);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	public JSONArray getFitDataJSON(double a, double b) {
		JSONArray ret = new JSONArray();
		
		for(int i = 0; i< X.length; i++) {
			double y = X[i]*a + b;
			JSONArray arr = new JSONArray();
			try {
				arr.put(X[i]);
				arr.put(y);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ret.put(arr);
		}
		return ret;
	}
	

	public void loadGraph() {
		JSONArray arr = new JSONArray();

		JSONObject rawData = new JSONObject();
		JSONArray raw = getRawDataJSON();
		
		
		// fit is available after original data is pulled
		double[] ab = linearRegression(X,Y);
		JSONObject fitData = new JSONObject();
		JSONArray fit = getFitDataJSON(ab[0],ab[1]);
		
		boolean isExpanded = ((HubbleAnalysisData)intent.getSerializableExtra("data")).isExpanded;
		if(!isExpanded) {
			Log.i("loadGraph","Not expanded!");
			return;
		}
		try {
			
			
			rawData.put("data",raw); 
			rawData.put("points",getLineOptionsJSON());
			fitData.put("data", fit);
			arr.put(rawData);
			arr.put(fitData);
		} catch (Exception ex) {
			// do something
			Log.i("LoadGraph",ex.toString());
			ex.printStackTrace();
		}
		// return arr.toString(); //This _WILL_ return the data in a good
		// looking JSON string, but if you pass it straight into the Flot Plot
		// method, it will not work!

		wv.loadUrl("javascript:GotGraph(" + arr.toString() + ")"); // this
		Log.i("handler","arr.toString() = "+arr.toString());
		//wv.loadUrl("javascropt:GotGraph([" + getRawDataJSON() + "])");     														// callback
																			// works!

	}
}
