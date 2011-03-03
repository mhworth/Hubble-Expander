package edu.utk.phys.astro;

import edu.utk.phys.astro.R;
import edu.utk.phys.astro.hubble.HubbleAnalysisHandler;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class HubbleAnalysis extends Activity {

	public static final String URL = "";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.hubble_analysis);
        final WebView wv = (WebView) findViewById(R.id.webview);
        final HubbleExpansionSim expander = (HubbleExpansionSim) findViewById(R.id.hubble_expansion);
        
    	HubbleAnalysisHandler myhandler = new HubbleAnalysisHandler(wv,this.getIntent());
		wv.getSettings().setJavaScriptEnabled(true);
		wv.addJavascriptInterface(myhandler, "hubble_analysis_handler");
		wv.loadUrl("file:///android_asset/hubble_analysis.html");
    }
}
