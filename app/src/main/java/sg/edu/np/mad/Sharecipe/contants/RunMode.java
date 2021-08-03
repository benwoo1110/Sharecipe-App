package sg.edu.np.mad.Sharecipe.contants;

import sg.edu.np.mad.Sharecipe.BuildConfig;

public class RunMode {
    public static final boolean IS_PRODUCTION = BuildConfig.BUILD_TYPE.equals("release");
}
