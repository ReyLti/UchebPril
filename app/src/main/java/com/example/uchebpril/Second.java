package com.example.uchebpril;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Second extends AppCompatActivity {
    TextView tvInfo;
    EditText tvName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        tvName = (EditText) findViewById(R.id.editTextTextPersonName);
    }
    public void onclick(View v) {
        Second.MyTask mt = new Second.MyTask();
        mt.execute(tvName.getText().toString());
    }
}
public class ClAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    List<String[]> lines;
    ClAdapter(Context context, List<String[]> elines){
        ctx = context;
        lines = elines;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return lines.size();
    }
    @Override
    public Object getItem(int position) {
        return lines.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        };
        String[] p =(String[]) getItem(position);
        ((TextView) view.findViewById(R.id.tvText)).setText(p[0]);
        ((TextView) view.findViewById(R.id.tvText1)).setText(p[1]);
        return view;
    };
}
class MyTask extends AsyncTask<String, Void, ArrayList<String[]>> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        tvInfo.setText("Begin");
    }
    @Override
    protected ArrayList<String[]> doInBackground(String... params) {
        ArrayList<String[]> res=new ArrayList <>();
        HttpURLConnection myConnection = null;
        try {
            URL mySite = new
                    URL("http://10.0.2.2:8080/json?id=1&name="+params[0]);
            myConnection =
                    (HttpURLConnection) mySite.openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int i=0;
        try {
            i = myConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (i==200) {
            InputStream responseBody=null;
            try {
                responseBody = myConnection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStreamReader responseBodyReader =null;
            try {
                responseBodyReader =
                        new InputStreamReader(responseBody, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            JsonReader jsonReader;
            jsonReader = null;
            jsonReader = new JsonReader(responseBodyReader);
            try {
                jsonReader.beginArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String key=null;
            String value =null;
            while (true) {
                try {
                    if (!jsonReader.hasNext()) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    jsonReader.beginObject();
                } catch (IOException e) {
                    e.printStackTrace();
                };
                String[] str=new String[2];
                int n=0;
                while (true) {
                    try {
                        if (!jsonReader.hasNext()) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        key = jsonReader.nextName();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
// sb.append("\r\n : " +key);
                    try {
                        value = jsonReader.nextString();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
// sb.append("\r\n : " +value);
                    str[n]=value;
                    n++;
                }
                try {
                    jsonReader.endObject();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                res.add(str);
            }
            try {
                jsonReader.endArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        myConnection.disconnect();
        return res;
    }
    @Override
    protected void onPostExecute(ArrayList<String[]> result) {
        super.onPostExecute(result);
        Second.ClAdapter clAdapter=new Second.ClAdapter(tvInfo.getContext(),result);
        ListView lvMain = (ListView) findViewById(R.id.lvMain);
        lvMain.setAdapter(clAdapter);
        tvInfo.setText("End");
    }
}
