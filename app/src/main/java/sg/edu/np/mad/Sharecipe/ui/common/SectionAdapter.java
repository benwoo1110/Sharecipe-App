package sg.edu.np.mad.Sharecipe.ui.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionViewHolder> {

    private final List<SectionCreator> sectionList;

    public SectionAdapter() {
        this.sectionList = new ArrayList<>();
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("New section");
        SectionCreator section = sectionList.get(viewType);
        View view = LayoutInflater.from(parent.getContext()).inflate(section.getLayoutId(), parent, false);
        return section.createViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        System.out.println("On bind");
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void addSection(SectionCreator sectionCreator) {
        this.sectionList.add(sectionCreator);
    }
}
