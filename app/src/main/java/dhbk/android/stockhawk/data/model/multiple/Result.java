package dhbk.android.stockhawk.data.model.multiple;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import dhbk.android.stockhawk.data.model.Quote;


/**
 * Created by Rajan Maurya on 07/08/16.
 */

public class Result implements Parcelable {

    List<Quote> quote = new ArrayList<>();

    public List<Quote> getQuote() {
        return quote;
    }

    public void setQuote(List<Quote> quote) {
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
        dest.writeTypedList(this.quote);
    }

    public Result() {
    }

    protected Result(Parcel in) {
        this.quote = in.createTypedArrayList(Quote.CREATOR);
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
