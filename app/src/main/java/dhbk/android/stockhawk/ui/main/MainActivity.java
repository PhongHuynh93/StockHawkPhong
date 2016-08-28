package dhbk.android.stockhawk.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dhbk.android.stockhawk.R;
import dhbk.android.stockhawk.SyncService;
import dhbk.android.stockhawk.data.model.Quote;
import dhbk.android.stockhawk.touch_helper.SimpleItemTouchHelperCallback;
import dhbk.android.stockhawk.ui.base.BaseActivity;
import dhbk.android.stockhawk.util.DialogFactory;
import dhbk.android.stockhawk.util.NetworkUtil;
import io.fabric.sdk.android.Fabric;

public class MainActivity  extends BaseActivity implements
        MainMvpView, StockAdapter.DismissStockListener {
    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "rajan.udacity.stock.hawk.ui.main.MainActivity.EXTRA_TRIGGER_SYNC_FLAG";

    // : 8/26/16 inject
    @Inject
    MainPresenter mMainPresenter;

    @Inject
    StockAdapter mStocksAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    /**
     *
     * // TODO: 8/26/16 - what method call this  ?
     * Return an Intent to start this Activity.
     * triggerDataSyncOnCreate allows disabling the background sync service onCreate. Should
     * only be set to false during testing.
     */
    public static Intent getStartIntent(Context context, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerDataSyncOnCreate);
        return intent;
    }


    // TODO: 8/26/16 oncreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        inject activity component
        activityComponent().inject(this);

        // declare recyclerview
        mRecyclerView.setAdapter(mStocksAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mStocksAdapter.setOnDismissStockListener(this);

//        attach view to this presenter and load datas from db
        mMainPresenter.attachView(this);
        mMainPresenter.loadStocks();

        // fixme - attach custome touch to recyclerview
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mStocksAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        // TODO: 8/26/16 find out the meaning of this row
        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            startService(SyncService.getStartIntent(this));
        }
    }

    /**
     * when click the fab, add the stock to recyclerview
     *         // fixme is network connect, show a dialog
     */
    @OnClick(R.id.fb_add_stock)
    void onClickAddStock() {
        if (NetworkUtil.isNetworkConnected(this)) {
            showMaterialDialogAddStock();
        }
        // if not, show a toast to inform to user
        else {
            Toast.makeText(this, getResources().getString(R.string.network_toast),
                    Toast.LENGTH_SHORT).show();
        }
    }
    // end making a dialog

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }

    // : 8/26/16, remove a stock
    @Override
    public void onStockDismiss(String symbol) {
        mMainPresenter.deleteStock(symbol);
    }

    // : 8/26/16, change the stocks data in the adapter and reset that list
    @Override
    public void showStocks(List<Quote> stocks) {
        mStocksAdapter.setStocks(stocks);
        mStocksAdapter.notifyDataSetChanged();
    }

    // TODO: 8/26/16, reset the list and show anothering
    @Override
    public void showStocksEmpty() {
        List<Quote> quotes = new ArrayList<>();
        mStocksAdapter.setStocks(quotes);
        mStocksAdapter.notifyDataSetChanged();
        Toast.makeText(this, R.string.empty_stocks, Toast.LENGTH_LONG).show();
    }

    // TODO: 8/26/16
    @Override
    public void showStock(Quote quote) {
        mStocksAdapter.setStock(quote);
    }

    // TODO: 8/26/16
    @Override
    public void showStockDoesNotExist() {
        Toast toast = Toast.makeText(this, getResources().getString(R.string.stock_does_not_exist),
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
        toast.show();
    }

    /**
     * fixme show a material dialog, build a dialog using the library
     */
    @Override
    public void showMaterialDialogAddStock() {
        new MaterialDialog.Builder(this).title(R.string.symbol_search)
                .content(R.string.content_test)
                .positiveColorRes(R.color.white)
                .positiveColor(Color.WHITE)
                .theme(Theme.DARK)
                .backgroundColorRes(R.color.material_blue_grey_800)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(R.string.input_hint, R.string.input_prefill,
                        (dialog, input) -> {
                            // On FAB click, receive user input. Make sure the stock doesn't
                            // already exist in the DB and proceed accordingly
                            if (checkSymbolExistOrNot(input.toString(),
                                    mStocksAdapter.getStocks())) {
                                showStockAlreadyExist();
                            } else if (!input.toString().isEmpty()){
                                mMainPresenter.loadStock(input.toString());
                            }
                        })
                .show();
    }

    @Override
    public Boolean checkSymbolExistOrNot(String symbol, List<Quote> stock) {
        return mMainPresenter.checkStocksExistOrNot(symbol, stock);
    }

    @Override
    public void showStockAlreadyExist() {
        Toast toast = Toast.makeText(this, getResources().getString(R.string.stocks_already_exist),
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
        toast.show();
    }

    @Override
    public void showError() {
        DialogFactory.createGenericErrorDialog(this, getString(R.string.error_loading_stocks))
                .show();
    }
}
