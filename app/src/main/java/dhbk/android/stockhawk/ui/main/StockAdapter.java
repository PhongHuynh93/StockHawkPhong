package dhbk.android.stockhawk.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dhbk.android.stockhawk.R;
import dhbk.android.stockhawk.data.model.Quote;
import dhbk.android.stockhawk.touch_helper.ItemTouchHelperAdapter;
import dhbk.android.stockhawk.touch_helper.ItemTouchHelperViewHolder;

/**
 *  14 - declare adapter for use with recuclerview
 *
 * this adapter in recyclerview can be swipe left and right
 */
public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder>
        implements ItemTouchHelperAdapter {

    private List<Quote> mQuoteList;

    private DismissStockListener mDismissStockListener;

    /**
     * make this adapter is a part of dagger
     */
    @Inject
    public StockAdapter() {
        mQuoteList = new ArrayList<>();
    }


    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock, parent, false);
        return new StockViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final StockViewHolder holder, int position) {
        Quote quote = mQuoteList.get(position);
        holder.tv_stock_symbol.setText(quote.getMsymbol());
        holder.tv_bid_price.setText(String.valueOf(quote.getBid()));
        holder.tv_change.setText(quote.getChangeinPercent());
    }

    @Override
    public int getItemCount() {
        return mQuoteList.size();
    }

    @Override
    public void onItemDismiss(int position) {
        mDismissStockListener.onStockDismiss(mQuoteList.get(position).getMsymbol());
        notifyItemRemoved(position);
    }

    public void setStocks(List<Quote> quotes) {
        mQuoteList = quotes;
    }

    public void setStock(Quote quote) {
        mQuoteList.add(quote);
        notifyDataSetChanged();
    }

    public List<Quote> getStocks() {
        return mQuoteList;
    }

    public StockAdapter setOnDismissStockListener(DismissStockListener listener) {
        mDismissStockListener = listener;
        return this;
    }

    /**
     * a viewholder class cntains views in a row of recyclerview
     */
    class StockViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        @BindView(R.id.stock_symbol) TextView tv_stock_symbol;
        @BindView(R.id.bid_price) TextView tv_bid_price;
        @BindView(R.id.change) TextView tv_change;

        public StockViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onItemSelected() {
            //itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            //itemView.setBackgroundColor(0);
        }
    }

    public interface DismissStockListener {
        void onStockDismiss(String symbol);
    }
}
