
package apsupportapp.aperotechnologies.com.designapp.MPM;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.squareup.picasso.Downloader;


import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.Httpcall.ApiRequest;
import apsupportapp.aperotechnologies.com.designapp.Httpcall.HttpResponse;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;

public class mpm_activity extends AppCompatActivity implements HttpResponse,View.OnClickListener,DownloadFile.Listener {

    private SharedPreferences sharedPreferences;
    private String userId;
    private String bearertoken;
    private String departmentName;
    private RequestQueue queue;
    private Context context;
    private int limit = 100;
    private int offsetvalue = 0;
    private String TAG = "mpm_activity";
    private mpm_model model_mpm;
    private String url;
    public static int clickPosition=0;
    private Cache cache;
    private Network network;
    private ListView listView;
    private ArrayList<mpm_model> list;
    private RelativeLayout WebViewProcess;
    private RelativeLayout mpm_imageBtnBack,Pdf_zoom_btn;
    public mpm_activity pre_activity;
    private LinearLayout WebView_match_layout,WebView_wrap_layout;
    private TextView Toolbar_title;
    private int dublicatePosition=0;
    private TextView Process_count,Pages_count,Pages_total;
    private LinearLayout Bottom_listItem,BaseLayout;
    private boolean error=false;
    private LinearLayout WebViewWrap;
    private mpm_adapter mpmAdapter;
    private RemotePDFViewPager remotePDFViewPager;
    private PDFPagerAdapter adapter;
    private DownloadFile.Listener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpm_activity);
        context = this;
        checkCollapsing();
        intialise();



        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            mpm_model model=new mpm_model();
            ApiRequest api_request = new ApiRequest(context, bearertoken, url, TAG, queue,model);
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }


    }




    private void intialise() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Log.e(TAG, "userID and token" + userId + "and this is" + bearertoken);
        cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        Toolbar_title=(TextView)findViewById(R.id.toolbar_title);
        Process_count = (TextView) findViewById(R.id.process_count);
        Pages_count = (TextView) findViewById(R.id.pages_count);
        Pages_total = (TextView) findViewById(R.id.pages_total);
        WebViewProcess = (RelativeLayout) findViewById(R.id.webview_process);
        mpm_imageBtnBack = (RelativeLayout) findViewById(R.id.mpm_imageBtnBack);
        Pdf_zoom_btn = (RelativeLayout) findViewById(R.id.pdf_zoom_btn);
        Bottom_listItem = (LinearLayout) findViewById(R.id.bottom_listItem);
        BaseLayout = (LinearLayout) findViewById(R.id.baseLayout);
        BaseLayout.setVisibility(View.GONE);

        Bottom_listItem.setVisibility(View.VISIBLE);
        WebViewProcess.setVisibility(View.GONE);
        WebViewProcess.setOnClickListener(this);
        Pdf_zoom_btn.setOnClickListener(this);

        WebViewWrap = (LinearLayout) findViewById(R.id.webview_wrap);

        url = ConstsCore.web_url + "/v1/display/mpmproducts/" + userId + "?offset=" + offsetvalue + "&limit=" + limit;
        Log.e(TAG, "web_url: " + url);
        listView = (ListView) findViewById(R.id.department_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if (WebViewProcess.getVisibility() == View.VISIBLE) {
                    Toast.makeText(context, "Please wait file is working above...", Toast.LENGTH_SHORT).show();
                } else {
                    clickPosition=position;
                    departmentName=list.get(position).getProductName();
                    mpmAdapter.notifyDataSetChanged();
                    WebViewProcess.setVisibility(View.VISIBLE);
                  //  new GetbytesFrompdf().execute(list.get(position).getMpmPath());
                    callPdf(position);
                    Log.e(TAG, "clickPosition: in Activity "+clickPosition );

                }
            }
        });


        mpm_imageBtnBack.setOnClickListener(this);
    }

    private void checkCollapsing() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

    }


    @Override
    public void response(ArrayList<mpm_model> list) {
        listener = (DownloadFile.Listener) context;
        Log.e(TAG, "response: " + list.size());
        this.list = list;
        mpmAdapter = new mpm_adapter(context, list);
        listView.setAdapter(mpmAdapter);
        BaseLayout.setVisibility(View.VISIBLE);
        departmentName=list.get(0).getProductName();
        // set web view for read pdf...
        WebViewProcess.setVisibility(View.VISIBLE);
       // new GetbytesFrompdf().execute(list.get(0).getMpmPath());
        callPdf(0);





    }

    private void callPdf(int position) {
        remotePDFViewPager = new RemotePDFViewPager(context,list.get(position).getMpmPath(), listener);
        remotePDFViewPager.setId(R.id.pdfViewPager);
        remotePDFViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.e(TAG, "onPageScrolled: "+position+"total count is" );
                int count=position+1;
                Pages_count.setText(""+count);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mpm_imageBtnBack:
                onBackPressed();
                break;
            case R.id.pdf_zoom_btn:
                if(Bottom_listItem.getVisibility()==View.VISIBLE){
                    Reusable_Functions.ViewGone(Bottom_listItem);
                    Toolbar_title.setText(departmentName);
                }else{
                    Reusable_Functions.ViewVisible(Bottom_listItem);
                    Toolbar_title.setText("Season Catalogue");

                }
                break;
            case R.id.webview_process:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(Bottom_listItem.getVisibility()==View.VISIBLE){
            error=true;
            clickPosition=0;
            finish();
        }else{
            Reusable_Functions.ViewVisible(Bottom_listItem);
            Toolbar_title.setText("MPM");

        }

    }

/*    @Override
    public void onPageChanged(int page, int pageCount) {
        Log.e(TAG, "onPageChanged: "+page+"and page count"+pageCount );
        int count=page+1;
        Pages_total.setText(""+pageCount);
        Pages_count.setText(""+count);
    }

    @Override
    public void loadComplete(int nbPages) {
        Log.e(TAG, "loadComplete: "+nbPages );
        WebViewProcess.setVisibility(View.GONE);

    }*/




    @Override
    public void onSuccess(final String url, String destinationPath) {
        Log.e(TAG, "onSuccess: "+url+" and "+destinationPath );
        Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable() {
            public void run() {
                // Run your task here
                adapter = new PDFPagerAdapter(context, FileUtil.extractFileNameFromURL(url));
                remotePDFViewPager.setAdapter(adapter);
                WebViewProcess.setVisibility(View.GONE);
                WebViewWrap.removeAllViewsInLayout();
                WebViewWrap.addView(remotePDFViewPager,
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Pages_total.setText(""+adapter.getCount());
                Process_count.setText(""+(int)0);
                Log.e(TAG, "run: Total count is>>>>>>"+adapter.getCount() );

            }
        }, 1000 );

    }

    @Override
    public void onFailure(Exception e) {
        WebViewProcess.setVisibility(View.GONE);
        Log.e(TAG, "onFailure: "+e.getMessage() );
    }

    @Override
    public void onProgressUpdate(int progress, int total) {
        Log.e(TAG, "onProgressUpdate: "+progress+" and "+total );
        Process_count.setText(""+(int)((progress*100)/total));



    }


    public class GetbytesFrompdf extends AsyncTask<String,String,String> {

        private URL PdfUrl;
        private int total=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e(TAG, "onPreExecute: >>>>>>>" );
            Process_count.setText("0");
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.e(TAG, "doInBackground: start" );



            String path=strings[0];
            Log.e(TAG, "doInBackground: path "+path );
            URI uri = null;
            URLConnection connection = null;
            try {
                uri = new URI(path);
                PdfUrl=uri.toURL();
                connection = PdfUrl.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();


                // Since you get a URLConnection, use it to get the InputStream
                // Now that the InputStream is open, get the content length
                int contentLength = connection.getContentLength();

                // To avoid having to resize the array over and over and over as
                // bytes are written to the array, provide an accurate estimate of
                // the ultimate size of the byte array
                ByteArrayOutputStream tmpOut;
                if (contentLength != -1) {
                    tmpOut = new ByteArrayOutputStream(contentLength);
                } else {
                    tmpOut = new ByteArrayOutputStream(16384); // Pick some appropriate size
                }

                byte[] buf = new byte[512];
                while (true) {
                    if(error==true)  // error is when you press back then it will intrupt and stop.
                    {
                        Log.e(TAG, "doInBackground: process has been interrupted !");
                        break;
                    }
                    int len = in.read(buf);
                    if (len == -1) {
                        break;
                    }
                    total += len;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/contentLength));
                    Log.e(TAG, "doInBackground: "+contentLength+"and total is "+total );
                    tmpOut.write(buf, 0, len);
                }
                in.close();
                tmpOut.close(); // No effect, but good to do anyway to keep the metaphor alive

                byte[] array = tmpOut.toByteArray();

                handle(array);

            } catch (IOException e1) {
                Log.e(TAG, "IOException: "+e1.getMessage() );
                OnMainThread();
                e1.printStackTrace();
            } catch (URISyntaxException e2) {
                Log.e(TAG, "URISyntaxException: "+e2.getMessage());
                OnMainThread();
                e2.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, "onPostExecute: >>>>>>>" );
            total=0;

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Process_count.setText(""+Integer.parseInt(values[0]));



        }
    }

    private void OnMainThread()
    {
        mpm_activity.this.runOnUiThread(new Runnable() {
            public void run() {
                Reusable_Functions.MakeToast(mpm_activity.this,"data failed...");
                WebViewProcess.setVisibility(View.GONE);

            }
        });
    }


    private void handle(final byte[] array) {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable() {
            public void run() {
                // Run your task here
              //  loadpdf(array);
            }
        }, 1000 );
    }

 /*   private void loadpdf(byte[] array) {

        WebViewWrap.fromBytes(array)

                //  .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                .defaultPage(0)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }*/


}

