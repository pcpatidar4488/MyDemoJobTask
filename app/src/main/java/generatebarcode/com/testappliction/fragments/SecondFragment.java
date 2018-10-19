package generatebarcode.com.testappliction.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import generatebarcode.com.testappliction.R;

/**
 * Created by punamchand on 18-Oct-18.
 */

public class SecondFragment extends Fragment {

    View view;
    CheckBox mCheckbox;
    PhotoView mImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first, container, false);
        mCheckbox = view.findViewById(R.id.checkbox);
        mImage = view.findViewById(R.id.image);
        mCheckbox.bringToFront();
        mCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Check Box", Toast.LENGTH_LONG).show();
            }
        });
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Image", Toast.LENGTH_LONG).show();
            }
        });

        mImage.setImageResource(R.drawable.image_splash);

        return view;
    }
}
