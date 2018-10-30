package com.zubb.jannarongj.zzubbreceive;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferList extends AppCompatActivity implements TextWatcher {

    final Context context = this;
    ConnectionClass connectionClass;
    UserHelper usrHelper;
    Version vers;
    ProgressBar pbbar;
    EditText hideEdt,vbelnEdt;
    TextView txt_vbeln;
    ListView list_vbeln ;
    SimpleAdapter ADA;

    List<Map<String, String>> vbelnlist  = new ArrayList<Map<String, String>>();

    String scanresult,tab_vbeln,tab_posnr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_list);

        vers = new Version();
        usrHelper = new UserHelper(this);
        connectionClass = new ConnectionClass();
        pbbar = (ProgressBar)findViewById(R.id.pbbar);
        hideEdt = (EditText) findViewById(R.id.hedt);
        vbelnEdt = (EditText) findViewById(R.id.edt_vbeln);
        list_vbeln = (ListView) findViewById(R.id.lvvbeln);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        pbbar.setVisibility(View.GONE);


        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        VbelnFList vbList = new VbelnFList();
        vbList.execute("");



        String[] from = {"VBELN","ARKTX","POSNR","CARLICENSE" };
         int[] views = {R.id.vbeln,R.id.arktx,R.id.posnr,R.id.carlicense};
         ADA = new SimpleAdapter(TransferList.this,
                vbelnlist, R.layout.adp_listitem, from,
                views);
        list_vbeln.setAdapter(ADA);
        list_vbeln.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                        .getItem(arg2);
                arg1.setSelected(true);

                Intent i = new Intent(TransferList.this, ReceiveTransf.class);
                i.putExtra("vbeln", (String) obj.get("VBELN"));
                i.putExtra("posnr", (String) obj.get("POSNR"));

                //Toast.makeText(TransferList.this, (String) obj.get("POSNR"), Toast.LENGTH_SHORT).show();

                startActivity(i);

            }
        });

        vbelnEdt.addTextChangedListener(this);

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        ADA.getFilter().filter(s);
       // Toast.makeText(TransferList.this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void afterTextChanged(Editable s) {}

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}




    public class VbelnFList extends AsyncTask<String, String, String> {

        String z = "";

        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {


            //Toast.makeText(TransferList.this, z, Toast.LENGTH_SHORT).show();

            pbbar.setVisibility(View.GONE);

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String query = "SELECT convert(nvarchar(20),WADAT_IST,103) as wadat,VBELN,POSNR,KUNNR,AR_NAME,CARLICENSE " +
                            "      ,MATNR,ARKTX,NTGEW,VRKME,LFIMG " +
                            "  FROM tbl_shipmentplan " +
                            "  where left(vbeln,1) = '3' and WADAT_IST > getdate()-1 and WADAT_IST < getdate()+1" ;
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();


                    while (rs.next()) {
                        Map<String, String> datanums = new HashMap<String, String>();
                        datanums.put("wadat", rs.getString("wadat"));
                        datanums.put("VBELN", rs.getString("VBELN")+"-"+rs.getString("POSNR"));
                        datanums.put("POSNR", rs.getString("POSNR"));
                        datanums.put("KUNNR", rs.getString("KUNNR"));
                        datanums.put("AR_NAME", rs.getString("AR_NAME"));
                        datanums.put("CARLICENSE", rs.getString("CARLICENSE"));
                        datanums.put("MATNR",rs.getString("MATNR"));
                        datanums.put("ARKTX",rs.getString("ARKTX"));
                        datanums.put("NTGEW",rs.getString("NTGEW"));
                        datanums.put("VRKME",rs.getString("VRKME"));
                        datanums.put("LFIMG",rs.getString("LFIMG"));
                        vbelnlist.add(datanums);

                    }

                    // z = "Success";
                    z = query ;
                }
            } catch (Exception ex) {

                z = ex.getMessage().toString();
                //"Error retrieving data from table";

            }

            return z;
        }
    }

}
