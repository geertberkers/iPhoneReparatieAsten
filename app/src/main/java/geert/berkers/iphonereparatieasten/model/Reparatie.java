package geert.berkers.iphonereparatieasten.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Geert.
 */

public class Reparatie implements Parcelable {

    private final double price;
    private final String reparatie;

    public Reparatie(String reparatie, double price){
        this.price = price;
        this.reparatie = reparatie;
    }

    public double getPrice() {
        return price;
    }

    public String getReparatie() {
        return reparatie;
    }

    private Reparatie(Parcel read) {
        this.reparatie = read.readString();
        this.price = read.readDouble();
    }

    public static final Parcelable.Creator<Reparatie> CREATOR = new Parcelable.Creator<Reparatie>() {
        @Override
        public Reparatie createFromParcel(Parcel source) {
            return new Reparatie(source);
        }

        @Override
        public Reparatie[] newArray(int size) {
            return new Reparatie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel arg0, int arg1) {
        arg0.writeString(reparatie);
        arg0.writeDouble(price);
    }
}
