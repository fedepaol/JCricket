package com.whiterabbit;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.whiterabbit.jcricket.PopupHint;

public class HelloAndroidActivity extends Activity {

    private static String TAG = "jcricket_sample";

    Button mButton;
    PopupHint mHint;
    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after 
     * previously being shut down then this Bundle contains the data it most 
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.main);


        mButton = (Button) findViewById(R.id.main_button);
        PopupHint.PopupBuilder b = new PopupHint.PopupBuilder(this);
        mHint = b.layout(R.layout.baloon).location(PopupHint.PopupLocation.TOP_LEFT, PopupHint.PopupCorner.BOTTOM_RIGHT).build();
        TextView baloonText = (TextView) mHint.getHintSubview(R.id.baloon_text);
        baloonText.setText("Baloon text\ncan be set programmatically\nPress me to dismiss");


    }


    public void onMainButtonPressed(View v){
        mHint.showHint(v);
    }

}

