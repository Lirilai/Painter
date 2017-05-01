package ru.sberbank.learning.rates.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

import ru.sberbank.learning.rates.R;
import ru.sberbank.learning.rates.networking.CurrenciesList;
import ru.sberbank.learning.rates.networking.Currency;

/**
 * Created by Lirilai on 28.04.2017.
 */

public class Adapter extends BaseAdapter {

    private List<Currency> mCurrency;

    public Adapter(List<Currency> mCurrency) {
        if (mCurrency == null) {
            Log.v("myTag", "Список пуст");
        } else {
            this.mCurrency = mCurrency;
        }
    }


    @Override
    public int getCount() {
        return mCurrency.size();
    }

    @Override
    public Currency getItem(int position) {
        return mCurrency.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mCurrency.get(position).getNumCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.for_list_view, parent, false);
            ViewHolder holder = new ViewHolder();

            holder.mName = (TextView) view.findViewById(R.id.text_name);
            holder.mCode = (TextView) view.findViewById(R.id.text_code);
            holder.mValue = (TextView) view.findViewById(R.id.text_value);

            view.setTag(holder);


        }

        ViewHolder holder = (ViewHolder) view.getTag();
        Currency currencyInfo = getItem(position);


        if (currencyInfo.getName() != null  && currencyInfo.getCharCode() != null && currencyInfo.getValue() != null) {

            holder.mName.setText(R.string.load_name);
            holder.mCode.setText(R.string.load_code);
            holder.mValue.setText(R.string.load_value);

            CurrencyLoader loader = new CurrencyLoader(currencyInfo, holder.mName, holder.mCode, holder.mValue);
            loader.execute();


        } else {
            holder.mName.setText(R.string.load_name);
            holder.mCode.setText(R.string.load_code);
            holder.mValue.setText(R.string.load_value);

        }
        return view;

    }

    private static class ViewHolder {
        private TextView mName;
        private TextView mCode;
        private TextView mValue;
    }


    private static class CurrencyLoader extends AsyncTask<Void, Void, CurrencyLoader.InfoBundle> {

        private Currency mCurrency;
        private WeakReference<TextView> mTextName;
        private WeakReference<TextView> mTextCode;
        private WeakReference<TextView> mTextValue;


        public CurrencyLoader (Currency currency, TextView name, TextView code, TextView value) {
            mCurrency = currency;
            mTextName =  new WeakReference<>(name);
            mTextCode = new WeakReference<>(code);
            mTextValue = new WeakReference<>(value);


        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected InfoBundle doInBackground(Void... params) {

            InfoBundle bundle = new InfoBundle();
            bundle.mName = mCurrency.getName();
            bundle.mCode = mCurrency.getCharCode() + " " + mCurrency.getNumCode();
            bundle.mValue = mCurrency.getValue().toString();

            return bundle;
        }

        @Override
        protected void onPostExecute(InfoBundle result) {

            TextView textName = mTextName.get();
            TextView textCode = mTextCode.get();
            TextView textValue = mTextValue.get();

            if (result == null || textName == null || textCode == null || textValue == null) {
                return;
            }

            textName.setText(result.mName);
            textCode.setText(result.mCode);
            textValue.setText(result.mValue);

        }

        public static class InfoBundle {
            String mName;
            String mCode;
            String mValue;
        }
    }


}
