package sg.edu.np.mad.Sharecipe.ui.create;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.Recipe;


public class InformationFragment extends Fragment {

    private ImagesAdapter adapter;
    private final ArrayList<Uri> imageList = new ArrayList<>();

    public InformationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);

        RecyclerView images = view.findViewById(R.id.recyclerview_images);
        ImageView enlargedImage = view.findViewById(R.id.expanded_image);

        adapter = new ImagesAdapter(getActivity(), imageList, enlargedImage, view);
        LinearLayoutManager cLayoutManager = new LinearLayoutManager(getActivity());
        cLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        images.setAdapter(adapter);
        images.setLayoutManager(cLayoutManager);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            imageList.add(uri);
            adapter.notifyDataSetChanged();
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}
