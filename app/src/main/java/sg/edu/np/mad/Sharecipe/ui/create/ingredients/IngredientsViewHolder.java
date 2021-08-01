package sg.edu.np.mad.Sharecipe.ui.create.ingredients;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.RecipeIngredient;
import sg.edu.np.mad.Sharecipe.ui.common.AfterTextChangedWatcher;
import sg.edu.np.mad.Sharecipe.utils.FormatUtils;

public class IngredientsViewHolder extends RecyclerView.ViewHolder {
    TextInputEditText name;
    TextInputEditText quantity;
    TextInputEditText unit;
    TextView number;
    RecipeIngredient ingredient;

    public IngredientsViewHolder(View itemView, ArrayList<RecipeIngredient> ingredientList) {
        super(itemView);

        name = itemView.findViewById(R.id.inputIngredientName);
        quantity = itemView.findViewById(R.id.inputIngredientQuantity);
        unit = itemView.findViewById(R.id.inputIngredientUnit);
        number = itemView.findViewById(R.id.createdIngredientNumber);

        name.addTextChangedListener((AfterTextChangedWatcher) s -> ingredientList.get(getAdapterPosition())
                .setName(s.toString()));

        quantity.addTextChangedListener((AfterTextChangedWatcher) s -> ingredientList.get(getAdapterPosition())
                .setQuantity(FormatUtils.convertToDouble(s.toString()).orElse(1D)));

        unit.addTextChangedListener((AfterTextChangedWatcher) s -> ingredientList.get(getAdapterPosition())
                .setUnit(s.toString()));
    }
}
