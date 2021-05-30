package sg.edu.np.mad.Sharecipe.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.User;

public class UserSearchResultAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private List<User> userList;

    public UserSearchResultAdapter() {
        this.userList = new ArrayList<>();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_user, parent, false);
        UserViewHolder viewHolder = new UserViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
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
