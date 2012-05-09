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
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

	/*
	  * onClick handles the clicks generate response1, and scan qr code
	  */
	public void onClick(View view) {
		 if(view == generateR1){
		 	 genResp1();		       
		      }
		 
		 else if(view == qrButton){
			 
			 Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			 intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // Only scan QR

			 
			 try
			 {
		     Log.w("Activity starts","Starting");
			 startActivityForResult(intent, 0);    //Barcode Scanner to scan for us
			 }
			 
			 catch(ActivityNotFoundException a)
			 {
				 		        
				 Intent i = new Intent(this,Warning.class);		
		  	     startActivity(i);
			 }
		      }
		
	}

	

	private void genResp1() {
		
		HttpClient httpclient = new DefaultHttpClient();
		
		/*
		 * This php-script handles the kind of remote help the user wants,
		 * and print out a generated response 1. If a session is being kept,
		 * the script should see if it has ended and then exit the application
		 */
		HttpPost httppost = new HttpPost("Your own php-script");
		
		 try {
			 
			    
			       
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
	            
	             // If the php-script returns "Session timeout" the session has ended
	             if(str.equalsIgnoreCase("Session timeout"))
	             {
			    		Toast msg = Toast.makeText(Remote_help.this, "The login session has ended", Toast.LENGTH_LONG);

			    		msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);

			    		msg.show();
			    		
			    		Intent i = new Intent(this,LoginActivity.class);
			    		startActivity(i);
			    		
	            	 
	             }
	             
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
	             
	             //Enabling QR-scanning activity button                         
	             qrButton =(Button)findViewById(R.id.button2);
	             qrButton.setEnabled(true);
	             

	 
	        } catch (ClientProtocolException e) {
	         e.printStackTrace();
	        } catch (IOException e) {
	         e.printStackTrace();
	        }
			 
			
			 		   
			 		   

		
		
	}

	/*
	 * This method works as the one inside the login activity. A translater from inputstream
	 * from my php-script to a readable string
	 */
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
    
    /*
     * This method, onActivityResult, gets called after the barcode scanner has finished
     * and if the scan was successful the code-representation is being sent to another php-script
     * If the representation matches the one generated by the qr-code the method genResp2 is called
     * to display the second response
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	
    	Log.w("QR","Handling qr");

    	if (requestCode == 0) {
	    	

	      if (resultCode == RESULT_OK) {
	    	  
	    	 
	    	 
	    	 HttpClient httpclient = new DefaultHttpClient();
	                  
	         HttpPost httppost = new HttpPost("Your own php-script");
	  
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
    
    /*
     * This last method of the remote help session calls a php-script and displays the response two 
     * if the session is running
     */
    private void genResp2()
    {
    	
HttpClient httpclient = new DefaultHttpClient();
		
		
		 try {

			    
			       
			 HttpGet request = new HttpGet();
	            try {
					request.setURI(new URI("Your own php-script"));
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
	            HttpResponse response = httpclient.execute(request);
	            
	            String str = inputStreamToString(response.getEntity().getContent()).toString();
	            Log.w("Linkdroid", str);	             

	             Log.w("Linkdroid", "TRUE"); // If php script returns true
	             
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
	
	

