package sg.edu.np.mad.Sharecipe.ui.main.search;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.User;
import sg.edu.np.mad.Sharecipe.ui.common.SectionCreator;
import sg.edu.np.mad.Sharecipe.ui.common.SectionViewHolder;
import sg.edu.np.mad.Sharecipe.ui.main.user.UserAdapter;

public class UserResultSectionCreator implements SectionCreator {

    private final String headerText;
    private final List<User> users;

    public UserResultSectionCreator(String headerText) {
        this.headerText = headerText;
        this.users = new ArrayList<>();
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_holder_section;
    }

    @Override
    public @NonNull SectionViewHolder createViewHolder(View view) {
        return new UserResultSectionViewHolder(view);
    }

    public void setUserList(List<User> userList) {
        this.users.clear();
        this.users.addAll(userList);
    }

    class UserResultSectionViewHolder extends SectionViewHolder {

        private final TextView header;
        private final RecyclerView sectionRecyclerView;

        public UserResultSectionViewHolder(@NonNull View itemView) {
            super(itemView);

            header = itemView.findViewById(R.id.discoverSectionTitle);
            sectionRecyclerView = itemView.findViewById(R.id.discoverSectionRecyclerView);

            UserAdapter adapter = new UserAdapter(users);
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());

            sectionRecyclerView.setAdapter(adapter);
            sectionRecyclerView.setLayoutManager(layoutManager);
        }

        @Override
        public void onBind(int position) {
            header.setText(headerText);
            sectionRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }
}
