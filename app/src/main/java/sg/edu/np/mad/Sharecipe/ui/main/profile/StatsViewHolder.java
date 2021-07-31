package sg.edu.np.mad.Sharecipe.ui.main.profile;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import sg.edu.np.mad.Sharecipe.R;

public class StatsViewHolder extends RecyclerView.ViewHolder {

    final MaterialCardView card;
    final TextView name;
    final TextView number;

    public StatsViewHolder(@NonNull View itemView) {
        super(itemView);

        card = itemView.findViewById(R.id.statsCard);
        name = itemView.findViewById(R.id.statsName);
        number = itemView.findViewById(R.id.statsNumber);

        // Ensure its centered properly
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        int displayWidth = displayMetrics.widthPixels;
        int spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, displayMetrics);
        int cardWidth = (displayWidth - spacing) / 2;
        card.getLayoutParams().width = cardWidth;
    }
}
