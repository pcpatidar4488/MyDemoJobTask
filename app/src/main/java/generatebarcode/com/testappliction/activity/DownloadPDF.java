package generatebarcode.com.testappliction.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;
import generatebarcode.com.testappliction.R;

import static generatebarcode.com.testappliction.utils.Helpers.isNetworkAvailable;

/**
 * Created by punamchand on 18-Oct-18.
 */
public class DownloadPDF extends AppCompatActivity {
    String storeDir;
    Activity context;
    @Bind(R.id.textUrl)
    EditText editUrl;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.status)
    TextView status;
    @Bind(R.id.pathTxt)
    TextView pathTxt;
    @Bind(R.id.txt)
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_pdf);
        ButterKnife.bind(this);
        initActionbar("File Downloader");
        //  editUrl.setText("http://2.imimg.com/data2/LQ/QV/MY-/teddy-small-size-500x500.jpg");
        editUrl.setText("https://www.hdwallpapers.in/download/emma_watson_hd_quality-normal.jpg");
        context = this;
    }

    private static File getStorageDir() {
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
        }
        return docsFolder;
    }
    private static Boolean getStorageDirCheck(String dir) {
        Boolean bool = false;
        File docsFolder = new File(dir);
        if (docsFolder.exists()) {
            bool = true;
        }
        return bool;
    }

    public void downloadFile(View view) {
        String url = editUrl.getText().toString();
        if (!url.equals("")) {
            if (url.contains("http")) {
                String[] arr = url.split("/");
                storeDir = getStorageDir() + "/" + arr[arr.length - 1];
                if (isNetworkAvailable(DownloadPDF.this)) {
                    if (getStorageDirCheck(storeDir)){
                        openDialog(url);
                    }else {
                        status.setText("Wait...");
                        BackTask bt = new BackTask();
                        bt.execute(url);
                    }
                } else {
                    Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Please Check your internet connection...!!!", Snackbar.LENGTH_SHORT);
                    snackbar1.show();
                }
            } else {
                Toast.makeText(context, "Please enter http url", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "No input url", Toast.LENGTH_LONG).show();
        }
    }

    // background task to download file
    private class BackTask extends AsyncTask<String, Integer, Void> {
        NotificationManager mNotifyManager;
        NotificationCompat.Builder mBuilder;

        protected void onPreExecute() {
            super.onPreExecute();
            mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(context);
            mBuilder.setContentTitle("File Download")
                    .setContentText("Download in progress")
                    .setSmallIcon(R.drawable.ic_launcher_background);
            Toast.makeText(context, "Downloading the file.....", Toast.LENGTH_LONG).show();
        }

        protected Void doInBackground(String... params) {
            URL url;
            int count;
            try {
                url = new URL(params[0]);
                String pathl = "";
                try {
//                    File f=new File(storeDir);
                    // if(f.exists()){
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    InputStream is = con.getInputStream();
                    String pathr = url.getPath();
                    String filename = pathr.substring(pathr.lastIndexOf('/') + 1);
                    //   pathl=storeDir+"/"+filename;
                    FileOutputStream fos = new FileOutputStream(storeDir);
                    int lenghtOfFile = con.getContentLength();
                    byte data[] = new byte[1024];
                    long total = 0;
                    while ((count = is.read(data)) != -1) {
                        total += count;
                        // publishing the progress
                        publishProgress((int) ((total * 100) / lenghtOfFile));
                        // writing data to output file
                        fos.write(data, 0, count);
                    }

                    is.close();
                    fos.flush();
                    fos.close();
                   /* }
                    else{
                        Log.e("Error","Not found: "+storeDir);

                    }*/

                } catch (Exception e) {
                    e.printStackTrace();

                }

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;

        }

        protected void onProgressUpdate(Integer... progress) {

            mBuilder.setProgress(100, progress[0], false);
            // Displays the progress bar on notification
            mNotifyManager.notify(0, mBuilder.build());
            if (progress[0] == 100) {
                updateDownload("Download completed " + progress[0]);
            } else {
                updateDownload("Downloading " + progress[0]);
            }
        }

        private void updateDownload(String s) {
            status.setText(s);
        }

        protected void onPostExecute(Void result) {
            mBuilder.setContentText("Download complete");
            // Removes the progress bar
            Toast.makeText(context, "Download complete", Toast.LENGTH_SHORT).show();
            mBuilder.setProgress(0, 0, false);
            mNotifyManager.notify(0, mBuilder.build());
            txt.setVisibility(View.VISIBLE);
            pathTxt.setVisibility(View.VISIBLE);
            pathTxt.setText(storeDir);
        }
    }

    private void initActionbar(String title) {
        ActionBar actionBar = getSupportActionBar();
        View viewActionBar = getLayoutInflater().inflate(R.layout.action_bar_tittle_text_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#303F9F")));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(viewActionBar, params);
        TextView actionbarTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        actionbarTitle.setText(title);
        //  actionbarTitle.setTypeface(TypefaceCache.get(getAssets(), 3));
        actionbarTitle.setTextSize(16);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openDialog(final String url) {
        final Dialog dialog = new Dialog(context); // Context, this, etc.
        dialog.setContentView(R.layout.dialog_custom);
        dialog.setTitle("Already");
        TextView dialog_info = dialog.findViewById(R.id.dialog_info);
        Button dialog_cancel = dialog.findViewById(R.id.dialog_cancel);
        Button dialog_ok = dialog.findViewById(R.id.dialog_ok);
        dialog_info.setText("File Already Exits!\n\nDo you want to replace?");
        dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText("Wait...");
                BackTask bt = new BackTask();
                bt.execute(url);
                dialog.dismiss();
            }
        });
        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}

