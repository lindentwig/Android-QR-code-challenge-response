/*
 * Copyright 2009 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package linkdroid.login.test;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Scanner extends Activity {
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main_scan);
	    HandleClick hc = new HandleClick();
	    findViewById(R.id.QR).setOnClickListener(hc);

	  }
	  private class HandleClick implements OnClickListener{
	    public void onClick(View argClick) {
	      Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	      switch(argClick.getId()){
	        case R.id.QR:
	          intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // Only scan QR
	        break;
	      }
	      startActivityForResult(intent, 0);    //Barcode Scanner to scan for us
	    }
	  }
	  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    if (requestCode == 0) {
	      TextView tvStatus=(TextView)findViewById(R.id.tvStatus);
	      TextView tvResult=(TextView)findViewById(R.id.tvResult);
	      if (resultCode == RESULT_OK) {
	        tvStatus.setText(intent.getStringExtra("SCAN_RESULT_FORMAT"));
	        tvResult.setText(intent.getStringExtra("SCAN_RESULT"));
	        
	        //String str = intent.getStringExtra("SCAN_RESULT"); 
	        // How to get the result string, this is the string I can use within another activity 

	      } else if (resultCode == RESULT_CANCELED) {
	        tvStatus.setText("Press the button to start a scan.");
	        tvResult.setText("Scan cancelled.");
	      }
	    }
	  }
	}
