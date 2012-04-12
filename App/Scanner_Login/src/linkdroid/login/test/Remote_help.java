package linkdroid.login.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Remote_help extends Activity implements OnClickListener{
	
	
	 Button generateR1,scanQr, radioR1, radioR2,qrButton;
	 TextView resp1,resp2;
	 
	public void onCreate(Bundle savedInstanceState) {

	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main_remote_help);
	        
	        generateR1 = (Button)findViewById(R.id.button1);
	        generateR1.setOnClickListener(this);
	        
	        qrButton = (Button)findViewById(R.id.button2);
	        qrButton.setOnClickListener(this);
	        
	  
	}

	public void onClick(View view) {
		 if(view == generateR1){
		 	 genResp1();		       
		      }
		 
		 else if(view == qrButton){
			 
			 Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			 intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // Only scan QR
			 
			 try
			 {
			 startActivityForResult(intent, 0);    //Barcode Scanner to scan for us
			 }
			 
			 catch(ActivityNotFoundException a)
			 {
				 		        
				 Intent i = new Intent(this,Warning.class);		
		  			startActivity(i);
			 }
			 
			/* Intent i = new Intent(this,Scanner.class);		
  			startActivity(i);*/	     
		      }
		
	}

	
	//Store OTL or RPC as session inside PHP, generate response one
	private void genResp1() {
		
		HttpClient httpclient = new DefaultHttpClient();
		
		HttpPost httppost = new HttpPost("HERE YOU ADD THE PATH TO YOUR OWN PHP-script");
		
		 try {
			 
			//if OTL or RPC pressed, send as variable type=OTL or RPC
			    
			    /* login.php returns true if username and password is equal to abc123
			     * this is what I've so far done in my php-script just do try out
			     * the code */
			    
			       
			    RadioGroup radioType = (RadioGroup) findViewById(R.id.radioGroup);
			    
			 // get selected radio button from radioGroup
			 			int selectedId = radioType.getCheckedRadioButtonId();
			  
			 // find the radiobutton by returned id
			 		   RadioButton radioTypeButton = (RadioButton) findViewById(selectedId);
			   
			 // Assigning button type to string
			 		   			 		   
			 		  String type = radioTypeButton.getText().toString();

	          
	            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	            nameValuePairs.add(new BasicNameValuePair("type", type));
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	 
	            // Execute HTTP Post Request
	            Log.w("Linkdroid", "Execute HTTP Post Request");
	            HttpResponse response = httpclient.execute(httppost);
	             
	            String str = inputStreamToString(response.getEntity().getContent()).toString();
	            Log.w("Linkdroid", str);
	             

	             Log.w("Linkdroid", "TRUE"); // If php script returns true
	             //result.setText(str);
	             
	             // Uses existing textview and set the text to the string, response one, from php-script
	             resp1 = (TextView)findViewById(R.id.resp_1);
	             resp1.setText(str);
	             
	             //Disables response one genereate button
	             generateR1 = (Button)findViewById(R.id.button1);
	             generateR1.setEnabled(false);
	             
	           //Disables radiobuttons for OTL, one time logon, and RPC, remote password change,
	             radioR1 = (Button)findViewById(R.id.radioButton1);
	             radioR2 =(Button)findViewById(R.id.radioButton2);
	             radioR1.setEnabled(false);
	             radioR2.setEnabled(false);
	             
	             //Enabling QR-scanning activity
	                          
	             qrButton =(Button)findViewById(R.id.button2);
	             qrButton.setEnabled(true);
	             

	 
	        } catch (ClientProtocolException e) {
	         e.printStackTrace();
	        } catch (IOException e) {
	         e.printStackTrace();
	        }
			 
			
			 		   
			 		   

		
		
	}
	
    private StringBuilder inputStreamToString(InputStream is) {
        String line = "";
        StringBuilder total = new StringBuilder();
        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        // Read response until the end
        try {
         while ((line = rd.readLine()) != null) {
           total.append(line);
         }
        } catch (IOException e) {
         e.printStackTrace();
        }
        // Return full string
        return total;
       }
    
    // Gets result from the qr-scan
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    if (requestCode == 0) {
	    	
	      
	    	//If the code was decoded correctly the string is tested against php-script
	    	// If tested correct against php script it generates response two
	      if (resultCode == RESULT_OK) {
	    	  
	    	 
	    	 
	    	 HttpClient httpclient = new DefaultHttpClient();
	         
         
	         //https://10.0.0.113/Tutorials/Projekt/QR/login.php
	         //http://localhost/Tutorials/Projekt/QR/login.php
	         
	         HttpPost httppost = new HttpPost("HERE YOU ADD THE PATH TO YOUR OWN PHP-script");
	  
	         try {
	             // Add user name and password

	        	 //Get the qr-decoded string 
	        	 String qr= intent.getStringExtra("SCAN_RESULT");
	        	 
	        	 // Select one string to be sent along with the post
	             List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	             nameValuePairs.add(new BasicNameValuePair("qr", qr));
	             httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	  
	             // Execute HTTP Post Request
	             Log.w("Linkdroid", "Execute HTTP Post Request");
	             HttpResponse response = httpclient.execute(httppost);
	              
	             //Get return string
	             String str = inputStreamToString(response.getEntity().getContent()).toString();
	             Log.w("Linkdroid", str);
	              
	             // If qr code don't match PHP-script it returns fail
	             if(str.toString().equalsIgnoreCase("Fail"))
	             {
	              Log.w("Linkdroid", "Fail"); //
	              
	              resp2 = (TextView)findViewById(R.id.resp_2);
	              // Generate second response
	              resp2.setText("QR code didn't match");
	             }else
	             {
	              Log.w("Linkdroid", "Success");
	              qrButton =(Button)findViewById(R.id.button2);
		          qrButton.setEnabled(false);
		          
		          genResp2();
		          
	                     
	             }
	  
	         } catch (ClientProtocolException e) {
	          e.printStackTrace();
	         } catch (IOException e) {
	          e.printStackTrace();
	         }
	     }
	    

	      }
	    
	      // If qr code decoding failed error message is displayed instead of repsonse two
	      else if (resultCode == RESULT_CANCELED) {
	    	resp2 = (Button)findViewById(R.id.resp_2);
	        resp2.setText("QR scan cancelled. No response two generated");
	      }
	    }
    
    private void genResp2()
    {
    	
HttpClient httpclient = new DefaultHttpClient();
		
		
		 try {

			    
			       
			 HttpGet request = new HttpGet();
	            try {
					request.setURI(new URI("HERE YOU ADD THE PATH TO YOUR OWN PHP-script"));
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
	            HttpResponse response = httpclient.execute(request);
	            
	            String str = inputStreamToString(response.getEntity().getContent()).toString();
	            Log.w("Linkdroid", str);
	             

	             Log.w("Linkdroid", "TRUE"); // If php script returns true
	             //result.setText(str);
	             
	             // Setting response two according to string from php-script
	             resp1 = (TextView)findViewById(R.id.resp_2);
	             resp1.setText(str);
	             

	             //Disable qr scan button

	             scanQr = (Button)findViewById(R.id.button1);

	             scanQr.setEnabled(false);
	                          
	             

	 
	        } catch (ClientProtocolException e) {
	         e.printStackTrace();
	        } catch (IOException e) {
	         e.printStackTrace();
	        }
    	
    }
}
	
	

