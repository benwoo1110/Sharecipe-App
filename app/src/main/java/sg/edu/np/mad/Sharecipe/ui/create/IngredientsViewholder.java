package sg.edu.np.mad.Sharecipe.ui.create;

import android.view.View;
import android.widget.Spinner;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

public class IngredientsViewholder extends RecyclerView.ViewHolder {
    TextInputEditText name;
    TextInputEditText quantity;
    Spinner unit;

    public IngredientsViewholder(View itemView) {
        super(itemView);
        // TODO: Stuff

    }

}
