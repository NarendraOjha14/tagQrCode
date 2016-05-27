package tag.tagqrcode;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;




import static android.widget.Toast.LENGTH_LONG;

public class Login_activity extends Activity  {


    public static final String PREFS_NAME = "LoginPrefs";
    public static final String PREFS_NAME_PERSON="Name";
    EditText inputemail,inputpassword;
    Button login;
    TextView msg;
    Dialog pdialog;
    JsonParser jsonParser = new JsonParser();

    String re="";



    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);


        SharedPreferences sharedPreferences=getSharedPreferences(PREFS_NAME,0);

        if(sharedPreferences.getString("logged","").toString().equals("logged")){

            Intent intent=new Intent(Login_activity.this,Result_activity.class);


            startActivity(intent);
        }



          inputemail = (EditText) findViewById(R.id.username);
          inputpassword = (EditText) findViewById(R.id.password);

         msg = (TextView)findViewById(R.id.register);


//setting button for login
            login = (Button) findViewById(R.id.signin);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

     //checking internet connection using function of CheckConnection class

                    if (CheckConnection.checkInternetConnection(getApplicationContext())) {

                        new LoginUser().execute();//asyntask executing...



                    } else

                    {

                        final Dialog dialog = new Dialog(Login_activity.this);
                        dialog.setContentView(R.layout.nointernet);
                        dialog.setTitle("Alert!!!");

                        Button button = (Button) dialog.findViewById(R.id.dismiss);

                        button.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                dialog.dismiss();
                                //finish();
                            }
                        });
                        dialog.show();
                    }

                }
           }
      );





//setting textview eventlistner..it opens new activity
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Register_activity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });
      }
//asyn task which connects api in sever via internet and return the login status..

    class LoginUser extends AsyncTask<String,String,String>{

        boolean failure=false;

       @Override
       protected void onPreExecute(){

           super.onPreExecute();
           pdialog = new ProgressDialog(Login_activity.this);
           pdialog.setTitle("LoggingIn");
           pdialog.setCancelable(true);
           pdialog.show();

        }
        @Override
        protected String doInBackground(String... args) {



                String email = inputemail.getText().toString();
                String password = inputpassword.getText().toString();
                User_functions user = new User_functions();
                Log.d("Button", "Login");
                JSONObject json = user.loginUser(email, password);

                try {
                    if (json.getString(KEY_SUCCESS) != null) {


//                       msg.setText("");
                        String res = json.getString(KEY_SUCCESS);
                        if (Integer.parseInt(res) == 1) {
                            //login sucessful


                            JSONObject json_user = json.getJSONObject("user");
                           //using shared prefernce to remer logged user..it sets the PREFS_NAME to logged and its value to logged..
                            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("logged", "logged");
                            editor.commit();
                           //using shared preference to remember user Name
                            SharedPreferences setname=getSharedPreferences(PREFS_NAME_PERSON,0);
                            SharedPreferences.Editor editname= setname.edit();

                            editname.putString("name", json_user.getString(KEY_NAME) );
                            editname.commit();


                            Intent i = new Intent(Login_activity.this, Result_activity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.putExtra("name", json_user.getString(KEY_NAME));
                             finish();
                            startActivity(i);



                            re = "Success";
                           // Toast.makeText(Login_activity.this,re,Toast.LENGTH_LONG).show();
                            return re;

                        } else {

                            re = "invalid username or password";
                            //Toast.makeText(Login_activity.this,re,Toast.LENGTH_LONG).show();


                            return re;
//
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            return null;
        }

        protected void onPostExecute(String file_url) {
            pdialog.dismiss();


        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
     // on back pressed exiting app
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();

    }

}
