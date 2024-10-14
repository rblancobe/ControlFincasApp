package com.hortichuelas.controlfincas.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.autofill.AutofillId;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hortichuelas.controlfincas.Models.Messages;
import com.hortichuelas.controlfincas.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.ConsoleHandler;

import cz.msebera.android.httpclient.Header;

public class Utils {

    public static AsyncHttpClient client;
    public static  int getHour(){
        Calendar cal = new GregorianCalendar();
        return cal.get(Calendar.HOUR_OF_DAY);
    }
    public static  int getMinute(){
        Calendar cal = new GregorianCalendar();
        return cal.get(Calendar.MINUTE);
    }
    public static String getDiaSemana(int dia){
        String d="";
        switch (dia){
            case 1: d="Lunes"; break;
            case 2: d="Martes"; break;
            case 3: d="Miercoles"; break;
            case 4: d="Jueves"; break;
            case 5: d="Viernes"; break;
            case 6: d="Sabado"; break;
            case 7: d="Domingo"; break;
        }
        return d;
    }
    public static String getMes(int mes){
        String m ="";
        switch (mes){
            case 0: m="Enero"; break;
            case 1: m="Febrero"; break;
            case 2: m="Marzo"; break;
            case 3: m="Abril"; break;
            case 4: m="Mayo"; break;
            case 5: m="Junio"; break;
            case 6: m="Julio"; break;
            case 7: m="Agosto"; break;
            case 8: m="Septiembre"; break;
            case 9: m="Octubre"; break;
            case 10: m="Noviembre"; break;
            case 11: m="Diciembre"; break;
        }
        return m;
    }
    public static String getUserImeiPrefs(SharedPreferences prefs){
        return prefs.getString("imei","");
    }
    public static String getUserActividadPrefs(SharedPreferences prefs){
        return prefs.getString("actividad","");
    }
    public static String getUserLoginPrefs(SharedPreferences prefs){
        return prefs.getString("empresa","");
    }
    public static String getUserPassPrefs(SharedPreferences prefs){
        return prefs.getString("pass","");
    }

    public static  void insertTicaje(Context context, String nfc, String empresa, String fecha, String hora, String idNave, String idTarea, String oper, TextView rsp, ColorStateList old){
       // Toast.makeText(context,"Datos: "+nfc+" "+empresa+" "+fecha+" "+hora+" "+idNave+" "+idTarea+" "+oper,Toast.LENGTH_LONG).show();
        RequestParams params = new RequestParams();
        params.put("insertTicaje","insertTicaje");
        params.put("nfc",nfc);
        params.put("empresa",empresa);
        params.put("fecha",fecha);
        params.put("hora",hora);
        params.put("idNave",idNave);
        params.put("idTarea",idTarea);
        params.put("operacion",oper);
        client = new AsyncHttpClient();
        client.post(Utils.service(), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody).trim();
                String[] datos = response.split("\\|");
                String alert ="";
                if(datos.length>0){
                    switch (datos[0]){
                        case "OK":
                            alert=datos[1];
                            break;
                        case "EXISTE":
                            alert=Messages.M_101;
                            rsp.setTextColor(context.getResources().getColor(R.color.colorHorti));
                            break;
                        case "INIT":
                            alert=Messages.M_105;
                            rsp.setTextColor(context.getResources().getColor(R.color.colorHorti));
                            break;
                        case "NO_EXISTE":
                            alert=Messages.M_104;
                            break;
                        case "ERROR":
                            alert=Messages.M_104;
                            rsp.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                            break;
                        case "BLOQUEADO":
                            alert=Messages.M_103;
                            rsp.setTextColor(context.getResources().getColor(R.color.colorAccent));
                            break;
                    }
                    rsp.setText(alert.toUpperCase().trim());
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rsp.setText("");
                            rsp.setTextColor(old);
                        }
                    },2000);
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public static void saveUser(String usr,String actividad,String imei,SharedPreferences prefs){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("empresa",usr);
        editor.putString("actividad",actividad);
        editor.putString("imei",imei);
        editor.commit();
        editor.apply();
    }
    public static void saveUserActividad(String usr,String actividad,SharedPreferences prefs){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("empresa",usr);
        editor.putString("actividad",actividad);
        editor.commit();
        editor.apply();
    }

    public static String setDate(){
        String fecha = "";
        Calendar cal = new GregorianCalendar();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int sem = cal.get(Calendar.DAY_OF_WEEK);
        String d = ""; String m="";
        if(day<10){d="0"+day;} else{d=""+day;}
        if(month+1<10){m="0"+(month+1);}else{m=""+(month+1);}
        fecha = Utils.getDiaSemana(sem)+", "+d+" de "+ Utils.getMes(month)+" de "+year;
        return fecha;
    }
    public static String setDateF(){
        String fecha = "";
        Calendar cal = new GregorianCalendar();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        String d = ""; String m="";
        if(day<10){d="0"+day;} else{d=""+day;}
        if(month+1<10){m="0"+(month+1);}else{m=""+(month+1);}
        fecha = year+"-"+m+"-"+d;
        return fecha;
    }
    public static void setDefaultUser(SharedPreferences prefs, TextView nameUsr , TextView idCultivo, TextView cultivo, TextView idTarea, TextView tarea, Context context){
        RequestParams params = new RequestParams();
        params.put("getDefaults","getDefaults");
        params.put("usr",Utils.getUserLoginPrefs(prefs));
        params.put("actividad",Utils.getUserActividadPrefs(prefs));
        client = new AsyncHttpClient();
        client.post(Utils.service(), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseStr = new String(responseBody).trim();
                if (!responseStr.isEmpty()) {
                  //  try {
                       // JSONObject response = new JSONObject(responseStr);
                       //String id = response.getString("defaults");
                        //JSONArray json = new JSONArray(id);
                        //JSONObject obj = json.getJSONObject(0);
                        //nameUsr.setText(obj.getString("usuario"));
                       // idCultivo.setText(obj.getInt("idNave")+"");
                        //cultivo.setText(obj.getString("nave"));
                        //idTarea.setText(obj.getInt("idTarea")+"");
                        //tarea.setText(obj.getString("tarea"));
                   // } //catch (JSONException e) {
                        //throw new RuntimeException(e);
                    }
               // } else {
                    Log.e("Response", "Respuesta vacía desde el servidor");
               // }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context,"No se reciben datos por defecto para ésta empresa\r\nContacte con el Administrador",Toast.LENGTH_LONG).show();
            }

        });
    }

    public static String setTime(){
        Calendar cal = new GregorianCalendar();
        int hora = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);
        String h = ""; String m=""; String s="";
        if(hora<10){h="0"+hora;}else{h=""+hora;}
        if(minute<10){m="0"+minute;} else{ m=""+minute;}
        if(seconds<10){s="0"+seconds;} else{ s=""+seconds;}
        return h+":"+m+":"+s;

    }
    public static void savePref(SharedPreferences prefs, String value,String name){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(name,value);
        editor.commit();
        editor.apply();
    }
    public static String service(){
       // return "http://192.168.2.75/HortichuelasNet/CpFincas/service.php";
        return "https://net.hortichuelas.es:450/CpFincas/service.php";
    }

    public  static  void toast(Context context,String texto){
        Toast alert = new Toast(context);
        alert.setDuration(Toast.LENGTH_LONG);
        View vista = new View(context);
        LinearLayout layout = new LinearLayout(context);

        layout.addView(vista);
        alert.setView(vista);
        alert.show();

    }

}
