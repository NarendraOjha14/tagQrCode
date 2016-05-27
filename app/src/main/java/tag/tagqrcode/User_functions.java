package tag.tagqrcode;

import android.content.Context;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rupak Adhikari on 1/26/2015.
 * Several functions used in Login and Registration..
 */
public class User_functions {

    private JsonParser jsonParser;
    private static String loginURL ="http://www.rupakadhikari.com.np/login_api/";
    private static String registerURL="http://www.rupakadhikari.com.np/login_api/";
    private static String login_tag="login";
    private static String register_tag="register";

    public User_functions(){

        jsonParser = new JsonParser();
    }

    //making login for user
    //params email and password
    public JSONObject loginUser(String email,String password){

        //Here building parameters to be passed
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag",login_tag));
        params.add(new BasicNameValuePair("email",email));
        params.add(new BasicNameValuePair("password",password));
        JSONObject json;
        json = jsonParser.makeHttpRequest(loginURL,params);
        return json;
     }
   /*
    registers the user
    params name , email , password
    */
   public JSONObject registerUser(String name,String email,String password){
        //here building params
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag",register_tag));
        params.add(new BasicNameValuePair("name",name));
        params.add(new BasicNameValuePair("email",email));
        params.add(new BasicNameValuePair("password",password));
         JSONObject json;
        json= jsonParser.makeHttpRequest(registerURL,params);
         return json;
    }
    //function that returns login status

         //function thats clears the login table
        // it is use for making log out



}



