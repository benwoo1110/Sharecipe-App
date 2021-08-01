package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Objects;

public class ImageRef implements Serializable {

    @Expose
    private String fileId;

    public ImageRef(String fileId) {
        this.fileId = fileId;
    }

    private ImageRef() { }

    public String getFileId() {
        return fileId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageRef imageRef = (ImageRef) o;
        return Objects.equals(fileId, imageRef.fileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileId);
    }

    @Override
    public String toString() {
        return "ImageRef{" +
                "fileId='" + fileId + '\'' +
                '}';
    }
}
