package geert.berkers.iphonereparatieasten.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import geert.berkers.iphonereparatieasten.enums.DeviceType;

/**
 * Created by Geert.
 */

public class Model implements Parcelable{

    private final int modelImage;
    private final String modelName;
    private final DeviceType deviceType;
    private final List<Reparatie> reparaties;

    public Model(String modelName, int modelImage, DeviceType deviceType){
        this.modelName = modelName;
        this.modelImage = modelImage;
        this.deviceType = deviceType;
        this.reparaties = new ArrayList<>();
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public String getModelName() {
        return modelName;
    }

    public int getModelImage() {
        return modelImage;
    }

    @SuppressWarnings("unused")
    public void addReparatie(Reparatie reparatie){
        this.reparaties.add(reparatie);
    }

    public void addReparatieList(List<Reparatie> reparaties){
        this.reparaties.addAll(reparaties);
    }

    public List<Reparatie> getReparaties() {
        return reparaties;
    }

    private Model(Parcel read) {
        this.reparaties = new ArrayList<>();
        this.modelImage = read.readInt();
        this.modelName = read.readString();
        this.deviceType = (DeviceType) read.readSerializable();
        read.readTypedList(reparaties, Reparatie.CREATOR);
    }

    public static final Parcelable.Creator<Model> CREATOR = new Parcelable.Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel source) {
            return new Model(source);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel arg0, int arg1) {
        arg0.writeInt(modelImage);
        arg0.writeString(modelName);
        arg0.writeSerializable(deviceType);
        arg0.writeTypedList(reparaties);

    }
}
