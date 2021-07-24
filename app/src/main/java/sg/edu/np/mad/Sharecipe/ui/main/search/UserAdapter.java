package sg.edu.np.mad.Sharecipe.ui.main.search;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.User;
import sg.edu.np.mad.Sharecipe.ui.App;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private final List<User> userList;

    public UserAdapter() {
        this.userList = new ArrayList<>();
    }

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_user, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userId = user.getUserId();
        holder.name.setText(user.getUsername());
        holder.bio.setText(user.getBio());
        if (user.getProfileImageId() != null) {
            App.getUserManager().getProfileImage(user).onSuccess(bitmap -> {
                new Handler(Looper.getMainLooper()).post(() -> holder.profileImage.setImageBitmap(bitmap));
            });
        } else {
            holder.profileImage.setImageResource(R.drawable.ic_baseline_person_24);
        }
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
