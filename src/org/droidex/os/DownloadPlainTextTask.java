package org.droidex.os;

import android.os.AsyncTask;
import android.util.Log;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.net.URL;
import java.net.HttpURLConnection;
import org.droidex.util.StreamUtils;

public class DownloadPlainTextTask
	extends AsyncTask<URL, Integer, byte[]>
{
	private AsyncTaskAware delegator;

	public DownloadPlainTextTask(AsyncTaskAware obj)
	{
		super();
		delegator = obj;
	}

	protected byte[] doInBackground(URL... urls)
	{
		URL url = urls[0];
		HttpURLConnection conn = null;

		try {
			conn = (HttpURLConnection) url.openConnection();
			InputStream in = new BufferedInputStream(conn.getInputStream());
			return StreamUtils.readBytes(in);
		} catch (Exception e) {
			Log.e(DownloadPlainTextTask.class.toString(), "Exception", e);
		} finally {
			if (conn != null)
				conn.disconnect();
		}
		
		return null;
	}

	protected void onPreExecute()
	{
		delegator.onTaskPreExecute();
	}

	protected void onProgressUpdate(Integer... progress)
	{
		delegator.onTaskProgressUpdate((Object)progress);
	}

	protected void onPostExecute(byte[] result)
	{
		delegator.onTaskPostExecute(result);
	}
}
