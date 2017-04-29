package ru.sberbank.learning.rates.utils;

import ru.sberbank.learning.rates.networking.CurrenciesList;

/**
 * Created by Lirilai on 29.04.2017.
 */

public interface AsynkInfo {
    void processFinish(CurrenciesList output);
}
