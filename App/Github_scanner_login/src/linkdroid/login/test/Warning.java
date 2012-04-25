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
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Warning extends Activity implements OnClickListener{
	
	
	Button bsPlay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			setContentView(R.layout.main_warning);
			
			bsPlay = (Button)findViewById(R.id.play);
	        bsPlay.setOnClickListener(this);
	}
	
	public void onClick(View view) {
		 if(view == bsPlay){
			 Uri uri = Uri.parse("market://details?id=com.google.zxing.client.android");

			 Intent i = new Intent(Intent.ACTION_VIEW, uri);
			 
		       try {
		        startActivity(i);
		       } catch (ActivityNotFoundException anfe) {

		         Log.w("Linkdroid", "Google Play is not installed? cannot install Barcode Scanner");
		       }	       
		      }
	
	}
}
