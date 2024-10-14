package com.hortichuelas.controlfincas.Activities;

import static com.hortichuelas.controlfincas.Utils.Utils.saveUser;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.hortichuelas.controlfincas.R;
import com.hortichuelas.controlfincas.Utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.UUID;

import cz.msebera.android.httpclient.Header;

public class Login extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private Switch remember;
    private Context context = Login.this;
    private Activity activity = Login.this;
    private AsyncHttpClient client;
    private String imei = "";
    private SharedPreferences prefs;
    // Local
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_2);
       // TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELECOM_SERVICE);
       // Toast.makeText(context,imei,Toast.LENGTH_SHORT).show();
        bind();
        prefs = getSharedPreferences("prefs",Context.MODE_PRIVATE);

        insertaDatos();
        //setCredentials(prefs);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);



                //String username = editTextUsername.getText().toString().trim();
                //String password = editTextPassword.getText().toString().trim();

                // Verificar si los campos están vacíos
                /*
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Por favor, ingrese nombre de usuario y contraseña", Toast.LENGTH_SHORT).show();
                } else if (username.equals(username) && password.equals(password)) {
                    // Los datos de inicio de sesión son correctos

                    // Redireccionar al activity_main.xml
                    RequestParams params = new RequestParams();
                    params.put("logIn","logIn");
                    params.put("imei",imei);
                    params.put("usr",username);
                    params.put("pass",password);
                    client = new AsyncHttpClient();

                    client.post(Utils.service(), params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                JSONObject response = new JSONObject(new String(responseBody).trim());

                                String id = response.getString("log");
                               // Toast.makeText(context,id+" ",Toast.LENGTH_SHORT).show();
                                JSONArray json = new JSONArray(id);
                                JSONObject obj = json.getJSONObject(0);
                                String valido = obj.getString("valido");

                                if(valido.equals("UN")){
                                    Toast.makeText(context,"El dispositivo utilizado no está autorizado\r\nContacte con el administrador",Toast.LENGTH_SHORT).show();
                                }else if(valido.equals("NO")){
                                    Toast.makeText(context,"Error en Usuario o contraseña",Toast.LENGTH_SHORT).show();
                                }else{
                                    goToMain();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(context,"Error JSON OBJ",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(context,"ERROR RESPONSE",Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    // Inicio de sesión incorrecto en usr o pass
                    Toast.makeText(Login.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
                * */
                logIn();
            }
        });
    }
    private  void bind(){
        //editTextUsername = findViewById(R.id.editTextUsername);
        //editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        //remember = findViewById(R.id.rememberLog);
    }

    private  void compruebaUsr(SharedPreferences pref){

    }
    private  void insertaDatos(){
        String Id = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        imei = Id;
        String serial = Build.SERIAL;
        RequestParams params = new RequestParams();
        params.put("readPhone","readPhone");
        params.put("Id",Id);
        params.put("serial",serial);
        client = new AsyncHttpClient();
        client.post(Utils.service(), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //Toast.makeText(context,new String(responseBody).trim(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    private void goToMain() {
        Intent intent = new Intent(Login.this, MainActivity.class);

        startActivity(intent);
    }


    private  void logIn(){
        /*
        RequestParams params = new RequestParams();
        params.put("logIn","logIn");
        params.put("imei",imei);
        client = new AsyncHttpClient();
        client.post(Utils.service(), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject response = new JSONObject(new String(responseBody).trim());

                    String id = response.getString("log");
                    // Toast.makeText(context,id+" ",Toast.LENGTH_SHORT).show();
                    JSONArray json = new JSONArray(id);
                    JSONObject obj = json.getJSONObject(0);
                    String valido = obj.getString("valido");

                    if(valido.equals("UN")){
                        Toast.makeText(context,"El dispositivo utilizado no está autorizado\r\nContacte con el administrador",Toast.LENGTH_SHORT).show();
                    }else if(valido.equals("NO")){
                        Toast.makeText(context,"Error en Usuario o contraseña",Toast.LENGTH_SHORT).show();
                    }else{
                        Utils.saveUser(obj.getString("empresa"),obj.getString("actividad"),imei,prefs);
                        goToMain();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context,"Error JSON OBJ",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context,"ERROR RESPONSE",Toast.LENGTH_SHORT).show();
            }
        });
        */
    }
    private void clearPreferences(SharedPreferences prefs){
        prefs.edit().clear().commit();
    }

    private  void setCredentials(SharedPreferences prefs){
        String user= Utils.getUserLoginPrefs(prefs);
        String pass = Utils.getUserPassPrefs(prefs);
        //editTextUsername.setText(user);
        //editTextPassword.setText(pass);
    }




}