package sg.edu.np.mad.Sharecipe.ui.main.discover;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.ui.common.SectionCreator;
import sg.edu.np.mad.Sharecipe.ui.common.SectionViewHolder;

public class LargeSectionCreator implements SectionCreator {

    private String headerText;

    public LargeSectionCreator(String headerText) {
        this.headerText = headerText;
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_holder_discover_section;
    }

    @Override
    public SectionViewHolder createViewHolder(View view) {
        return new LargeSectionViewHolder(view);
    }

    class LargeSectionViewHolder extends SectionViewHolder {

        private final TextView header;
        private final RecyclerView discoverSectionRecyclerView;

        public LargeSectionViewHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.discoverSectionTitle);
            discoverSectionRecyclerView = itemView.findViewById(R.id.discoverSectionRecyclerView);

            LargeCardAdapter adapter = new LargeCardAdapter();
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            discoverSectionRecyclerView.setAdapter(adapter);
            discoverSectionRecyclerView.setLayoutManager(layoutManager);
            new LinearSnapHelper().attachToRecyclerView(discoverSectionRecyclerView);
        }

        @Override
        public void onBind(int position) {
            System.out.println("Binding");
            header.setText(headerText);
        }
    }
}
