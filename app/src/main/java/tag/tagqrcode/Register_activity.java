package tag.tagqrcode;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;



public class Register_activity extends Activity {
    EditText full_name,inputemail,password_register,confirm_password;
    Button signup;
    TextView login;
    Dialog rdialog;
    //json response mode pre set in api
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
        setContentView(R.layout.activity_register_activity);
        full_name= (EditText)findViewById(R.id.name);
        inputemail=(EditText)findViewById(R.id.email);
        password_register=(EditText)findViewById(R.id.password_register);
        confirm_password=(EditText)findViewById(R.id.password_confirm);
        signup =(Button)findViewById(R.id.register);
        login=(TextView)findViewById(R.id.login);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //validating text feild
                if (validated().equals("true")) {
                 //checking internet connection before registering user
                    if (CheckConnection.checkInternetConnection(getApplicationContext())) {

                        new RegisterTry().execute();//asyntask for registering user


                    } else

                    {

                        final Dialog dialog = new Dialog(Register_activity.this);
                        dialog.setContentView(R.layout.nointernet);
                        dialog.setTitle("Alert!!!");

                        Button button = (Button) dialog.findViewById(R.id.dismiss);

                        button.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                Intent i = new Intent(Intent.ACTION_MAIN);
                                i.addCategory(Intent.CATEGORY_HOME);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);

                            }
                        });
                        dialog.show();
                    }


                }else
                {
                    Toast.makeText(Register_activity.this,"cannot be register",Toast.LENGTH_LONG).show();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),Login_activity.class);
                startActivity(i);
            }
        });


    }


    class RegisterTry extends AsyncTask<String ,String,String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            rdialog = new ProgressDialog(Register_activity.this);
            rdialog.setTitle("Registering User");
            rdialog.setCancelable(true);
            rdialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            String msg = new String();
            String name = full_name.getText().toString();
            String email = inputemail.getText().toString();
            String password = password_register.getText().toString();
            User_functions user = new User_functions();
            JSONObject json = user.registerUser(name, email, password);

            //check for json response
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    msg = "";
                    String res = json.getString(KEY_SUCCESS);
                    if (Integer.parseInt(res) == 1) {
                  //sucess then calls another activity
                        Intent login = new Intent(getApplicationContext(), Login_activity.class);
                        startActivity(login);
                        finish();
                        return msg;
                    } else {

                        msg = "Register failed.";
                        return msg;

                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;



    }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            rdialog.dismiss();
        }
    }
//validates the input fields for registering

    private String validated() {
           String re ="";
        if (TextUtils.isEmpty(full_name.getText())) {
            full_name.setError("Name Cannot be empty");
            full_name.focusSearch(View.FOCUS_DOWN);
        } else if (TextUtils.isEmpty(inputemail.getText())) {

            inputemail.setError("Username or email required");
            full_name.focusSearch(View.FOCUS_DOWN);
        } else if (TextUtils.isEmpty(password_register.getText())) {

            password_register.setError("Password needed");
            password_register.focusSearch(View.FOCUS_DOWN);


        } else if(!password_register.getText().toString().equals( confirm_password.getText().toString())){

            confirm_password.setError("password must match ");
            confirm_password.focusSearch(View.FOCUS_DOWN);

        }else{
            re="true";
        }


       return re;
       }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register_activity, menu);
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
    //on back kills all the process
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
