package com.unal.reto10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Spinner tipo_de_residuos_spinner;
    private ArrayList<String> tipo_de_residuoss;
    private ArrayAdapter<String> tipo_de_residuos_adapter;
    private Spinner raz_n_social_spinner;
    private ArrayList<String> raz_n_socials;
    private ArrayAdapter<String> raz_n_social_adapter;
    private ListView list;
    private ArrayList<String> datos;
    private ArrayAdapter<String> cod_adapter;
    private Context context = this;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tipo_de_residuoss = new ArrayList<>();
        raz_n_socials = new ArrayList<>();
        datos = new ArrayList<>();
        queue = Volley.newRequestQueue(this);
        String url = "https://www.datos.gov.co/resource/c2fr-ezse.json?$select=distinct%20tipo_de_residuos&$order=tipo_de_residuos%20ASC";
        tipo_de_residuos_spinner = (Spinner) findViewById(R.id.residuos);
        raz_n_social_spinner = (Spinner) findViewById(R.id.social);
        list = findViewById(R.id.list);
        //---------------
        tipo_de_residuoss.add("Todos");
        JsonArrayRequest departamentos = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject tmp = null;
                            try {
                                tmp = response.getJSONObject(i);
                                tipo_de_residuoss.add(tmp.getString("tipo_de_residuos"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        tipo_de_residuos_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, tipo_de_residuoss);
                        tipo_de_residuos_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        tipo_de_residuos_spinner.setAdapter(tipo_de_residuos_adapter);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        tipo_de_residuos_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                raz_n_socials.clear();
                String tmp = (String) parent.getItemAtPosition(pos);
                String url;
                if(tmp == "Todos"){
                    url = "https://www.datos.gov.co/resource/c2fr-ezse.json?";
                }else{
                    url = "https://www.datos.gov.co/resource/c2fr-ezse.json?$select=distinct%20raz_n_social&tipo_de_residuos="+ tmp + "&$order=raz_n_social%20ASC";
                }
                JsonArrayRequest municipios = new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject tmp = null;
                                    try {
                                        tmp = response.getJSONObject(i);
                                        raz_n_socials.add(tmp.getString("raz_n_social"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                raz_n_social_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, raz_n_socials);
                                raz_n_social_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                raz_n_social_spinner.setAdapter(raz_n_social_adapter);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                queue.add(municipios);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        raz_n_social_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                datos.clear();
                String tmp = (String) parent.getItemAtPosition(pos);
                tmp = tmp.replaceAll(" ", "%20");
                String url = "https://www.datos.gov.co/resource/c2fr-ezse.json?raz_n_social=" + tmp;
                JsonArrayRequest codes = new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject tmp = null;
                                    try {
                                        tmp = response.getJSONObject(i);
                                        String tmp2 = "Departamento: " + tmp.getString("departamento") + "\n";
                                        tmp2 += "Municipio: " + tmp.getString("municipio") + "\n";
                                        tmp2 += "Programa de recolección: " + tmp.getString("programa_de_recolecci_n") + "\n";
                                        tmp2 += "Tipo de residuos: " + tmp.getString("tipo_de_residuos") + "\n";
                                        tmp2 += "Codigo dane: " + tmp.getString("codigo_dane") + "\n";
                                        tmp2 += "Direccion: " + tmp.getString("direccion") + "\n";
                                        datos.add(tmp2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                cod_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, datos);
                                list.setAdapter(cod_adapter);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("REQ", "bad");
                            }
                        });
                queue.add(codes);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        queue.add(departamentos);

    }
}
