package com.hortichuelas.controlfincas.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hortichuelas.controlfincas.Adapters.AdapterEmpresa;
import com.hortichuelas.controlfincas.Adapters.AdapterList;
import com.hortichuelas.controlfincas.Adapters.AdapterTareas;
import com.hortichuelas.controlfincas.Models.Empresas;
import com.hortichuelas.controlfincas.Models.Lista;
import com.hortichuelas.controlfincas.Models.Tareas;
import com.hortichuelas.controlfincas.R;
import com.hortichuelas.controlfincas.Utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    public static final String Error_Detected = "No se detecta";
    public static final String Write_Success = "Texto grabado correctamente";
    public static final String Write_Error = "Error al grabar";
    public Handler handler;
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writingTagFilters[];
    boolean writeMode;
    Tag myTag;
    Context context = MainActivity.this;
    TextView edit_message;
    TextView nfc_contents;
    private TextView idNave, nave,idTarea,tarea,tipo,nameUsr,horaFija,reloj,fecha;
    private TextView cdEmpresa,idActividad,fechaF,txtRsp;
    Button ActivateButton,HoraFija,btnParada,btnFinJonada;
    private SharedPreferences prefs;
    private LinearLayout layoutCultivo,layoutTarea;
    private  int visible =  0;
    private LinearLayout layoutHoraFija;
    private AsyncHttpClient client;
    private ArrayList<Lista> arrayCultivos = new ArrayList<Lista>();
    private ArrayList<Tareas> arrayTareas = new ArrayList<Tareas>();
    private ArrayList<Empresas> arrayEmpresas = new ArrayList<>();
    private ColorStateList oldResp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind();
        oldResp = txtRsp.getTextColors();
        reloj.setText(Utils.setTime());
        fecha.setText(Utils.setDate());
        actions();
        clock();
        setFecha();
        prefs = getSharedPreferences("prefs",Context.MODE_PRIVATE);
        Utils.setDefaultUser(prefs,nameUsr, idNave, nave,idTarea,tarea,context);


        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter == null){
            Toast.makeText(this, "No se soporta NFC", Toast.LENGTH_SHORT).show();
            //finish();

        }

        //readFromIntent(getIntent());
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),PendingIntent.FLAG_MUTABLE);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writingTagFilters = new IntentFilter[] { tagDetected };

    }
    private  void bind(){
        edit_message = findViewById(R.id.edit_message);
        nfc_contents = findViewById(R.id.nfc_contents);
        idNave = findViewById(R.id.txtIdNave);
        nave = findViewById(R.id.txtNave);
        idTarea = findViewById(R.id.txtIdTarea);
        tarea = findViewById(R.id.txtTarea);
        tipo = findViewById(R.id.txtTipoTarea);
        nameUsr = findViewById(R.id.nameUser);
        horaFija = findViewById(R.id.txtHoraFija);
        reloj = findViewById(R.id.reloj);
        fecha = findViewById(R.id.fecha);
        cdEmpresa = findViewById(R.id.cdEmpresa);
        idActividad = findViewById(R.id.idActividad);
        fechaF = findViewById(R.id.fechaF);
        txtRsp = findViewById(R.id.txtRSP);

        ActivateButton = findViewById(R.id.btnGrabarTicaje);
        btnParada = findViewById(R.id.btnParada);
        HoraFija = findViewById(R.id.btnHoraFija);
        btnFinJonada = findViewById(R.id.btnFinJornada);
        layoutHoraFija = findViewById(R.id.layoutClock);
        layoutCultivo = findViewById(R.id.layoutNave);
        layoutTarea = findViewById(R.id.layoutTarea);
    }
    private  void actions(){
        HoraFija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visible == 0){
                    visible =  1; layoutHoraFija.setVisibility(View.VISIBLE);
                    setTime();


                }else{visible = 0; layoutHoraFija.setVisibility(View.GONE); clock();}

            }
        });
        layoutHoraFija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime();
            }
        });
        ActivateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (myTag == null) {
                        Toast.makeText(context, Error_Detected, Toast.LENGTH_LONG).show();
                    } else {
                        String text = edit_message.getText().toString();
                        write(text, myTag);
                        Toast.makeText(context, Write_Success, Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(context, Write_Error, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (FormatException e) {
                    Toast.makeText(context, Write_Error, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }



        });
        layoutCultivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();
                params.put("getNaves","getNaves");
                params.put("usr", Utils.getUserLoginPrefs(prefs));
                client = new AsyncHttpClient();
                client.post(Utils.service(), params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            JSONObject response = new JSONObject(new String(responseBody).trim());
                            String id = response.getString("naves");
                            //Toast.makeText(context,"Cultivos "+id,Toast.LENGTH_SHORT).show();
                            JSONArray json = new JSONArray(id);
                            JSONObject obj = null;
                            if(arrayCultivos.size()>0){arrayCultivos.clear();}
                            for(int i=0; i<json.length(); i++){
                                obj = json.getJSONObject(i);
                                Lista c = new Lista(obj.getInt("id"), obj.getString("nave"));
                                arrayCultivos.add(c);
                            }
                            View view = getLayoutInflater().inflate(R.layout.list_items, null);
                            RecyclerView recyclerView = view.findViewById(R.id.recycler_items);
                            AlertDialog.Builder cult = new AlertDialog.Builder(context);
                            cult.setView(view);
                            AlertDialog alert = cult.create();
                            AdapterList adapterList = new AdapterList(context,arrayCultivos, idNave, nave,alert);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
                            recyclerView.setAdapter(adapterList);
                            alert.show();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });





            }
        });
        layoutTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();
                params.put("getTareas","getTareas");
                params.put("empresa",Utils.getUserLoginPrefs(prefs));
                params.put("actividad",Utils.getUserActividadPrefs(prefs));
                client = new AsyncHttpClient();
                client.post(Utils.service(), params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            JSONObject response = new JSONObject(new String(responseBody).trim());
                            String id = response.getString("tareas");
                            //Toast.makeText(context,"Cultivos "+id,Toast.LENGTH_SHORT).show();
                            JSONArray json = new JSONArray(id);
                            JSONObject obj = null;
                            if(arrayTareas.size()>0){arrayTareas.clear();}
                            for(int i=0; i<json.length(); i++){
                                obj = json.getJSONObject(i);
                                Tareas t = new Tareas(obj.getInt("id"),obj.getString("tarea"),obj.getString("tipo"));
                                arrayTareas.add(t);
                            }
                            View view = getLayoutInflater().inflate(R.layout.list_items, null);
                            RecyclerView recyclerView = view.findViewById(R.id.recycler_items);
                            AlertDialog.Builder cult = new AlertDialog.Builder(context);
                            cult.setView(view);
                            AlertDialog alert = cult.create();
                            AdapterTareas adapterList = new AdapterTareas(context,arrayTareas,idTarea,tarea,tipo,alert);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
                            recyclerView.setAdapter(adapterList);
                            alert.show();
                        } catch (JSONException e) {
                            Toast.makeText(context,"Esta empresa no tiene tareas registradas\r\nContacte con el adminsitrador",Toast.LENGTH_LONG).show();
                            throw new RuntimeException(e);

                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
            }
        });
        nameUsr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();
                params.put("getEmpresas","getEmpresas");
                params.put("imei",Utils.getUserImeiPrefs(prefs));
                client = new AsyncHttpClient();
                client.post(Utils.service(), params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            JSONObject response = new JSONObject(new String(responseBody).trim());
                            String id = response.getString("empresas");
                            Toast.makeText(context,id,Toast.LENGTH_LONG).show();
                            JSONArray json = new JSONArray(id);
                            JSONObject obj = null;
                            if(arrayEmpresas.size()>0){arrayEmpresas.clear();}
                            for(int i=0; i<json.length(); i++){
                                obj = json.getJSONObject(i);
                                Empresas emp = new Empresas(obj.getInt("codigo"),obj.getString("empresa"),obj.getInt("actividad"));
                                arrayEmpresas.add(emp);
                            }
                            AlertDialog.Builder empresa = new AlertDialog.Builder(context);
                            View view = getLayoutInflater().inflate(R.layout.list_items, null);
                            RecyclerView recyclerView = view.findViewById(R.id.recycler_items);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
                            empresa.setView(view);
                            AlertDialog alert = empresa.create();
                            AdapterEmpresa adapterEmpresa = new AdapterEmpresa(context,arrayEmpresas,cdEmpresa,nameUsr,idActividad,alert,prefs);
                            recyclerView.setAdapter(adapterEmpresa);
                            adapterEmpresa.notifyDataSetChanged();
                            alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    Utils.setDefaultUser(prefs,nameUsr, idNave, nave,idTarea,tarea,context);
                                }
                            });
                            alert.show();

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });

            }
        });
        btnParada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btnFinJonada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoParada = new Intent(MainActivity.this, FinalJornada.class);
                gotoParada.putExtra("fecha",fechaF.getText().toString());
                gotoParada.putExtra("hora",reloj.getText().toString());
                startActivity(gotoParada);
            }
        });

    }
    private  void clock(){
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(visible == 0){
                    reloj.setText(Utils.setTime());
                    fecha.setText(Utils.setDate());
                    handler.postDelayed(this,1000);
                }else{
                    handler.removeCallbacks(this);
                }
            }
        },1000);
    }
    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            buildTagViews(msgs);
        }else{

        }
    }
    private void buildTagViews(NdefMessage[] msgs) {

        if (msgs == null || msgs.length == 0) return;

        String text = "";
//        String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

        try {

            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.toString());
        }
        Utils.insertTicaje(context,text,Utils.getUserLoginPrefs(prefs),fechaF.getText().toString(),reloj.getText().toString(),idNave.getText().toString(),
                idTarea.getText().toString(),tipo.getText().toString(),txtRsp,oldResp);
       //   Toast.makeText(context,"Ticaje "+text+" "+Utils.getUserLoginPrefs(prefs)+" "+fechaF.getText().toString()+" "+
       //   reloj.getText().toString()+" "+idNave.getText().toString()+" "+idTarea.getText().toString()+" "+tipo.getText().toString(),Toast.LENGTH_LONG).show();
       //   edit_message.setText(text);

    }
    private void write(String text, Tag tag) throws IOException, FormatException {
        NdefRecord[] records = { createRecord(text) };
        NdefMessage message = new NdefMessage(records);
        // Instance tag.
        Ndef ndef = Ndef.get(tag);
        // Enable 1/O
        ndef.connect();
        // para escribir
        ndef.writeNdefMessage(message);
        // termina
        ndef.close();
    }
    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {
        String lang       = "es";
        byte[] textBytes  = text.getBytes();
        byte[] langBytes  = lang.getBytes("US-ASCII");
        int    langLength = langBytes.length;
        int    textLength = textBytes.length;
        byte[] payload    = new byte[1 + langLength + textLength];


        payload[0] = (byte) langLength;


        System.arraycopy(langBytes, 0, payload, 1,              langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,  NdefRecord.RTD_TEXT,  new byte[0], payload);

        return recordNFC;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        readFromIntent(intent);
        //Toast.makeText(context,"ACTION "+intent.getAction(),Toast.LENGTH_LONG).show();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
    }


    @Override
    public void onPause(){
        super.onPause();
        WriteModeOff();
    }

    @Override
    public void onResume(){
        super.onResume();
        WriteModeOn();
    }


    /*************************ESCRITURA***************************/
    private void WriteModeOn(){
        writeMode = true;
      //  nfcAdapter.enableForegroundDispatch(this, pendingIntent, writingTagFilters, null);
    }
    /**********************************DESHABILITAR ESCRITURA*****************************/
    private void WriteModeOff(){
        writeMode = false;
    //    nfcAdapter.disableForegroundDispatch(this);
    }


    private void setTime(){
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String h =""; String m ="";
                if(hourOfDay<10){h="0"+hourOfDay;}else{h=""+hourOfDay;}
                if(minute<10){ m="0"+minute;}else{m=""+minute;}
                horaFija.setText(h+":"+m+":00");
                reloj.setText(h+":"+m+":00");
            }
        };
        TimePickerDialog dialog = new TimePickerDialog(context,listener, Utils.getHour(),Utils.getMinute(),true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                String h =""; String m ="";
                if(Utils.getHour()<10){h="0"+Utils.getHour();}else{h=""+Utils.getHour();}
                if(Utils.getMinute()<10){ m="0"+Utils.getMinute();}else{m=""+Utils.getMinute();}
                horaFija.setText(h+":"+m+":00");
                reloj.setText(h+":"+m+":00");
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        //time.show(getSupportFragmentManager(),"time picker");
        dialog.show();
    }
    private void setFecha(){
        fechaF.setText(Utils.setDateF());
    }


}