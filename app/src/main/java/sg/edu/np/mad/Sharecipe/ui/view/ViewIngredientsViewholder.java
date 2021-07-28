package sg.edu.np.mad.Sharecipe.ui.view;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.Sharecipe.R;

public class ViewIngredientsViewholder extends RecyclerView.ViewHolder {

    TextView number;
    TextView name;
    TextView quantity;
    TextView unit;

    public ViewIngredientsViewholder(View itemView) {
        super(itemView);

        number = itemView.findViewById(R.id.displayIngredientNumber);
        name = itemView.findViewById(R.id.displayIngredientName);
        quantity = itemView.findViewById(R.id.displayIngredientQuantity);
        unit = itemView.findViewById(R.id.displayingredientUnit);
    }
}
