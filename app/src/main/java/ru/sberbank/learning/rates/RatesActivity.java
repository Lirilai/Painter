package ru.sberbank.learning.rates;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import ru.sberbank.learning.rates.networking.CurrenciesList;
import ru.sberbank.learning.rates.utils.Adaptor;
import ru.sberbank.learning.rates.utils.AsynkInfo;

public class RatesActivity extends Activity implements AsynkInfo{

    private ProgressBar mBar;
    private ListView mListView;
    private Adaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);


        mBar = (ProgressBar) findViewById(R.id.bar);
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setVisibility(ListView.INVISIBLE);
        MyTask m=new MyTask();
        m.delegate = this;
        m.execute();


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void processFinish(CurrenciesList output) {
        mBar.setVisibility(ProgressBar.INVISIBLE);
        mListView.setVisibility(ListView.VISIBLE);
        adaptor = new Adaptor(output.getCurrencies());
        mListView.setAdapter(adaptor);


    }


    private static class MyTask extends AsyncTask<Void, Void, CurrenciesList> {

        public AsynkInfo delegate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }



        @Override
        protected CurrenciesList doInBackground(Void... params) {


            CurrenciesList curList;
            try {
                InputStream input = new URL("http://www.cbr.ru/scripts/XML_daily.asp").openStream();
                 curList= CurrenciesList.readFromStream(input);
        }
        catch (IOException e) {
            e.printStackTrace();
            curList=new CurrenciesList();
        }
            SystemClock.sleep(1000);
            return curList;
        }


        @Override
        protected void onPostExecute(CurrenciesList result) {
            super.onPostExecute(result);
            delegate.processFinish(result);

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.reload:
                mListView.setVisibility(ListView.INVISIBLE);
                mBar.setVisibility(ProgressBar.VISIBLE);
                MyTask m=new MyTask();
                m.delegate = this;
                m.execute();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}


