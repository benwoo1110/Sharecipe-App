package sg.edu.np.mad.Sharecipe.ui.create;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.RecipeIngredient;

public class IngredientsViewholder extends RecyclerView.ViewHolder {
    EditText name;
    EditText quantity;
    EditText unit;

    public IngredientsViewholder(View itemView, ArrayList<RecipeIngredient> ingredientList) {
        super(itemView);

        name = itemView.findViewById(R.id.inputIngredientName);
        quantity = itemView.findViewById(R.id.inputIngredientQuantity);
        unit = itemView.findViewById(R.id.inputIngredientUnit);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ingredientList.get(getAdapterPosition()).setName(name.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int num = 1;
                try {
                    num = Integer.parseInt(String.valueOf(quantity.getText()));}
                catch (NumberFormatException e) {
                        ingredientList.get(getAdapterPosition()).setQuantity(num);
                    }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        unit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ingredientList.get(getAdapterPosition()).setUnit(unit.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
