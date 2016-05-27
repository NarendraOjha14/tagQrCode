package tag.tagqrcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MyActivity_Splash extends Activity {
    User_functions user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

         final Thread mySplash = new Thread(){
             @Override
             public void run() {
                 try{

                     sleep(3000);//sleeps for 3 second


//calls another activity after 3 seconds
                     Intent i = new Intent(MyActivity_Splash.this, Login_activity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                     startActivity(i);
                     finish();
//
                 }catch (Exception e) {

                     e.printStackTrace();

                 }
             }
         };


        mySplash.start();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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
}
