package tag.tagqrcode;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Result_activity extends Activity {
    public static final String PREFS_NAME = "LoginPrefs";
    public static final String PREFS_NAME_PERSON="Name";
    TextView scan_Result;
    Button run,show,delete;
   private String rese;
    DataBaseHandler dh;
   List<QrCode> qrCodeList;
    BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


       setContentView(R.layout.activity_result_activity);


        dh = new DataBaseHandler(getApplicationContext());
        qrCodeList=dh.getAll();
        final Intent intent = getIntent();//


        //final String ti = intent.getStringExtra("name");
        SharedPreferences settings = getSharedPreferences(PREFS_NAME_PERSON, 0);


        setTitle("WELCOME" +" " + settings.getString("name","").toString());


        rese = intent.getStringExtra("results");
        scan_Result = (TextView) findViewById(R.id.resultView);
        if(rese != null) {




            scan_Result.setText(rese);
        }else {

            scan_Result.setText("Hello You Are WELCOME");

        }




        adapter = new ArrayAdapter<QrCode>(this,android.R.layout.simple_list_item_1,qrCodeList){
            @Override
            public View getView(int position,View convertView,ViewGroup parent){
                if(convertView==null){

                    convertView = getLayoutInflater().inflate(R.layout.row_qrcode,null);

                  }
                QrCode qr = qrCodeList.get(position);

                TextView name = (TextView) convertView.findViewById(R.id.qr_name);
                name.setText(qr.getQr_Code());
                TextView date = (TextView) convertView.findViewById(R.id.qr_time);
                date.setText(qr.getDate());

                return convertView; }


  };

        run = (Button)findViewById(R.id.button);
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE","QR_CODE_MODE");
                    startActivityForResult(intent,0);

                }catch(Exception e){

                    toast("NO SCANNER FOUND");

                }

            }
        });


       show = (Button) findViewById(R.id.button2);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scan_Result.setVisibility(View.INVISIBLE);
                scan_Result.setText("");
                ListView list = (ListView) findViewById(R.id.listView);
                list.setAdapter(adapter);
            }
        });


        delete = (Button) findViewById(R.id.button3);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              deleteValues();



                
            }
        });

    }
    //toast function which makes the toast accept string
     public void toast(String string){
        Toast.makeText(getApplicationContext(),string,Toast.LENGTH_LONG).show();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.result_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        if(item.getItemId()==R.id.log_out){

            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("logged");
            editor.commit();
            finish();
            Intent i = new Intent(Result_activity.this,Login_activity.class);
            startActivity(i);


        }
        return super.onOptionsItemSelected(item);
    }


// it starts new activity
    @Override
    public void onBackPressed() {

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("logged");
        editor.commit();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();


    }

    public void onActivityResult(int requestCode , int resultCode , Intent intent){


        if(requestCode == 0){

            if(resultCode == RESULT_OK){

              //  String contents = intent.getStringExtra("SCAN_RESULT");
                 String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
     //          Toast toast1 = Toast.makeText(this, "Content:" + contents + " Format:" + format, LENGTH_LONG);
//
                  toast(format);
                String tim = getDatenTime();
                String res = intent.getStringExtra("SCAN_RESULT");
                QrCode qrc = new QrCode(0,res,tim);
                QrCode qrd = dh.addQr(qrc);
                qrCodeList.add(qrd);
                adapter.notifyDataSetChanged();

                Intent i = new Intent(Result_activity.this,Result_activity.class);
                i.putExtra("results",intent.getStringExtra("SCAN_RESULT"));
                startActivity(i);
            }
        }
    }
//returns the current date in string type
    public String getDatenTime(){
        SimpleDateFormat dateFormat=new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        Date date=new Date();
        return dateFormat.format(date);

     }
      //it deletes all values from the table
    public void deleteValues(){


        new AlertDialog.Builder(Result_activity.this)
                .setTitle("Confirm Delete")
                .setMessage("Press ok for clear")
                .setPositiveButton("OK", new OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){

                        dh.clearTable();
                        qrCodeList.clear();
                        adapter.notifyDataSetChanged();



                    }

                })
        .setNegativeButton("Cancel",null)
        .create()
        .show();

    }


}
