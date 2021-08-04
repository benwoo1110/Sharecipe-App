package sg.edu.np.mad.Sharecipe.ui.main.profile.stats;

import android.content.Context;

import androidx.annotation.NonNull;

import sg.edu.np.mad.Sharecipe.models.User;

public interface StatsAction {

    /**
     *
     * @return
     */
    @NonNull
    StatsType getType();

    /**
     *
     * @param context
     * @param user
     */
    void onClick(@NonNull Context context, @NonNull User user);
}
