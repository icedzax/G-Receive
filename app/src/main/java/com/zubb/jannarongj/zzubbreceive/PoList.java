package com.zubb.jannarongj.zzubbreceive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PoList extends AppCompatActivity implements TextWatcher {

    final Context context = this;
    ConnectionClass connectionClass;
    UserHelper usrHelper;
    Version vers;
    ProgressBar pbbar;
    EditText hideEdt,vbelnEdt;
    Button btnVeln;
    ListView list_vbeln ;
    SimpleAdapter ADA;
    LinearLayout lnse ;

    List<Map<String, String>> vbelnlist  = new ArrayList<Map<String, String>>();

    String scanresult,tab_vbeln,tab_posnr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_po_list);

        vers = new Version();
        usrHelper = new UserHelper(this);
        connectionClass = new ConnectionClass();
        pbbar = (ProgressBar)findViewById(R.id.pbbar);
        hideEdt = (EditText) findViewById(R.id.hedt);
        vbelnEdt = (EditText) findViewById(R.id.edt_vbeln);
        list_vbeln = (ListView) findViewById(R.id.lvvbeln);
        btnVeln =(Button)findViewById(R.id.btnvbeln);
        lnse = (LinearLayout)findViewById(R.id.lnse);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        pbbar.setVisibility(View.GONE);


        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        VbelnFList vbList = new VbelnFList();
        vbList.execute("");

        btnVeln.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                vbelnEdt = (EditText) findViewById(R.id.edt_vbeln);

                VbelnFList vbList = new VbelnFList();
                vbList.execute(vbelnEdt.getText().toString().trim());

                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(lnse.getWindowToken(), 0);
            }

        });



        vbelnEdt.addTextChangedListener(this);

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().trim().length() == 0) {
            VbelnFList vbList = new VbelnFList();
            vbList.execute("");
        }

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

            String[] from = {"ponum","ARKTX" };
            int[] views = {R.id.vbeln,R.id.arktx};
            ADA = new SimpleAdapter(PoList.this,
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

                    Intent i = new Intent(PoList.this, ReceivePo.class);
                    i.putExtra("vbeln", (String) obj.get("ponum"));

                    //Toast.makeText(TransferList.this, (String) obj.get("POSNR"), Toast.LENGTH_SHORT).show();

                    startActivity(i);

                }
            });
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
                    String where = "";
                    if(params[0]==null || params[0].equals("")){
                        where = "";
                    }else{
                        where = " where ponum like '%"+params[0].trim()+"%' ";
                    }

                    String query = "SELECT convert(nvarchar(20),docdate,103) as wadat,* " +
                            "  FROM vw_ponum " + where ;

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    vbelnlist.clear();
                    while (rs.next()) {
                        Map<String, String> datanums = new HashMap<String, String>();
                        datanums.put("wadat", rs.getString("wadat"));
                        datanums.put("ponum", rs.getString("ponum"));
                        datanums.put("vendor", rs.getString("vendor"));
                        datanums.put("doc", rs.getString("doc"));
                        datanums.put("MATNR",rs.getString("MATNR"));
                        datanums.put("ARKTX",rs.getString("ARKTX"));

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
