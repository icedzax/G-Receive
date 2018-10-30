package com.zubb.jannarongj.zzubbreceive;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileFilter;
import java.security.interfaces.RSAKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.Inflater;

public class ReceiveTransf extends AppCompatActivity  {

    final Context context = this;
    MLocation loc;
    Raiserror raise;
    ConnectionClass connectionClass;
    UserHelper usrHelper;
    Version vers;
    ProgressBar pbbar;
    EditText hideEdt;
    TextView txt_sumcount,txt_sumqty,txt_sumweight,txtRsLoc,txt_vbeln,txt_ar_name,txt_arktx,txt_carlicense,txt_loc,tx_pil,tx_ch;
    ListView lvvbeln,lvitem;
    Button btnLocSv,btndelete;
    List<Map<String, String>> itemlist  = new ArrayList<Map<String, String>>();
    List<Map<String, String>> vbelnlist  = new ArrayList<Map<String, String>>();
    View vxl ;
    ForegroundColorSpan frcRed = new ForegroundColorSpan(Color.RED);
    ForegroundColorSpan frcBlack = new ForegroundColorSpan(Color.BLACK);

    String itxt,user,ver,rmd_id,rmd_date,rmd_no,rmd_charge,r_bundle,r_qty,matcode,rmd_period,rmd_spec,rmd_size,rmd_grade,rmd_length,rmd_weight,rmd_qa_grade,rmd_remark,rmd_plant,rmd_station;

    String scanresult,g_vbeln,g_posnr,ch,pil;
    String dSize,dBar_id,dCharge,dBundle,dVbeln,dPonum,dUser,dStamp,dLocation;
    String h_vbeln,h_posnr,h_arktx,h_matnr,h_carlicense,h_kunnr,h_ar_name,tab_hn,tab_bun,tab_id;
    int erDup;
    int erNf;
    int erNl;
    int erNm;
    int erMm;

    public int getErDup() {
        return erDup;
    }

    public void setErDup(int erDup) {
        this.erDup = erDup;
    }

    public int getErNf() {
        return erNf;
    }

    public void setErNf(int erNf) {
        this.erNf = erNf;
    }

    public int getErNl() {
        return erNl;
    }

    public void setErNl(int erNl) {
        this.erNl = erNl;
    }

    public int getErNm() {
        return erNm;
    }

    public void setErNm(int erNm) {
        this.erNm = erNm;
    }

    public int getErMm() {
        return erMm;
    }

    public void setErMm(int erMm) {
        this.erMm = erMm;
    }


    int sumcount,sumqty,sumweight ;
    ContextThemeWrapper cw = new ContextThemeWrapper( this, R.style.AlertDialogTheme );
    Locale THLocale = new Locale("en", "TH");
    NumberFormat nf = NumberFormat.getInstance(THLocale);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_transf);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {

                g_vbeln = null;
                g_posnr = null;


            } else {

                g_vbeln = extras.getString("vbeln").trim();
                g_posnr = extras.getString("posnr").trim();
            }

        } else {

            g_vbeln = (String) savedInstanceState.getSerializable("vbeln");
            g_posnr = (String) savedInstanceState.getSerializable("posnr");

        }
        raise = new Raiserror();
        loc = new MLocation();
        vers = new Version();
        usrHelper = new UserHelper(this);
        connectionClass = new ConnectionClass();
        pbbar = (ProgressBar)findViewById(R.id.pbbar);
        hideEdt = (EditText)findViewById(R.id.hedt);
        txt_vbeln = (TextView)findViewById(R.id.txt_vbeln);
        txt_ar_name = (TextView)findViewById(R.id.txt_ar_name);
        txt_arktx = (TextView)findViewById(R.id.txt_arktx);
        txt_carlicense = (TextView)findViewById(R.id.txt_carlicense);
        txt_loc = (TextView)findViewById(R.id.txt_loc);
        txt_sumcount =(TextView) findViewById(R.id.sumcount);
        txt_sumqty =(TextView) findViewById(R.id.sumqty);
        txt_sumweight =(TextView) findViewById(R.id.sumweight);
        lvitem = (ListView)findViewById(R.id.lvitem);
        btndelete = (Button)findViewById(R.id.btndelete);
        loc.setFr("R");
        loc.setLr("L");

        FillList fillList = new FillList();
        fillList.execute((g_vbeln.trim().substring(0,g_vbeln.trim().indexOf("-"))),g_posnr.trim());

        user =  usrHelper.getUserName();
        ver = usrHelper.getVer();

        pbbar.setVisibility(View.GONE);

        btndelete.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {


                if (tab_hn == null) {
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(ReceiveTransf.this);
                    builder.setMessage("กรุณาเลือกรายการที่จะลบ");
                    builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

                    builder.show();

                } else {
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(ReceiveTransf.this);
                    builder.setMessage("ลบรายการ " + tab_hn +" หรือไม่ ?");
                    builder.setPositiveButton("ยกเลิก", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DeleteItem deletePro = new DeleteItem();
                            deletePro.execute(tab_id);
                            dialog.dismiss();
                        }
                    });
                    builder.show();

                }

            }
        });


        hideEdt.requestFocus();
        hideEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            String demo ="";
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if(hideEdt.getText().toString().trim().contains("DEMO")){
                        demo = hideEdt.getText().toString().trim().replace("DEMO","");
                    }else{
                        demo = hideEdt.getText().toString().trim();
                    }
                    insertSCAN(demo);
                }

                return false;
            }

        });

    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent KEvent)
    {
        int keyaction = KEvent.getAction();

        if(keyaction == KeyEvent.ACTION_DOWN)
        {
            int keycode = KEvent.getKeyCode();

            if(keycode == 120){
                hideEdt.requestFocus();
            }
        }
        return super.dispatchKeyEvent(KEvent);
    }
    public void insertSCAN(String rsscan){

        if(rsscan.length()>0){
            this.scanresult = rsscan;

            AddProScan addProScan = new AddProScan();
            addProScan.execute(rsscan);

            this.hideEdt.setText("");
        }else{
            this.hideEdt.setText("");
        }
        this.hideEdt.setText("");
    }



    public class AddProScan extends AsyncTask<String, String, String> {

        String z = "";

        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String r) {


            //Toast.makeText(ReceiveTransf.this,xx,Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {

                FillList fillList = new FillList();
                fillList.execute((g_vbeln.trim().substring(0,g_vbeln.trim().indexOf("-"))),g_posnr.trim());

                Toast.makeText(ReceiveTransf.this, z, Toast.LENGTH_SHORT).show();
            }else{
                //onErrorDialog(raise.geteDup(),raise.getNotFound(),raise.geteMis(),raise.geteNmat(),raise.geteNlocS());
                onErrorDialog(getErDup(),getErNf(),getErMm(),getErNm(),getErNl());

                /*Log.d("e1Dup", String.valueOf(getErDup()));
                Log.d("e1NF", String.valueOf(getErNf()));
                Log.d("e1MM", String.valueOf(getErMm()));
                Log.d("e1NM", String.valueOf(getErNm()));
                Log.d("e1NL", String.valueOf(getErNl()));*/

               // Toast.makeText(ReceiveTransf.this, z, Toast.LENGTH_SHORT).show();

            }
            pbbar.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "พบปัญหาการเชื่อมต่อ";
                } else {
                    String barCheck = "" ;
                    String cDup = "" ;

                    String checkEx = "Select bar_id from vw_barcode_item where bar_id = '" + params[0].trim() + "' ";
                    PreparedStatement getCheck = con.prepareStatement(checkEx);
                    ResultSet cks = getCheck.executeQuery();
                    while (cks.next()) {
                        barCheck = cks.getString("bar_id");
                    }

                    setErDup(0);setErNl(0);setErNm(0);setErMm(0);setErNf(0);

                    if(barCheck==null || barCheck.equals("")){
                        //TODO ERROR ITEM DOES NOT EXISTS AWW !
                        setErNf(1);

                    }else{
                        String checkDup = "Select top 1 * from tbl_receive where item_barcode = '" + params[0].trim() + "' order by add_stamp desc  ";
                        PreparedStatement cd = con.prepareStatement(checkDup);
                        ResultSet cdps = cd.executeQuery();

                        while (cdps.next()) {
                            dBar_id = cdps.getString("item_barcode");
                            dCharge = cdps.getString("charge");
                            dBundle = cdps.getString("bundle");
                            dUser = cdps.getString("user_add");
                            dStamp = cdps.getString("add_stamp");
                            dLocation = cdps.getString("location");
                            dVbeln = cdps.getString("vbeln");
                            dPonum = cdps.getString("ponum");
                            dSize = cdps.getString("rmd_size");
                            cDup = cdps.getString("item_barcode");
                        }

                        if(cDup.equals("")){
                            setErDup(0);
                        }else{
                            setErDup(1);
                        }

                        String scanS = "SELECT rmd_id " +
                                " ,rmd_date,rmd_no,rmd_charge,r_bundle,r_qty,matcode " +
                                " ,rmd_period,rmd_spec,rmd_size,rmd_grade " +
                                " ,rmd_length,rmd_weight,rmd_qa_grade,rmd_remark " +
                                " ,rmd_plant,rmd_station,r_qty " +
                                "  FROM  " +
                                "  vw_barcode_item where bar_id = '" + params[0].trim() + "' ";
                        PreparedStatement getDataSt = con.prepareStatement(scanS);
                        ResultSet rs = getDataSt.executeQuery();
                        while (rs.next()) {

                            rmd_id = rs.getString("rmd_id");
                            rmd_date =rs.getString("rmd_date");
                            rmd_no = rs.getString("rmd_no");
                            rmd_charge = rs.getString("rmd_charge");
                            r_bundle = rs.getString("r_bundle");
                            r_qty = rs.getString("r_qty");
                            matcode = rs.getString("matcode");
                            rmd_period = rs.getString("rmd_period");
                            rmd_spec = rs.getString("rmd_spec");
                            rmd_size = rs.getString("rmd_size");
                            rmd_grade = rs.getString("rmd_grade");
                            rmd_length = rs.getString("rmd_length");
                            rmd_weight = rs.getString("rmd_weight");
                            rmd_qa_grade = rs.getString("rmd_qa_grade");
                            rmd_remark = rs.getString("rmd_remark");
                            rmd_plant = rs.getString("rmd_plant");
                            rmd_station = rs.getString("rmd_station");

                        }

                        if(loc.getCurLoc()==false){
                           setErNl(1);
                        }

                        if(matcode==null || matcode.equals("")){
                            setErNm(1);
                        }else{
                            String hmat = h_matnr.trim();
                            String smat = matcode.trim();
                            String hm,sm;
                            int mm = 3 ;
                            if(hmat.substring(0,2).equals("BF") || hmat.substring(0,2).equals("BM")){
                                mm = 1 ;
                            }
                            sm = smat.substring(0,mm)+""+smat.substring(5,smat.length());
                            hm = hmat.substring(0,mm)+""+hmat.substring(5,hmat.length());
                            if(hm.equals(sm)){
                                setErMm(0);
                            }else{
                                setErMm(1);
                            }
                        }

                        if(getErDup()==0 && getErNf()==0 &&getErMm()==0 &&getErNm()==0 &&getErNl()==0){

                            String insrt = "insert into tbl_receive (vbeln,posnr,ponum,carlicense,item_barcode,rmd_id,rmd_date,rmd_no,charge " +
                                    ",bundle,qty,matcode,location,rmd_period,rmd_spec,rmd_size,rmd_grade,rmd_length " +
                                    ",rmd_weight,rmd_qa_grade,rmd_remark,rmd_plant,rmd_station,user_add,add_stamp) " +

                                    "values ('"+h_vbeln+"','"+h_posnr+"',NULL,'"+h_carlicense+"','"+params[0].trim()+"','"+rmd_id+"','"+rmd_date+"'" +
                                    ",'"+rmd_no+"','"+rmd_charge+"','"+r_bundle+"','"+r_qty+"','"+matcode+"','"+loc.getCloc()+"','"+rmd_period+"'" +
                                    ",'"+rmd_spec+"','"+rmd_size+"','"+rmd_grade+"','"+rmd_length+"','"+rmd_weight+"','"+rmd_qa_grade+"'" +
                                    ",'"+rmd_remark+"','"+rmd_plant+"','"+rmd_station+"','"+user+"_"+ver+"',current_timestamp)";

                            PreparedStatement preparedStatement = con.prepareStatement(insrt);
                            preparedStatement.executeUpdate();
                            isSuccess=true ;
                            z = "บันทึกเรียบร้อยแล้ว";

                        }else{
                            isSuccess = false;
                            //onErrorDialog(raise.geteDup(),raise.getNotFound(),raise.geteMis(),raise.geteNmat(),raise.geteNlocS());
                        }
                    }
                }

            } catch (Exception ex) {
                isSuccess = false;
               // z = ex.getMessage().toString();
                itxt = ex.getMessage().toString();
                }

            return z ;
        }
    }

    public class FillList extends AsyncTask<String, String, String> {

        String z = "";
        Boolean head = false ;

        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
                String stext  = h_vbeln+"-"+h_posnr;
                int start = stext.trim().indexOf("-")+1;
                int end = stext.trim().length();

                SpannableString svbeln = new SpannableString(stext.trim());
                //SpannableStringBuilder  bvblen = new SpannableStringBuilder(stext);
                svbeln.setSpan(frcRed,start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                svbeln.setSpan(frcBlack,start-1,start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                txt_vbeln.setText(svbeln);
                txt_ar_name.setText(h_ar_name);
                txt_arktx.setText(h_arktx);
                //txt_wadat.setText(h_wadat);
                txt_carlicense.setText(h_carlicense);


            String[] from = {"ids","charge","qty","rmd_weight","rmd_qa_grade","rmd_remark","location" };
            final int[] views = {R.id.id,R.id.charge,R.id.qty,R.id.weight,R.id.qa_grade,R.id.remark,R.id.location};
            final SimpleAdapter ADA = new SimpleAdapter(ReceiveTransf.this,
                    itemlist, R.layout.adp_listscan, from,
                    views) {
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView tvRemark = (TextView) view.findViewById(R.id.remark);
                   if(itemlist.get(position).get("rmd_remark")==null || itemlist.get(position).get("rmd_remark").trim().equals("") || itemlist.get(position).get("rmd_remark").trim().equals("null")){
                       tvRemark.setVisibility(View.GONE);

                    }
                    return view;
                }
            };


            lvitem.setAdapter(ADA);
            lvitem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(arg2);
                    String t_id = (String) obj.get("id");
                    String t_charge = (String) obj.get("charge");
                    String t_bundle = (String) obj.get("bundle");

                    tab_hn = t_charge;
                    tab_id = t_id;
                    tab_bun = t_bundle;

                    arg1.setSelected(true);

                }
            });

            lvitem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(arg2);
                    String qhn = obj.get("charge")+"-"+obj.get("bundle");
                    double qqty = Integer.parseInt((String) obj.get("qty"));
                    double qweight = Integer.parseInt((String) obj.get("weight"));

                    /*tab_id = (String) obj.get("id");
                    String qdf = (String) obj.get("defect");


                    arg1.setSelected(true);

                    adjQty(qhn,qqty,qweight,qdf);*/

                    return true;
                }
            });

            txt_sumcount.setText(""+nf.format(sumcount)+" มัด");
            txt_sumqty.setText(""+nf.format(sumqty)+" เส้น");
            txt_sumweight.setText(""+nf.format(sumweight)+" kg.");

            //Toast.makeText(ReceiveTransf.this, start+"-"+end, Toast.LENGTH_SHORT).show();

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

                    if (!params[0].equals(null)) {

                        where = "where vbeln = '" + params[0] + "' and posnr = '" + params[1] + "' ";
                    }

                    String headqry = "select convert(nvarchar(20),cast(wadat as datetime),103) as wadat2 ,* from vw_wsum " + where;
                    PreparedStatement hps = con.prepareStatement(headqry);
                    ResultSet hrs = hps.executeQuery();

                    while (hrs.next()) {
                        h_vbeln = hrs.getString("vbeln");
                        h_posnr = hrs.getString("posnr");
                        h_kunnr = hrs.getString("KUNNR");
                        h_ar_name = hrs.getString("AR_NAME");
                        h_carlicense = hrs.getString("CARLICENSE");
                        h_matnr = hrs.getString("MATNR");
                        h_arktx = hrs.getString("ARKTX");

                    }
                    String itemquery = "SELECT id,charge,bundle,qty,rmd_weight,rmd_qa_grade,rmd_remark,location from tbl_receive " + where;
                    PreparedStatement itemq = con.prepareStatement(itemquery);
                    ResultSet irs = itemq.executeQuery();
                    int ids = 0 ;
                    itemlist.clear();

                    while (irs.next()) {
                        Map<String, String> datascan = new HashMap<String, String>();
                        ids++;
                        datascan.put("ids",String.valueOf(ids));
                        datascan.put("id",irs.getString("id"));
                        datascan.put("charge",irs.getString("charge")+"-"+irs.getString("bundle"));
                        datascan.put("bundle",irs.getString("bundle"));
                        datascan.put("qty",irs.getString("qty"));
                        datascan.put("rmd_weight",irs.getString("rmd_weight"));
                        datascan.put("rmd_qa_grade",irs.getString("rmd_qa_grade"));
                        datascan.put("rmd_remark",irs.getString("rmd_remark"));
                        datascan.put("location",irs.getString("location"));
                        itemlist.add(datascan);
                    }

                    String sumq = "SELECT count(item_barcode) AS sc ,sum(qty) as sq ,sum(rmd_weight) AS sw  FROM tbl_receive  "+where ;
                    PreparedStatement qps = con.prepareStatement(sumq);
                    ResultSet qrs = qps.executeQuery();

                    while (qrs.next()) {
                        sumcount = qrs.getInt("sc");
                        sumqty = qrs.getInt("sq");
                        sumweight = qrs.getInt("sw");
                    }


                }


            } catch (Exception ex) {

                z = ex.getMessage().toString();

            }

            return z;
        }
    }

    public class DeleteItem extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(ReceiveTransf.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {

                FillList fillList = new FillList();
                fillList.execute((g_vbeln.trim().substring(0,g_vbeln.trim().indexOf("-"))),g_posnr.trim()); //Handle filter string
                // Toast.makeText(MainActivity.this, filterp1+"\n"+filterp2, Toast.LENGTH_SHORT).show();

                tab_hn = null;
                tab_id = null;
                tab_bun = null;
            }
            tab_hn = null;
            tab_id = null;
            tab_bun = null;

        }
        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String query = "delete tbl_receive WHERE item_id = "+tab_id+"  ";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();
                    z = "ลบสำเร็จ";
                    isSuccess = true;
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = ex.getMessage().toString();
            }
            return z;
        }
    }


    public void onErrorDialog(int eDup , int notFound, int misMat, int nomat , int noLoc) {
        String msg = "";
        String doc = dVbeln;
        if(eDup==0 && notFound ==0 && misMat ==0 && nomat==0 && noLoc==0){
            msg = itxt+"\n"+"Code "+scanresult;
        }
        if(noLoc == 1) {
            msg = "เลือกเสาก่อนสแกน !";
        }else {
            if (notFound == 1) {
                msg = "ไม่พบข้อมูลที่สแกน !"+ "\n\n" + "Code " + scanresult;
            } else {
                if (eDup == 1) {
                    if (dVbeln == null || dVbeln.equals("")) {
                        doc = dPonum;
                    }
                    msg = "ซ้ำ HN " + dCharge + "-" + dBundle + " ช่อง " + dLocation + "\n" + "สินค้า : " + dSize + "\nเอกสาร : " + doc + " " + "\nโดย " + dUser.substring(0, dUser.length()-4) + "\nวันที่ " + dStamp.substring(0, 16) + "\n\n" + "Code " + scanresult;
                } else if(misMat==1){
                    msg = "ชนิดสินค้าไม่ตรงกับเอกสาร \n"+"เอกสาร "+h_matnr+"\n"+"ยิงได้ "+matcode;
                }
                else if(nomat==1){
                    msg = "ไม่พบข้อมูล Material Code"+ "\n\n" + "Code " + scanresult;
                }
            }
        }

        new AlertDialog.Builder(context)

                .setTitle("ผิดพลาด")
                .setMessage(msg)
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                            if(getErNl()==1){
                                onLocationClick(vxl);
                            }
                    }
                })
               /* .setNegativeButton("ปิด", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })  */

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }



    public  void  onLocationClick(View v){


        final Dialog dialog = new Dialog(ReceiveTransf.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_location_picker);
        dialog.setCancelable(true);

        final TextView tx_fr,tx_rl;

        tx_ch = (TextView)dialog.findViewById(R.id.d_ch);
        tx_fr = (TextView)dialog.findViewById(R.id.d_fr);
        tx_rl = (TextView)dialog.findViewById(R.id.d_rl);
        tx_pil = (TextView)dialog.findViewById(R.id.d_pil);
        txtRsLoc = (TextView)dialog.findViewById(R.id.txtRsLoc);
        btnLocSv = (Button)dialog.findViewById(R.id.btnLocSv);

        if(loc.getCloc().equals("")){

        }else{
            locationChecker(loc.getFr(),loc.getLr(),loc.getCh(),loc.getPill());
            checkCH(loc.getCh());
            checkPIL(loc.getPill());
            tx_ch.setText(loc.getCh());
            tx_pil.setText(loc.getPill());
            if(loc.getLr().equals("R")){
                tx_rl.setText("ขวา");
            }else{
                tx_rl.setText("ซ้าย");
            }
            if(loc.getFr().equals("F")){
                tx_fr.setText("หน้า");
            }else{
                tx_fr.setText("หลัง");
            }
            txtRsLoc.setText(loc.getCloc());
        }

        btnLocSv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                txt_loc.setText(loc.getCloc());
                dialog.dismiss();

            }
        });

        tx_ch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
               onChClick(view);

            }
        });
        tx_pil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPilClick(view);
                
            }
        });
        tx_fr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tx_fr.getText().toString().equals("หน้า")){
                    tx_fr.setText("หลัง");
                    loc.setFr("R");
                    locationChecker(loc.getFr(),loc.getLr(),loc.getCh(),loc.getPill());
                }else{
                    tx_fr.setText("หน้า");
                    loc.setFr("F");
                    locationChecker(loc.getFr(),loc.getLr(),loc.getCh(),loc.getPill());
                }
            }
    });
        tx_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tx_rl.getText().toString().equals("ซ้าย")){
                    tx_rl.setText("ขวา");
                    loc.setLr("R");
                    locationChecker(loc.getFr(),loc.getLr(),loc.getCh(),loc.getPill());
                }else{
                    tx_rl.setText("ซ้าย");
                    loc.setLr("L");
                    locationChecker(loc.getFr(),loc.getLr(),loc.getCh(),loc.getPill());
                }
            }
        });


        dialog.show();


    }

    public void onChClick(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(cw);
        builder.setTitle("เลือกช่อง");
        String[] animals = {"ช่อง 1", "ช่อง 3", "ช่อง 4", "ช่อง 5","่ช่อง 6","ช่อง 7","ช่อง 8","ช่อง 9","ช่อง 10"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {

                    case 0: ch = "1";
                        break;
                    case 1: ch = "3";
                        break;
                    case 2: ch = "4";
                        break;
                    case 3: ch = "5";
                        break;
                    case 4: ch = "6";
                        break;
                    case 5: ch = "7";
                        break;
                    case 6: ch = "8";
                        break;
                    case 7: ch = "9";
                        break;
                    case 8: ch = "10";
                        break;

                }
                loc.setCh(ch);
                tx_ch.setText(loc.getCh());
                checkCH(loc.getCh());
                locationChecker(loc.getFr(),loc.getLr(),loc.getCh(),loc.getPill());
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public  void  onPilClick(View v){

        final Dialog dialog = new Dialog(ReceiveTransf.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pil_picker);
        dialog.setCancelable(true);

        final EditText edtPil = (EditText)dialog.findViewById(R.id.edtPil);
        Button btnSv = (Button)dialog.findViewById(R.id.btnSv);

        btnSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pil = edtPil.getText().toString();
                /*if(pil.equals("")||pil==null){
                    pil = "0";
                }*/
                loc.setPill(pil);
                tx_pil.setText(loc.getPill());
                checkPIL(loc.getPill());
                locationChecker(loc.getFr(),loc.getLr(),loc.getCh(),loc.getPill());
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private  void  checkCH(String ch){
        if(ch.length()<1){
            tx_ch.setTextColor(Color.parseColor("#FA032C"));
            tx_ch.setBackground(getResources().getDrawable(R.drawable.border_red));

        }
        else{
            tx_ch.setTextColor(Color.parseColor("#00B94C"));
            tx_ch.setBackground(getResources().getDrawable(R.drawable.border_green));
        }

    }
    private  void  checkPIL(String pil){
        if(pil.length()<1){
            tx_pil.setTextColor(Color.parseColor("#FA032C"));
            tx_pil.setBackground(getResources().getDrawable(R.drawable.border_red));
        }
        else{
            tx_pil.setTextColor(Color.parseColor("#00B94C"));
            tx_pil.setBackground(getResources().getDrawable(R.drawable.border_green));

        }

    }


    public void locationChecker(String pfr,String prl,String pch,String ppil){
        Boolean isFound = false;
        loc.setCloc(pch+""+pfr+""+prl+"-"+ppil.trim());
        isFound = loc.checkLocation(loc.getCloc());
        if(isFound==true){
            txtRsLoc.setTextColor(Color.parseColor("#00B94C"));
            txtRsLoc.setBackgroundColor(Color.parseColor("#D0FFDC"));
            btnLocSv.setVisibility(View.VISIBLE);
        }else{
            txtRsLoc.setTextColor(Color.parseColor("#FA032C"));
            txtRsLoc.setBackgroundColor(Color.parseColor("#ffcfcc"));
            btnLocSv.setVisibility(View.GONE);
        }
        this.txtRsLoc.setText(loc.getCloc());

    }

    public void onVbelnClick(View v){

        final Dialog dialog = new Dialog(ReceiveTransf.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_vbeln);

        dialog.setCancelable(true);
        Button pick = (Button)dialog.findViewById(R.id.btnPick);

        pick.setOnClickListener(new View.OnClickListener() {
            EditText doEdt = (EditText) dialog.findViewById(R.id.doEdt);
            public void onClick(View v) {
                txt_vbeln.setText(doEdt.getText().toString());
                // paramcar = doEdt.getText().toString();
                dialog.dismiss();
            }
        });
        lvvbeln = (ListView) dialog.findViewById(R.id.lvcar);
        String[] from = {"loc"};
        int[] views = {R.id.r1 };
        final SimpleAdapter ADA = new SimpleAdapter(ReceiveTransf.this,
                vbelnlist, R.layout.adp_list_vbeln, from,
                views){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position,convertView,parent);
                if(position %2 == 1)
                {
                    view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                else
                {
                    view.setBackgroundColor(Color.parseColor("#F6F6F6"));
                }
                return view;
            }
        };

        lvvbeln.setAdapter(ADA);
        lvvbeln.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                        .getItem(arg2);
                String scar = (String) obj.get("loc");
                String strb = (String) obj.get("Storage_Bin");

                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(2000);
                arg1.startAnimation(animation1);

              /*  FillList fillList = new FillList();
                fillList.execute(storagebin);
*/
                dialog.dismiss();

            }

        });

        dialog.show();
    }


}
