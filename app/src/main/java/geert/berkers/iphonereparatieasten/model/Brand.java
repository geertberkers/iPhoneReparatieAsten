package geert.berkers.iphonereparatieasten.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Geert.
 */

public class Brand implements Parcelable {

    private final int image;

    private final String brand;

    private final List<Model> models;
    private final List<String> deviceTypes;

    public Brand(String brand, int image){
        this.image = image;
        this.brand = brand;
        this.deviceTypes = new ArrayList<>();
        this.models = new ArrayList<>();
    }

    public String getBrand() {
        return brand;
    }

    public int getImage() {
        return image;
    }

    private Brand(Parcel read) {
        this.models = new ArrayList<>();
        this.image = read.readInt();
        this.brand = read.readString();
        this.deviceTypes = read.createStringArrayList();
        read.readTypedList(models, Model.CREATOR);
    }

    public static final Parcelable.Creator<Brand> CREATOR = new Parcelable.Creator<Brand>() {
        @Override
        public Brand createFromParcel(Parcel source) {
            return new Brand(source);
        }

        @Override
        public Brand[] newArray(int size) {
            return new Brand[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel arg0, int arg1) {
        arg0.writeInt(image);
        arg0.writeString(brand);
        arg0.writeStringList(deviceTypes);
        arg0.writeTypedList(models);
    }

    public List<String> getDeviceTypes() {
        return deviceTypes;
    }

    public void setDeviceType(String deviceType) {
        this.deviceTypes.add(deviceType);
    }

    public void addModel(Model model){
        models.add(model);
    }

    public List<Model> getModels() {
        return models;
    }
}
