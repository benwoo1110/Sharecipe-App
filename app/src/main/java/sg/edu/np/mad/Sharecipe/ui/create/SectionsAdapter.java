package sg.edu.np.mad.Sharecipe.ui.create;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;

public class SectionsAdapter extends RecyclerView.Adapter<SectionsViewholder> {
    ArrayList<RecyclerView> sections;

    public SectionsAdapter() {}

    public SectionsViewholder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_sections, parent, false);
        SectionsViewholder holder = new SectionsViewholder(item);

        return holder;
    }

    public void onBindViewHolder(SectionsViewholder holder, int position) {
        //TODO: stuff
    }

    public int getItemCount() {
        return sections.size();
    }
}
