package generatebarcode.com.testappliction.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.github.chrisbanes.photoview.PhotoView;
import java.util.HashMap;
import java.util.Map;
import generatebarcode.com.testappliction.R;
import generatebarcode.com.testappliction.activity.MainActivity;
import generatebarcode.com.testappliction.network.ApiCallServices;
import generatebarcode.com.testappliction.network.api_request.ApiRequest;
import generatebarcode.com.testappliction.utils.Cv;
import generatebarcode.com.testappliction.utils.Helpers;
import generatebarcode.com.testappliction.utils.ImagePicker;

import static android.app.Activity.RESULT_OK;

/**
 * Created by punamchand on 18-Oct-18.
 */

public class FirstFragment extends Fragment {
    View view;
    CheckBox mCheckbox;
    CheckBox mCheckbox1;
    PhotoView mImage;
    RelativeLayout checkbox_layout;
    RelativeLayout checkbox_layout1;
    Button mBrowseImage;
    Button mUploadImage;
    FrameLayout mFrameLayout;
    private Uri imageToUploadUri;
    public static Bitmap photo;
    public static String encodedString = "";
    public static ProgressDialog mProgressDialog;
   // static String imagePath = "/storage/sdcard0/Pictures/image.jpg";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_first, container, false);
        mCheckbox = view.findViewById(R.id.checkbox);
        mCheckbox1 = view.findViewById(R.id.checkbox1);
        mImage = view.findViewById(R.id.image);
        checkbox_layout = view.findViewById(R.id.checkbox_layout);
        checkbox_layout1 = view.findViewById(R.id.checkbox_layout1);
        mBrowseImage = view.findViewById(R.id.brows_image);
        mUploadImage = view.findViewById(R.id.upload_image);
        mFrameLayout = view.findViewById(R.id.frameLayout);
        mCheckbox.bringToFront();
        mCheckbox1.bringToFront();
        checkbox_layout.bringToFront();
        checkbox_layout1.bringToFront();
        if (MainActivity.checkBox) {
            mCheckbox.setChecked(true);
        } else {
            mCheckbox.setChecked(false);
        }
        if (MainActivity.checkBox1) {
            mCheckbox1.setChecked(true);
        } else {
            mCheckbox1.setChecked(false);
        }
        if (photo != null) {
            mImage.setImageBitmap(photo);
            mCheckbox.setVisibility(View.VISIBLE);
            mCheckbox1.setVisibility(View.VISIBLE);
        } else {
            mCheckbox.setVisibility(View.GONE);
            mCheckbox1.setVisibility(View.GONE);
           // mImage.setImageResource(R.drawable.image_splash);
        }

        mCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckbox.isChecked()) {
                    MainActivity.checkBox = true;
                } else {
                    MainActivity.checkBox = false;
                }
            }
        });

        mCheckbox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckbox1.isChecked()) {
                    MainActivity.checkBox1 = true;
                } else {
                    MainActivity.checkBox1 = false;
                }
            }
        });

        mBrowseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDialog();
            }
        });
        mUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!encodedString.equals("")){
                    if (Helpers.isNetworkAvailable(getActivity())){
                        mProgressDialog = new ProgressDialog(getActivity());
                        mProgressDialog.setMessage("Wait...");
                        mProgressDialog.setIndeterminate(false);
                        mProgressDialog.setCancelable(true);

                        ApiRequest apiRequest = new ApiRequest();
                        Map map = new HashMap();
                        map.put("image",encodedString);
                        apiRequest.setMap(map);
                        ApiCallServices.action(getActivity(),Cv.ACTION_IMAGE_SEND);
                        mProgressDialog.show();
                    }else {
                        Snackbar snackbar1 = Snackbar.make(mFrameLayout, "Please Check your internet connection...!!!", Snackbar.LENGTH_SHORT);
                    }

                }else {
                    Toast.makeText(getActivity(), "Please select Image", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    private void startDialog() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setNegativeButton("Gallary",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        getIntent.setType("image/*");
                        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        pickIntent.setType("image/*");
                        startActivityForResult(pickIntent, Cv.REQUEST_GALLERY);
                    }
                });
        myAlertDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            photo = null;
            switch (requestCode) {
                case Cv.REQUEST_GALLERY:
                    try {
                        imageToUploadUri = data.getData();
                        //imagePath = data.getData().getPath();
                        photo = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                        if (photo != null) {
                            mCheckbox.setVisibility(View.VISIBLE);
                            mCheckbox1.setVisibility(View.VISIBLE);
                            mImage.setVisibility(View.VISIBLE);
                            encodedString = Helpers.bitmapToBase64(photo);
                            mImage.setImageBitmap(photo);
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }
    }
}
