package dhbk.android.stockhawk.data.model.single;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import dhbk.android.stockhawk.data.model.Quote;


/**
 * Created by Rajan Maurya on 07/08/16.
 */

public class Result implements Parcelable {

    @SerializedName("quote")
    Quote quote = new Quote();

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    @Override
    public String toString() {
        return "Result{" +
                "quote=" + quote +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.quote, flags);
    }

    public Result() {
    }

    protected Result(Parcel in) {
        this.quote = in.readParcelable(Quote.class.getClassLoader());
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel source) {
            return new Result(source);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };
}
