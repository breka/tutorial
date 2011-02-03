package org.imogene.android.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.imogene.android.Constants.Paths;
import org.imogene.android.preference.PreferenceHelper;
import org.imogene.android.util.WakeLockSingleton;
import org.imogene.android.util.http.multipart.FileInputStreamPart;
import org.imogene.android.util.http.multipart.MultipartEntity;
import org.imogene.android.util.http.multipart.Part;
import org.imogene.android.util.http.ssl.SSLHttpClient;
import org.xmlpull.v1.XmlSerializer;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

public class MetadataService extends Service {
	
	private static final String TAG = MetadataService.class.getName();
	
	private static final String EXTRA_VALUES = "values-to-send";
	private static final String EXTRA_EXCEPTION = "exception-to-send";
	private static final String EXTRA_TIME = "time-to-send";
	
	public static final void startMetadataService(Context context, ContentValues values) {
		WakeLockSingleton.getInstance(context).acquire();
		Intent i = new Intent();
		i.setClass(context, MetadataService.class);
		i.putExtra(EXTRA_VALUES, values);
		context.startService(i);
	}
	
	public static final void startMetadataService(Context context, Exception e, long time) {
		WakeLockSingleton.getInstance(context).acquire();
		Intent i = new Intent();
		i.setClass(context, MetadataService.class);
		i.putExtra(EXTRA_TIME, time);
		i.putExtra(EXTRA_EXCEPTION, e);
		context.startService(i);
	}
	
	private int mStartId = -1;
	
	private Thread mUploaderThead;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		mStartId = startId;
		
		mUploaderThead = new Thread(new Uploader(this, intent), "MetadataUploader");
		mUploaderThead.start();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mUploaderThead != null) {
			mUploaderThead.interrupt();
			try {
				mUploaderThead.join();
			} catch (InterruptedException e) {
				// Don't care
			}
		}
	}
	
	private void onFinish() {
		stopSelf(mStartId);
		WakeLockSingleton.getInstance(this).release();
	}
	
	private class Uploader implements Runnable {
		
		private static final String ATTR_TERMINAL = "terminal";
		private static final String ATTR_TIME = "time";
		private static final String ATTR_NAME = "name";
		private static final String ATTR_VALUE = "value";
		
		private static final String TAG_METADATA = "metadata";
		private static final String TAG_DATA = "data";
		private static final String TAG_EXCEPTION = "exception";
		
		private final Intent mIntent;
		private final MetadataClient mClient;
		private final String hardwareId;
		private final String realTime;
		
		public Uploader(Context context, Intent intent) {
			mIntent = intent;
			mClient = new MetadataClient(PreferenceHelper.getServerUrl(context));
			hardwareId = PreferenceHelper.getHardwareId(context);
			realTime = Long.toString(PreferenceHelper.getRealTime(context));
		}
		
		private void reportFinish() {
			mHandler.sendEmptyMessage(MSG_FINISHED);
		}
		
		public void run() {
			try {
				File file = new File(Paths.PATH_SYNCHRO, System.currentTimeMillis()+".metadata");
				FileOutputStream fos = new FileOutputStream(file);
				
				XmlSerializer serializer = Xml.newSerializer();
				serializer.setOutput(fos, null);
				
				serializer.startTag(null, TAG_METADATA);
				serializer.attribute(null, ATTR_TIME, realTime);
				serializer.attribute(null, ATTR_TERMINAL, hardwareId);
				
				if (mIntent.hasExtra(EXTRA_VALUES)) {
					ContentValues values = mIntent.getParcelableExtra(EXTRA_VALUES);
					for (Entry<String, Object> value : values.valueSet()) {
						if (value.getValue() != null) {
							serializer.startTag(null, TAG_DATA);
							serializer.attribute(null, ATTR_NAME, value.getKey());
							serializer.attribute(null, ATTR_VALUE, value.getValue().toString());
							serializer.endTag(null, TAG_DATA);
						}
					}
				} else if (mIntent.hasExtra(EXTRA_EXCEPTION)) {
					long time = mIntent.getLongExtra(EXTRA_TIME, 0);
					serializer.startTag(null, TAG_EXCEPTION);
					serializer.attribute(null, ATTR_TIME, Long.toString(time));
					
					StringWriter sw = new StringWriter();

					PrintWriter pw = new PrintWriter(sw);
					Exception e = (Exception) mIntent.getSerializableExtra(EXTRA_EXCEPTION);
					e.printStackTrace(pw);
					pw.flush();
					pw.close();
					
					serializer.cdsect(sw.toString());
					serializer.endTag(null, TAG_EXCEPTION);
				}
				
				serializer.endTag(null, TAG_METADATA);
				
				serializer.flush();
				
				serializer.endDocument();
				
				fos.flush();
				fos.close();
				
				FileInputStream fis = new FileInputStream(file);
				
				mClient.sendMetaData(fis);
			} catch (Exception e) {
				Log.e(TAG, "error sending metadata", e);
			} finally {
				reportFinish();
			}			
		}
	}
	
	public static class MetadataClient {
		
		private final String mServer;

		public MetadataClient(String server) {
			mServer = server + "sync.html";
		}
		
		/**
		 * Send meta data to the server
		 * @param fis the meta data to send
		 * @return 0 if done with success, -1 otherwise
		 * @throws Exception
		 */
		public final int sendMetaData(FileInputStream fis) throws Exception {	
			try {
				UUID uuid = UUID.randomUUID();
				
				HttpClient client = new SSLHttpClient();
				
				StringBuilder builder = new StringBuilder(mServer)
				.append("?cmd=meta")
				.append("&id="+uuid);
				
				HttpPost method = new HttpPost(builder.toString());
				
				String fileName = uuid + ".metadata";
				FileInputStreamPart part = new FileInputStreamPart("data", fileName, fis);
				MultipartEntity requestContent = new MultipartEntity(new Part[] {part});
				
				method.setEntity(requestContent);
				
				HttpResponse response = client.execute(method);
				int code = response.getStatusLine().getStatusCode();
				if(code == HttpStatus.SC_OK){
					return 0;
				} else {
					return -1;
				}
			} catch (Exception e) {
				throw new Exception("send meta data", e);
			}
		}
	}
	
	private static final int MSG_FINISHED = 0;
	
	private final Handler mHandler = new Handler() {
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_FINISHED:
				onFinish();
				break;
			}
		};
	};
}
