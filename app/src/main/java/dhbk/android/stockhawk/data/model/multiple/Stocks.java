package dhbk.android.stockhawk.data.model.multiple;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class Stocks implements Parcelable {

    @SerializedName("query")
    Query mQuery = new Query();

    public Query getQuery() {
        return mQuery;
    }

    public void setQuery(Query query) {
        mQuery = query;
    }

    @Override
    public String toString() {
        return "Stocks{" +
                "mQuery=" + mQuery +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mQuery, flags);
    }

    public Stocks() {
    }

    protected Stocks(Parcel in) {
        this.mQuery = in.readParcelable(Query.class.getClassLoader());
    }

    public static final Creator<Stocks> CREATOR = new Creator<Stocks>() {
        @Override
        public Stocks createFromParcel(Parcel source) {
            return new Stocks(source);
        }

        @Override
        public Stocks[] newArray(int size) {
            return new Stocks[size];
        }
    };
}


