package sg.edu.np.mad.Sharecipe.ui.create.infomation;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.Sharecipe.models.RecipeTag;

public class TagNamesAdapter extends ArrayAdapter<String> {

    public static List<String> convertToTagNames(List<RecipeTag> tags) {
        List<String> tagNames = new ArrayList<>();
        for (RecipeTag tag : tags) {
            tagNames.add(tag.getName());
        }
        return tagNames;
    }

    private final Filter mFilter;
    private final List<String> mOriginalValues;
    private final EditText mEditText;

    public TagNamesAdapter(@NonNull Context context,
                           int resource,
                           EditText editText,
                           @NonNull List<String> tagNames) {

        super(context, resource, tagNames);
        mFilter = new MyFilter();
        mOriginalValues = new ArrayList<>(tagNames);
        mEditText = editText;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class MyFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            final FilterResults results = new FilterResults();

            List<String> values = new ArrayList<>(mOriginalValues);

            // Remove already existing
            String[] presentEntries = mEditText.getText().toString().split(", ");
            for (String entry : presentEntries) {
                values.remove(entry);
            }

            if (prefix == null || prefix.length() <= 0) {
                results.values = values;
                results.count = values.size();
                return results;
            }

            final String prefixString = prefix.toString().toLowerCase();

            final int count = values.size();
            final ArrayList<String> newValues = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                final String value = values.get(i);
                final String valueText = value.toLowerCase();

                // First match against the whole, non-splitted value
                if (valueText.startsWith(prefixString)) {
                    newValues.add(value);
                } else {
                    final String[] words = valueText.split(" ");
                    for (String word : words) {
                        if (word.startsWith(prefixString)) {
                            newValues.add(value);
                            break;
                        }
                    }
                }
            }

            results.values = newValues;
            results.count = newValues.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            TagNamesAdapter.this.clear();
            //noinspection unchecked
            TagNamesAdapter.this.addAll((List<String>) results.values);
        }
    }
}
