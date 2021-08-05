package sg.edu.np.mad.Sharecipe.ui.common.section;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class SectionViewHolder extends RecyclerView.ViewHolder {
    
    public SectionViewHolder(@NonNull View itemView) {
        super(itemView);
    }
    
    public abstract void onBind(int position);
}
