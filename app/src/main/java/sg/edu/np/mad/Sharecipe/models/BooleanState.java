package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

public class BooleanState {

    @Expose
    boolean state;

    public boolean getState() {
        return state;
    }

    @Override
    public String toString() {
        return "BooleanState{" +
                "state=" + state +
                '}';
    }
}
