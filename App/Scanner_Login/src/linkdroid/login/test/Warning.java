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
