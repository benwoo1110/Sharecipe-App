package sg.edu.np.mad.Sharecipe.ui.create.ingredients;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.RecipeIngredient;
import sg.edu.np.mad.Sharecipe.utils.FormatUtils;

public class IngredientsViewHolder extends RecyclerView.ViewHolder {
    EditText name;
    EditText quantity;
    EditText unit;
    TextView number;
    RecipeIngredient ingredient;

    public IngredientsViewHolder(View itemView, ArrayList<RecipeIngredient> ingredientList) {
        super(itemView);

        name = itemView.findViewById(R.id.inputIngredientName);
        quantity = itemView.findViewById(R.id.inputIngredientQuantity);
        unit = itemView.findViewById(R.id.inputIngredientUnit);
        number = itemView.findViewById(R.id.createdIngredientNumber);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ingredientList.get(getAdapterPosition()).setName(s.toString());
            }
        });

        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int quantityNum = FormatUtils.convertToInt(s.toString()).orElse(1);
                ingredientList.get(getAdapterPosition()).setQuantity(quantityNum);
            }
        });

        unit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ingredientList.get(getAdapterPosition()).setUnit(s.toString());
            }
        });
    }
}
