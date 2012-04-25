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
import java.util.ArrayList;
import java.util.List;

import linkdroid.login.test.R;

 
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
 
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
 
public class LoginActivity extends Activity implements OnClickListener {
  
 Button ok,back,exit;
 TextView result;
 Bundle extras;

  
 /*
  * onCreate test if Barcode scanner is installed,
  * if true it displays the login form and listen for login button click 
  * if false a warning is displayed and the user can download the app
  * from google play/android market by a link
  */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	Intent i1 = new Intent("com.google.zxing.client.android.SCAN");
    	
        super.onCreate(savedInstanceState);
        
        if(isCallable(i1)==true)
        {
        setContentView(R.layout.main);
         
        // Login button clicked
        ok = (Button)findViewById(R.id.btn_login);
        ok.setOnClickListener(this);
         
        result = (TextView)findViewById(R.id.lbl_result);
        }
        
        else
        {
        	Intent i = new Intent(this,Warning.class);		
			startActivity(i);
        }
         
    }
    
    /* Method for testing if intent to other activity is installed or callable within
     * the current android OS, in this case the ZXing Barcode Scanner, http://code.google.com/p/zxing/
     *  which is licensed other Apache
     */
    private boolean isCallable(Intent intent1) {   
    	
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent1,     
    PackageManager.MATCH_DEFAULT_ONLY);    
        
        if(list.size() > 0)  
        return true ;    
        else  
    return false;  
  
    }  
    

     /* This method is for posting the login-form data to a php-script with httppost
      * You need to add your own path to the php-script I choose how to handle the
      * login data
      */
    public void postLoginData() {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
         
        /* the login php-script can be so simple that it returns true if username and password is equal to e.g abc123
         * 
         */

        
        HttpPost httppost = new HttpPost("Here you put your login php-script");
 
        try {

         EditText uname = (EditText)findViewById(R.id.txt_username);
         String username = uname.getText().toString();
         
         EditText pword = (EditText)findViewById(R.id.txt_password);
         String password = pword.getText().toString();
          
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
 
            // Execute HTTP Post Request
            Log.w("Linkdroid", "Execute HTTP Post Request");
            HttpResponse response = httpclient.execute(httppost);
             
            String str = inputStreamToString(response.getEntity().getContent()).toString();
            Log.w("Linkdroid", str);
             
            if(str.toString().equalsIgnoreCase("Successful"))
            {
             Log.w("Linkdroid", "TRUE"); // If php script returns the string true there was a successful login
             result.setText("Login successful"); 
             Intent i = new Intent(this,Remote_help.class);		
  			startActivity(i);
            }else
            {
            	// If php script returns the string false the login was unsuccessful
             Log.w("Linkdroid", "FALSE");
             result.setText(str);            
            }
 
        } catch (ClientProtocolException e) {
         e.printStackTrace();
        } catch (IOException e) {
         e.printStackTrace();
        }
    }
   
    /* This method handles the response from the php-script as an inputstream and
     *  creates a string which e.g can be used to display the content from the php-script
     *  If the login is successful the Remote_help activity is started by intent
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
     * If button is clicked the method postLoginData is called 
     */
    public void onClick(View view) {
      if(view == ok){
        postLoginData();
       
      }

    }
 
}