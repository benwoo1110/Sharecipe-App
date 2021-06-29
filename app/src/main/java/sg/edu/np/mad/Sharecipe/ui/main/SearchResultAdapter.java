package sg.edu.np.mad.Sharecipe.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.User;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultViewHolder> {

    private final List<User> userList;

    public SearchResultAdapter() {
        this.userList = new ArrayList<>();
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_search_result, parent, false);
        return new SearchResultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        User user = userList.get(position);
        holder.name.setText(user.getUsername());
        holder.bio.setText(user.getBio());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> newUsers) {
        userList.clear();
        userList.addAll(newUsers);
    }
}
