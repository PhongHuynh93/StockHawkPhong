package dhbk.android.stockhawk.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

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
import dhbk.android.stockhawk.util.NetworkUtil;

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
     */
    @OnClick(R.id.fb_add_stock)
    void onClickAddStock() {
        if (NetworkUtil.isNetworkConnected(this)) {
            showMaterialDialogAddStock();
        } else {
            Toast.makeText(this, getResources().getString(R.string.network_toast),
                    Toast.LENGTH_SHORT).show();
        }
    }

    // TODO: 8/26/16
    @Override
    public void onStockDismiss(String symbol) {

    }

    // TODO: 8/26/16
    @Override
    public void showStocks(List<Quote> quoteList) {

    }

    // TODO: 8/26/16
    @Override
    public void showStocksEmpty() {

    }

    // TODO: 8/26/16
    @Override
    public void showStock(Quote quote) {

    }

    // TODO: 8/26/16
    @Override
    public void showStockDoesNotExist() {

    }

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
    public Boolean checkSymbolExistOrNot(String symbol, List<Quote> quoteList) {
        return null;
    }

    @Override
    public void showStockAlreadyExist() {

    }

    @Override
    public void showError() {

    }
}
