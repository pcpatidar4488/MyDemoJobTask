package generatebarcode.com.testappliction.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import generatebarcode.com.testappliction.R;
import generatebarcode.com.testappliction.utils.Cv;

/**
 * Created by punamchand on 18-Oct-18.
 */
public class MainActivity extends AppCompatActivity {
    @Bind(R.id.button1)
    Button button1;
    @Bind(R.id.button2)
    Button button2;
    public static Boolean checkBox = false;
    public static Boolean checkBox1 = false;
    boolean isExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActionbar("Task");
        ButterKnife.bind(this);

        // int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        checkPermissions();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(MainActivity.this, "Button 1 click", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, FragmentZoomableActivity.class);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(MainActivity.this, "Button 2 click", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, DownloadPDF.class);
                startActivity(intent);
            }
        });

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
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(true);
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE,
                            Manifest.permission.INTERNET,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_NETWORK_STATE,

                    },
                    Cv.PERMISSIONS_BUZZ_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case Cv.PERMISSIONS_BUZZ_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /*   checkPermissions();
                    Toast.makeText(this, "if", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "else", Toast.LENGTH_SHORT).show();*/
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isExit) {
            super.onBackPressed();
        }
        isExit = true;
        Toast.makeText(this, "Press back again to Exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isExit = false;
            }
        }, 2000);
    }
}
