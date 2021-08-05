package sg.edu.np.mad.Sharecipe.ui.main.search;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.ui.common.section.SectionCreator;
import sg.edu.np.mad.Sharecipe.ui.common.section.SectionViewHolder;
import sg.edu.np.mad.Sharecipe.ui.main.recipe.RecipeAdapter;

public class RecipeResultSectionCreator implements SectionCreator {

    private final String headerText;
    private final List<Recipe> recipes;

    public RecipeResultSectionCreator(String headerText) {
        this.headerText = headerText;
        this.recipes = new ArrayList<>();
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_holder_section;
    }

    @Override
    public @NonNull SectionViewHolder createViewHolder(View view) {
        return new RecipeResultSectionViewHolder(view);
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipes.clear();
        this.recipes.addAll(recipeList);
    }

    class RecipeResultSectionViewHolder extends SectionViewHolder {

        private final TextView header;
        private final RecyclerView sectionRecyclerView;

        public RecipeResultSectionViewHolder(@NonNull View itemView) {
            super(itemView);

            header = itemView.findViewById(R.id.discoverSectionTitle);
            sectionRecyclerView = itemView.findViewById(R.id.discoverSectionRecyclerView);

            RecipeAdapter adapter = new RecipeAdapter(recipes);
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());

            sectionRecyclerView.setAdapter(adapter);
            sectionRecyclerView.setLayoutManager(layoutManager);
        }

        @Override
        public void onBind(int position) {
            header.setText(headerText);
            sectionRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }
}
