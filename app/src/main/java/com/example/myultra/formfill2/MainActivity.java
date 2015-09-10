package com.example.myultra.formfill2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;


public class MainActivity extends ActionBarActivity {

   // FACEBOOK INTEGRATION INITIALIZATION
    CallbackManager callbackManager;
    LoginButton fbLoginButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//FACEBOOK SDK INITALIZED 
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager= CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);
        fbLoginButton=(LoginButton) findViewById(R.id.fb_login_button);


 //THIS REGISTERCALLBACK WILL OPEN THE FACEBOOK LOGIN AND ASK FOR CREDENTIALS
 
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
           
           //IF LOGIN IS SUCCESFUL
            @Override
            public void onSuccess(LoginResult loginResult) {

                System.out.println("Facebook Login Successful!");
                System.out.println("Logged in user Details : ");
                System.out.println("--------------------------");
                System.out.println("User ID  : " + loginResult.getAccessToken().getUserId());
                System.out.println("Authentication Token : " + loginResult.getAccessToken().getToken());
                Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_LONG).show();
            }

            ////IF LOGIN IS CANCELLED
            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Login cancelled by user!", Toast.LENGTH_LONG).show();
                System.out.println("Facebook Login failed!!");
            }
            
            //IF LOGIN IS UNSUCCESFUL

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(MainActivity.this, "Login unsuccessful!", Toast.LENGTH_LONG).show();
                System.out.println("Facebook Login failed!!");
            }

        });





//INITIALIZATION OF BUTTONS
        Button button;
        final EditText edit_name,edit_pass,edit_email,edit_phone;
        final CheckBox check;
        SharedPreferences pref;
         final Editor editor;

//INSTANCES
        button=(Button)findViewById(R.id.button);
        edit_name=(EditText)findViewById(R.id.edit_name);
        edit_pass=(EditText)findViewById(R.id.edit_pass);
        edit_email=(EditText)findViewById(R.id.edit_email);
        edit_phone=(EditText)findViewById(R.id.edit_phone);
        check=(CheckBox)MainActivity.this.findViewById(R.id.checkBox);
        pref=getSharedPreferences("Registration",0);
        editor=pref.edit();

//CHECKBOX FUNTION TO VIEW PASSWORD

       check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(!check.isChecked())
               {
                   edit_pass.setTransformationMethod(new PasswordTransformationMethod());
               }
               else
               {
                   edit_pass.setTransformationMethod(null);
               }
           }
       });


//IT WILL CHECK IF THE DATA GIVEN IS VALID OR NOT

               button.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       boolean flag =true;
                       String name=edit_name.getText().toString();
                       String email=edit_email.getText().toString();
                       String pass=edit_pass.getText().toString();
                       String phone=edit_phone.getText().toString();

                  // IT CHECKS THE VALID EMAIL
                       if(!isValidEmail(email))
                        {
                           edit_email.setError("Invalid Email");
                           flag=false;
                        }
                        
                        
                  // IT CHECKS THE VALID PASSWORD
                       if(!isValidPassword(pass))
                       {
                           edit_pass.setError("Invalid Password");
                           flag=false;
                       }
                  
                  // IT CHECKS THE VALID PHONE NO.
                       if (!isValidPhone(phone))
                       {
                           edit_phone.setError("Invalid phone");
                           flag=false;
                       }


                  // IF ALL THE DATA IS VALID , THEN SUCCESSFUL
                       if(flag==true)
                       {
                           Toast.makeText(getApplicationContext(), "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                           editor.putString("Name", name);
                           editor.putString("Email", email);
                           editor.putString("phone",phone);
                           editor.putString("password",pass);
                           editor.commit();
                           Intent i = new Intent(MainActivity.this,upload_photo.class);
                           startActivity(i);

                       }
                        // IF ANY OF THE DATA IS INVALID , THEN UNSUCCESSFUL
                       else
                       {
                           Toast.makeText(getApplicationContext(),"LOGIN UNSUCCESSFUL",Toast.LENGTH_SHORT).show();
                       }
                   }
               });

    }



//FUNCTIONS FOR CHECKING THE VALIDITY OF EMAIL

    private boolean isValidEmail(String email)
    {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern=Pattern.compile(EMAIL_PATTERN);
        Matcher matcher=pattern.matcher(email);
        return matcher.matches();
    }
//FUNCTIONS FOR CHECKING THE VALIDITY OF PASSWORD
    private boolean isValidPassword(String password)
    {
        if(password!=null && password.length()>6)
        {
            return true;
        }
        else
            return false;
    }
//FUNCTIONS FOR CHECKING THE VALIDITY OF PHONE NO.
    private boolean isValidPhone(String phone)
    {
        if (phone.length()==10)
        {
            return true;
        }

        return false;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
