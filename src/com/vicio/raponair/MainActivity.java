package com.vicio.raponair;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
//import org.apache.http.client.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;




public class MainActivity extends Activity implements ServiceConnection,Callback{


    MediaPlayer mp ;
	Boolean sonando;
    WebView browserCurrentSong, browserLasts, browserCargarSongs;//, browserCargoLasts;
    TextView txtInfo;
    Timer timer;
    String myHtml;

    //Variables para el formateo de LastsSongs
    /*Bitmap bitmap;
    ProgressDialog pDialog;
    ImageView cara1,cara2,cara3,cara4;
    int contadorCara = 1;
    */
    boolean t2 = false, t3 = false, t4 = false, t5 = false;

	Messenger main, servicio;

    final String TAG = "MainActivity"; //para los logs

	private static final int MSG_CONECTAR = 1;
    private static final int MSG_PONERPAUSE = 2;
    private static final int MSG_ERROR = 3;
    private static final int MSG_SMS = 4;

    // Para el tema de cargarLastSongs que solo entre la primera vez y ya
    Boolean cargarLatsSongsSoloDeUnoEnUno;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

        TabHost tabs=(TabHost)findViewById(R.id.tabHost);

        tabs.setup();


        TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        View tabIndicator1 = LayoutInflater.from(this).inflate(R.layout.tab_indicator, (ViewGroup) findViewById(android.R.id.tabs), false);
        ((TextView) tabIndicator1.findViewById(R.id.title)).setText("RADIO");
        ((ImageView) tabIndicator1.findViewById(R.id.icon)).setImageResource(R.drawable.wave);
        spec.setIndicator(tabIndicator1);
        //spec.setIndicator("",getResources().getDrawable(R.drawable.wave));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        View tabIndicator2 = LayoutInflater.from(this).inflate(R.layout.tab_indicator, (ViewGroup) findViewById(android.R.id.tabs), false);
        ((TextView) tabIndicator2.findViewById(R.id.title)).setText(getString(R.string.horario));
        ((ImageView) tabIndicator2.findViewById(R.id.icon)).setImageResource(R.drawable.watch);
        spec.setIndicator(tabIndicator2);
        //spec.setIndicator("",getResources().getDrawable(R.drawable.watch));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("mitab3");
        spec.setContent(R.id.tab3);
        View tabIndicator3 = LayoutInflater.from(this).inflate(R.layout.tab_indicator, (ViewGroup) findViewById(android.R.id.tabs), false);
        ((TextView) tabIndicator3.findViewById(R.id.title)).setText("WEB");
        ((ImageView) tabIndicator3.findViewById(R.id.icon)).setImageResource(R.drawable.web);
        spec.setIndicator(tabIndicator3);
        //spec.setIndicator("",getResources().getDrawable(R.drawable.web));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("mitab4");
        spec.setContent(R.id.tab4);
        View tabIndicator4 = LayoutInflater.from(this).inflate(R.layout.tab_indicator, (ViewGroup) findViewById(android.R.id.tabs), false);
        ((TextView) tabIndicator4.findViewById(R.id.title)).setText("FB");
        ((ImageView) tabIndicator4.findViewById(R.id.icon)).setImageResource(R.drawable.facebook);
        spec.setIndicator(tabIndicator4);
        //spec.setIndicator("",getResources().getDrawable(R.drawable.facebook));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("mitab5");
        spec.setContent(R.id.tab5);
        View tabIndicator5 = LayoutInflater.from(this).inflate(R.layout.tab_indicator, (ViewGroup) findViewById(android.R.id.tabs), false);
        ((TextView) tabIndicator5.findViewById(R.id.title)).setText("TWITTER");
        ((ImageView) tabIndicator5.findViewById(R.id.icon)).setImageResource(R.drawable.twitter);
        spec.setIndicator(tabIndicator5);
        //spec.setIndicator("",getResources().getDrawable(R.drawable.twitter));
        tabs.addTab(spec);


        tabs.setCurrentTab(0);

        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if(s=="mitab2" & !t2) {
                    WebView wlo=(WebView)findViewById(R.id.webLoad2);
                    wlo.loadUrl("file:///android_asset/loading.html");
                    wlo.setWebViewClient(new WebViewClient(){ //NO SE PUEDA HACER CLICK!
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            return true;
                        }
                    });
                    WebView www=(WebView)findViewById(R.id.webViewHorario);
                    WebSettings webSettings = www.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                    webSettings.setSupportZoom(true);
                    webSettings.setUseWideViewPort(true);
                    webSettings.setBuiltInZoomControls(true);
                    webSettings.setLoadWithOverviewMode(true);
                    webSettings.setUseWideViewPort(true);
                    www.setWebViewClient(new WebViewClient() {
                        public void onPageFinished(WebView view, String url) {
                            WebView loading=(WebView)findViewById(R.id.webLoad2);
                            loading.setVisibility(View.GONE);
                            view.setVisibility(View.VISIBLE);
                        }
                    });
                    www.loadUrl("http://www.raponair.com/wp-content/uploads/2015/05/HORARIO-web.jpg");
                    t2 = true;
                }else if(s=="mitab3" & !t3){
                    WebView wlo=(WebView)findViewById(R.id.webLoad3);
                    wlo.loadUrl("file:///android_asset/loading.html");
                    wlo.setWebViewClient(new WebViewClient(){ //NO SE PUEDA HACER CLICK!
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            return true;
                        }
                    });
                    WebView www=(WebView)findViewById(R.id.webViewWeb);
                    WebSettings webSettings = www.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                    webSettings.setSupportZoom(true);
                    webSettings.setUseWideViewPort(true);
                    webSettings.setBuiltInZoomControls(true);
                    www.setWebViewClient(new WebViewClient() {
                        public void onPageFinished(WebView view, String url) {
                            WebView loading=(WebView)findViewById(R.id.webLoad3);
                            loading.setVisibility(View.GONE);
                            view.setVisibility(View.VISIBLE);
                        }
                    });
                    www.loadUrl("http://raponair.com");


                    t3 = true;
                }else if(s=="mitab4" & !t4){
                    WebView wlo=(WebView)findViewById(R.id.webLoad4);
                    wlo.loadUrl("file:///android_asset/loading.html");
                    wlo.setWebViewClient(new WebViewClient(){ //NO SE PUEDA HACER CLICK!
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            return true;
                        }
                    });
                    WebView www=(WebView)findViewById(R.id.webViewFb);
                    www.getSettings().setJavaScriptEnabled(true);
                    www.setWebViewClient(new WebViewClient() {
                        public void onPageFinished(WebView view, String url) {
                            WebView loading=(WebView)findViewById(R.id.webLoad4);
                            loading.setVisibility(View.GONE);
                            view.setVisibility(View.VISIBLE);
                        }
                    });
                    www.loadUrl("https://m.facebook.com/pages/raponaircom/253681601363800");
                    t4 = true;
                }else if(s=="mitab5" & !t5){
                    WebView wlo=(WebView)findViewById(R.id.webLoad5);
                    wlo.setWebViewClient(new WebViewClient(){ //NO SE PUEDA HACER CLICK!
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            return true;
                        }
                    });
                    wlo.loadUrl("file:///android_asset/loading.html");
                    WebView www=(WebView)findViewById(R.id.webViewTwit);
                    www.getSettings().setJavaScriptEnabled(true);
                    www.setWebViewClient(new WebViewClient() {
                        public void onPageFinished(WebView view, String url) {
                            WebView loading=(WebView)findViewById(R.id.webLoad5);
                            loading.setVisibility(View.GONE);
                            view.setVisibility(View.VISIBLE);
                        }
                    });
                    www.loadUrl("https://mobile.twitter.com/raponair");



                    t5 = true;
                }


            }
        });

        Log.i(TAG,"onCreate - Creadas todas las tabs");

		main = new Messenger(new Handler(this));
        txtInfo=(TextView) findViewById(R.id.txtInfo);
        browserCurrentSong=(WebView) findViewById(R.id.webViewNombre);
        browserLasts=(WebView) findViewById(R.id.webViewLasts);
        browserCargarSongs =(WebView) findViewById(R.id.webViewCargarSongs);

		try {


            //AppRater.app_launched(this);
		
			getBoton().setOnClickListener(new View.OnClickListener() {

			    public void onClick(View v) {
			    	playOstop();
			    }
			});

            browserCurrentSong.getSettings().setJavaScriptEnabled(true);
            browserCurrentSong.setBackgroundColor(0);
            //browserCurrentSong.getSettings().setLoadWithOverviewMode(true);
            //browserCurrentSong.getSettings().setUseWideViewPort(true);


            browserLasts.getSettings().setJavaScriptEnabled(true);
            browserLasts.setBackgroundColor(0);
/*            browserLasts.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
            //WebViewClient must be set BEFORE calling loadUrl!
            browserLasts.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    browserLasts.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                }
            });*/

            browserCargarSongs.setBackgroundColor(0);
            browserCargarSongs.loadUrl("file:///android_asset/loading.html");


            if (servicioCorriendo()){
                setSonando(true);
                getBoton().setBackgroundResource(R.drawable.playerpause);
                //cargarCurrentSong();
            }else{
                setSonando(false);
                getBoton().setBackgroundResource(R.drawable.playerplay);
                setTxtInfo(getString(R.string.aqueesperas));
                playOstop();
            }



		} catch (IllegalStateException e) {
			setTxtInfo("Error:" + e.getMessage());
		}
	}

    private void lanzarTimer(){
        timer=new Timer();
        timer.schedule(new TimerTask()
        {
            // @Override
            public void run()
            {
                //browser.reload();
                //browserLasts.reload();
                Log.i(TAG,"Timer - Entro");
                cargarCurrentSong();
                cargarLatsSongs();
            }
        },1,30000);//
    }
	//FUNCION QUE ES LLAMADA POR EL BOTON
	public void playOstop()  {
		 
		 if(estaSonando()){
             setTxtInfo(getString(R.string.teesperamos));
             //browser.loadUrl("http://www.raponair.com/banners/BANNER%20RAP%20ON%20AIR.gif");
			 pararServicio();
			 
		 }else{
             setTxtInfo(getString(R.string.cargando));
             arrancarServicio();

		 }
		 
	 }
	private void pararServicio(){
		 stopService(new Intent(this,ServicioRapOnAir.class));
		 getBoton().setBackgroundResource(R.drawable.playerplay);
		 setSonando(false);
         browserCurrentSong.setVisibility(View.GONE);
         getBoton().setEnabled(true);

     }
	private void arrancarServicio(){
		 try {

			 startService(new Intent(this,ServicioRapOnAir.class));
			 setSonando(true);
			 if (bindService(new Intent(this, ServicioRapOnAir.class), this, 0)){
                 getBoton().setBackgroundResource(R.drawable.playerload);
				 getBoton().setEnabled(false);
			 }else{
					setTxtInfo("Error!");
			 }


		} catch (Exception e) {
			setTxtInfo("ERROR");
		}
	 }
	private boolean servicioCorriendo() {
		    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
		        if (ServicioRapOnAir.class.getName().equals(service.service.getClassName())) {
		            return true;
		        }
		    }
		    return false;
		}

	public void setTxtInfo(String mensaje) {
        browserCurrentSong.setVisibility(View.GONE);
        txtInfo.setVisibility(View.VISIBLE);
        txtInfo.setText(mensaje);
		}

    private void cargarCurrentSong(){
        Log.i(TAG,"cargarCurrentSong - Entro");
        myHtml = getHTML("http://api.radionomy.com/currentsong.cfm?radiouid=a3babc1f-617b-488b-9fba-9757cfb66e38&apikey=c3ff5cc2-41f8-41ca-bddf-8aad372a586c&callmeback=yes&type=xml&cover=yes");
        browserCurrentSong.post(new Runnable() {
            @Override
            public void run() {
                Tracks tracks = new Tracks(myHtml);
                browserCurrentSong.loadData(showTracksInHtml(tracks), "text/html", "UTF-8");
            }
        });

    }
    private void cargarLatsSongs(){

        Log.i(TAG,"cargarLatsSongs - Last songs of radionomy.com");
        myHtml = getHTML("http://api.radionomy.com/tracklist.cfm?radiouid=a3babc1f-617b-488b-9fba-9757cfb66e38&apikey=c3ff5cc2-41f8-41ca-bddf-8aad372a586c&amount=5&type=xml&cover=yes");
        browserCargarSongs.post(new Runnable() {
            @Override
            public void run() {

                Tracks tracks = new Tracks(myHtml);
                browserCargarSongs.loadData(showTracksInHtml(tracks), "text/html", "UTF-8");
            }
        });

    }

    private String showTracksInHtml(Tracks tracks){
        String myHTML = "<html><head><title>ouyeahmate</title>" +
                "<style type=\"text/css\">" +
                "body {background-color: #0C0C0C; }" +
                "table {color: #A8A8A8; font-family: verdana; width: 100%;}" +
                "strong {color: #686868; font-family: verdana;}" +
                "</style></head><body><table>";
        for(int i=0;i< tracks.getList().size();i++){
            if (i == 0) {
                myHTML += "<tr style=\"background-color: #1E1E1E;\">";
            } else {
                myHTML += "<tr>";
            }
            myHTML += "<td width=\"72px\"><img src=\"";
            myHTML += tracks.getList().get(i).getCover();
            myHTML += "\" height=\"72px\" width=\"72px\"/></td>";
            if (i==0) {
                myHTML += "<td style=\"color: #FFFFFF;\"><strong style=\"color: #CCCCCC;\">";
            } else {
                myHTML += "<td><strong>";
            }
            myHTML += tracks.getList().get(i).getArtits();
            myHTML += "</strong><br/>";
            myHTML += tracks.getList().get(i).getTitle();
            myHTML += "</td>";
            myHTML += "</tr>";
        }

        myHTML += "</table></body></html>";
        Log.i(TAG, myHTML);
        return myHTML;
    }

private String getHTML(String url){
    StringBuilder str = null;
    String html = "";
    try {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);


        InputStream in = response.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        str = new StringBuilder();
        String line = null;
        while((line = reader.readLine()) != null)
        {
            str.append(line);
        }
        in.close();
        html = str.toString();
    } catch (IOException e) {
        html = "ERROR";
    }
    return html;
}

    private Boolean estaSonando(){
		 if (sonando != null)
			 return sonando;
		 else
			 return false;
	 }
	public void setSonando(Boolean sonando) {
		this.sonando = sonando;
	}
	public ImageButton getBoton(){
		ImageButton boton =  (ImageButton) findViewById(R.id.btnControl);
		return boton;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_about:
                Intent myIntent = new Intent(this, AboutActivity.class);
                startActivityForResult(myIntent, 0);
                return true;

            case R.id.menu_salir:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		
		servicio = new Messenger(service);
		
		Message msg = new Message();
		msg.what = MSG_CONECTAR;
		msg.replyTo = main; //Es importante indicar el replyTo, pues será a donde responda el Service
		
		try {
			servicio.send(msg);
		} catch (RemoteException e) {
			setTxtInfo("Error de servicio.");
		}
		
		
	}



	@Override
	public void onServiceDisconnected(ComponentName name) {
		
	}

    //Aqui recibo los mensajes que manda el servicio. Como poner el icono de pause porque ha activado la música
	@Override
	public boolean handleMessage(Message msg) {
		
		switch (msg.what) {
		case MSG_PONERPAUSE:
			
			getBoton().setBackgroundResource(R.drawable.playerpause);
			getBoton().setEnabled(true);
            txtInfo.setVisibility(View.GONE);
            browserCurrentSong.setVisibility(View.VISIBLE);
            lanzarTimer();
            break;
		case MSG_ERROR:
			setTxtInfo(getString(R.string.errorconexion));
			pararServicio();
			break;
		case MSG_SMS:
			setTxtInfo(msg.obj.toString());
			break;
		default:
			break;
		}
		
		return false;
	}


}
