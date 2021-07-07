package sg.edu.np.mad.Sharecipe.ui.create;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.Sharecipe.R;

public class IngredientsViewholder extends RecyclerView.ViewHolder {
    EditText name;
    EditText quantity;
    EditText unit;

    public IngredientsViewholder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.inputIngredientName);
        quantity = itemView.findViewById(R.id.inputIngredientQuantity);
        unit = itemView.findViewById(R.id.inputIngredientUnit);
    }

}
