package com.zubb.jannarongj.zzubbreceive;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CheckVersion extends AppCompatActivity {

    ProgressBar pbbar;ProgressDialog bar ;
    ConnectionClass connectionClass;
    Version vers ;
    String app_name,capp_ver,fapp_verion;
    TextView cur_app,last_app;
    Button btnUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_version);
        vers = new Version();
        connectionClass = new ConnectionClass();
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        cur_app = (TextView) findViewById(R.id.cur_app);
        last_app = (TextView) findViewById(R.id.last_app);
        btnUp = (Button) findViewById(R.id.btnUp);

        app_name = vers.name;
        capp_ver = vers.Version;

        FecthCheck fetchC = new FecthCheck();
        fetchC.execute("");

        btnUp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                getUpdate();
            }
        });

    }

    private  void getUpdate(){


        new DownloadNewVersion().execute();
        cur_app.setText(capp_ver);
        last_app.setText(fapp_verion);

    }

    public class FecthCheck extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {

            if(isSuccess==true) {
                if(capp_ver.trim().equals(fapp_verion.trim())){
                    Intent i = new Intent(CheckVersion.this, Login.class);
                    startActivity(i);
                    finish();
                }else{
                    getUpdate();
                }
            }else{

            }
            cur_app.setText(capp_ver);
            last_app.setText(fapp_verion);

            pbbar.setVisibility(View.GONE);

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {


                    String query = "SELECT  top 1 name,version FROM tbl_version_control  where name = '"+app_name+"'  order by id desc " ;
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    Log.d("LOG",query);


                    while (rs.next()) {
                        fapp_verion = rs.getString("version");
                    }

                    //       z = "Success";
                    isSuccess = true;
                }
            } catch (Exception ex) {
//                z = ex.getMessage().toString();
                isSuccess = false;
                //"Error retrieving data from table";
            }

            return z;
        }
    }

    class DownloadNewVersion extends AsyncTask<String,Integer,Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            bar = new ProgressDialog(CheckVersion.this);
            bar.setCancelable(false);

            bar.setMessage("Downloading...");

            bar.setIndeterminate(true);
            bar.setCanceledOnTouchOutside(false);
            bar.show();

        }

        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

            bar.setIndeterminate(false);
            bar.setMax(100);
            bar.setProgress(progress[0]);
            String msg = "";
            if(progress[0]>99){

                msg="Finishing... ";

            }else {

                msg="Downloading... "+progress[0]+"%";
            }
            bar.setMessage(msg);

        }
        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            bar.dismiss();

            if(result){

                Toast.makeText(getApplicationContext(),"Update Done",
                        Toast.LENGTH_SHORT).show();

            }else{

                Toast.makeText(getApplicationContext(),"Error: Try Again",
                        Toast.LENGTH_SHORT).show();

            }

        }


        @Override
        protected Boolean doInBackground(String... arg0) {
            Boolean flag = false;

            try {

                URL url = new URL("http://report.zubbsteel.com/version/version.php?n="+app_name);

                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                String PATH = Environment.getExternalStorageDirectory()+"/Download/";
                File file = new File(PATH);
                file.mkdirs();

                File outputFile = new File(file,app_name+".apk");

                if(outputFile.exists()){
                    outputFile.delete();
                }

                InputStream is = c.getInputStream();

                int total_size = 1431692;//size of apk

                byte[] buffer = new byte[1024];
                int len1 = 0;
                int per = 0;
                int downloaded=0;

                FileOutputStream fos = new FileOutputStream(outputFile);

                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                    downloaded +=len1;
                    per = (int) (downloaded * 100 / total_size);
                    publishProgress(per);
                }
                fos.close();
                is.close();

                OpenNewVersion(PATH);

                flag = true;
            } catch (Exception e) {
                //Log.e(TAG, "Update Error: " + e.getMessage());
                flag = false;
            }
            return flag;

        }

    }

    void OpenNewVersion(String location) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(location + app_name+".apk")),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

}
