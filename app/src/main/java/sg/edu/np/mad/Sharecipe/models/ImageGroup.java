package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

import java.util.List;

public class ImageGroup {

    @Expose
    private List<ImageRef> imageIds;

    public List<ImageRef> getImageIds() {
        return imageIds;
    }

    public void setImageIds(List<ImageRef> imageIds) {
        this.imageIds = imageIds;
    }
}
