package com.zubb.jannarongj.zzubbreceive;

import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by jannarong.j on 10/25/2018.
 */

public class Raiserror {

    private Boolean eNmat ;
    private Boolean eDup ;
    private Boolean notFound ;
    private Boolean eNlocS ;
    private Boolean eMis ;

    public Boolean getNotFound() {
        return notFound;
    }

    public void setNotFound(Boolean notFound) {
        this.notFound = notFound;
    }


    String serial = Build.SERIAL;

    public Boolean geteDup() {
        return eDup;
    }

    public void seteDup(Boolean eDup) {
        this.eDup = eDup;
    }


    public Boolean geteNmat() {
        return eNmat;
    }

    public void seteNmat(Boolean eNmat) {
        this.eNmat = eNmat;
    }

    public Boolean geteNlocS() {
        return eNlocS;
    }

    public void seteNlocS(Boolean eNlocS) {
        this.eNlocS = eNlocS;
    }


    public Boolean geteMis() {
        return eMis;
    }

    public void seteMis(Boolean eMis) {
        this.eMis = eMis;
    }


    ConnectionClass connectionClass;




    public class ErrorLog extends AsyncTask<String, String, String> {

        String z = "";

        @Override
        protected void onPreExecute() {

        }
        @Override
        protected void onPostExecute(String r) {

            // Toast.makeText(MainActivity.this, z, Toast.LENGTH_SHORT).show();

        }

        @Override
        protected String doInBackground(String... params) {
            connectionClass = new ConnectionClass();
            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "พบปัญหาการเชื่อมต่อ";
                } else {
                    String barCheck = "" ;

                    String checkEx = "Select bar_id from vw_barcode_item where bar_id = '" + params[0].trim() + "' ";
                    PreparedStatement getCheck = con.prepareStatement(checkEx);
                    ResultSet cks = getCheck.executeQuery();
                    while (cks.next()) {
                        barCheck = cks.getString("bar_id");
                    }

                    if(barCheck==null){
                        //TODO ERROR ITEM DOES NOT EXISTS AWW !
                    }else{
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



                        }


                    }
                }

            } catch (Exception ex) {

                z = ex.getMessage().toString();

            }

            return z ;
        }
    }

}

