package ru.sberbank.learning.rates;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import ru.sberbank.learning.rates.networking.CurrenciesList;
import ru.sberbank.learning.rates.utils.Adapter;
import ru.sberbank.learning.rates.utils.AsyncInfo;

import static android.content.ContentValues.TAG;

public class RatesActivity extends Activity implements AsyncInfo {

    private ProgressBar mBar;
    private ListView mListView;
    private Adapter adapter;
    private TextView mCodeView;

    public static final String EXTRA_CODE = "code";
    public static final String EXTRA_VALUE = "value";
    public static final String EXTRA_NOMINAL = "nominal";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);

        mCodeView = (TextView) findViewById(R.id.text_code);

        mBar = (ProgressBar) findViewById(R.id.bar);
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setVisibility(ListView.INVISIBLE);
        MyTask m=new MyTask();
        m.delegate = this;
        m.execute();

    }


    @Override
    public void processFinish(CurrenciesList output) {

        if (output.getCurrencies().size()>0) {

            mBar.setVisibility(ProgressBar.INVISIBLE);
            mListView.setVisibility(ListView.VISIBLE);
            adapter = new Adapter(output.getCurrencies());
            mListView.setAdapter(adapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(view.getContext(), Calculate.class);
                    intent.putExtra(RatesActivity.EXTRA_CODE, adapter.getItem(position).getCharCode());
                    intent.putExtra(RatesActivity.EXTRA_NOMINAL, adapter.getItem(position).getNominal());
                    intent.putExtra(RatesActivity.EXTRA_VALUE, adapter.getItem(position).getValue());
                    startActivity(intent);
                }
            });
        } else {
            Log.v(TAG, "toast");
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No Internet Connection", Toast.LENGTH_LONG);
            toast.show();
        }

    }


    private class MyTask extends AsyncTask<Void, Void, CurrenciesList> {

        public AsyncInfo delegate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected CurrenciesList doInBackground(Void... params) {

            CurrenciesList curList;
            try {
                InputStream input = new URL("http://www.cbr.ru/scripts/XML_daily.asp").openStream();
                 curList = CurrenciesList.readFromStream(input);
            }
            catch (IOException e) {

                e.printStackTrace();
                curList = new CurrenciesList();
                Log.v(TAG, "catch");
            }
            SystemClock.sleep(1000);
            Log.v(TAG, "return");
            return curList;
        }


        @Override
        protected void onPostExecute(CurrenciesList result) {
            super.onPostExecute(result);

             try {
                 delegate.processFinish(result);
             }
             catch (Exception e){

                 Log.v(TAG, e.getMessage());
                 mBar.setVisibility(ProgressBar.INVISIBLE);

                 Toast toast = Toast.makeText(getApplicationContext(),
                         "No Internet Connection", Toast.LENGTH_LONG);
                 toast.show();
             }


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


