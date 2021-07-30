package sg.edu.np.mad.Sharecipe.ui.main.profile;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import sg.edu.np.mad.Sharecipe.R;

public class StatsViewHolder extends RecyclerView.ViewHolder {

    MaterialCardView card;

    public StatsViewHolder(@NonNull View itemView) {
        super(itemView);

        card = itemView.findViewById(R.id.statsCard);

        int displayWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, Resources.getSystem().getDisplayMetrics());
        int cardWidth = (displayWidth - dimensionInDp) / 2;
        card.getLayoutParams().width = cardWidth;
    }
}
