package dhbk.android.stockhawk.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dhbk.android.stockhawk.R;
import dhbk.android.stockhawk.SyncService;
import dhbk.android.stockhawk.ui.base.BaseActivity;

public class MainActivity  extends BaseActivity implements
        MainMvpView, StockAdapter.DismissStockListener {
    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "rajan.udacity.stock.hawk.ui.main.MainActivity.EXTRA_TRIGGER_SYNC_FLAG";

    // TODO: 8/26/16 inject
    @Inject
    MainPresenter mMainPresenter;

    @Inject
    StockAdapter mStocksAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    /**
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

        //
        mRecyclerView.setAdapter(mStocksAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mStocksAdapter.setOnDismissStockListener(this);
        mMainPresenter.attachView(this);
        mMainPresenter.loadStocks();

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mStocksAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            startService(SyncService.getStartIntent(this));
        }
    }
}
