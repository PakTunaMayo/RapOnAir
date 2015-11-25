package com.vicio.raponair;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class ServicioRapOnAir extends Service implements Callback{

    MediaPlayer reproductor;
    private NotificationManager nm;  
    private static final int ID_NOTIFICACION_CREAR = 1;
    
    private static final int MSG_CONECTAR = 1;
    private static final int MSG_PONERPAUSE = 2;
    private static final int MSG_ERROR = 3;
    private static final int MSG_SMS = 4;
    
    Messenger main, servicio;


    @Override

    public void onCreate() {

    	 nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    	 servicio = new Messenger(new Handler(this));

    }



    @Override

    public int onStartCommand(Intent intenc, int flags, int idArranque) {

          

          return START_STICKY;
          

    }



    @Override

    public void onDestroy() {
    	if (reproductor != null){
    		if(reproductor.isPlaying())
    			reproductor.stop();
    			reproductor.release();
    	}  
          nm.cancel(ID_NOTIFICACION_CREAR);

    }



    @Override
    public IBinder onBind(Intent intencion) {

    	return servicio.getBinder();

    }

    public void conectar(){
    	try {
			//reproductor = MediaPlayer.create(this, Uri.parse("http://188.165.241.117:8008/stream"));
            //reproductor = MediaPlayer.create(this, Uri.parse("http://listen.radionomy.com/raponair.m3u"));
            //reproductor = MediaPlayer.create(this, Uri.parse("http://streaming.radionomy.com/Raponair"));
            reproductor = MediaPlayer.create(this, Uri.parse("http://streaming.radionomy.com/RAP-ON-AIR-24h"));

          } catch (Exception e) {
			Message m = new Message();
			m.what = MSG_ERROR;
			m.obj = e.getMessage();
			try {
				main.send(m);
			} catch (RemoteException ex) {
			}
          }
    }
    public boolean play(){
    	boolean res = false;
    	try {


			reproductor.start();
			res = true;
            //TODO actualizar
//			 Notification notificacion = new Notification(
//			         R.drawable.on32,
//			         getString(R.string.escuchando),
//			         System.currentTimeMillis() );
//			 PendingIntent intencionPendiente = PendingIntent.getActivity(
//			          this, 0, new Intent(this, MainActivity.class), 0);
//
//
//			notificacion.setLatestEventInfo(this, "Rap On Air","Escuchando emisión", intencionPendiente);
//
            Notification noti = new Notification.Builder(this)
                    .setContentTitle("Rap On Air")
                    .setContentText("Escuchando emisión")
                    .setSmallIcon(R.drawable.on32)
                    .build();
			nm.notify(ID_NOTIFICACION_CREAR, noti);
          } catch (Exception e) {
  			Message m = new Message();
  			m.what = MSG_ERROR;
  			m.obj = e.getMessage();
  			try {
  				main.send(m);
  			} catch (RemoteException ex) {
                ex.printStackTrace();
  			}
  			
  		}
    	return res;
    }

	@Override
	public boolean handleMessage(Message msg) {
		
		switch (msg.what) {
			case MSG_CONECTAR:
                new Thread(new Runnable() {
                    public void run() {
                        conectar();
                        if (play()){
                            Message m = new Message();
                            m.what = MSG_PONERPAUSE;
                            m.obj = "PonImagenPause";
                            try {
                                main.send(m);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }else{
                            onDestroy();
                        }
                    }
                }).start();
				main = msg.replyTo;

			
			break;
		
		
		}
		return false;
	}


}