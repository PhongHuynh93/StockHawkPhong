package dhbk.android.stockhawk.ui.main;

import java.util.List;

import dhbk.android.stockhawk.data.model.Quote;
import dhbk.android.stockhawk.ui.base.MvpView;


public interface MainMvpView extends MvpView {

    void showStocks(List<Quote> quoteList);

    void showStocksEmpty();

    void showStock(Quote quote);

    void showStockDoesNotExist();

    void showMaterialDialogAddStock();

    Boolean checkSymbolExistOrNot(String symbol, List<Quote> quoteList);

    void showStockAlreadyExist();

    void showError();

}
