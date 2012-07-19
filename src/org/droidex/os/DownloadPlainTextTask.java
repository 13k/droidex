package org.droidex.os;

import android.os.AsyncTask;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.net.URL;
import java.net.HttpURLConnection;
import org.droidex.util.StreamUtils;
import org.droidex.util.L;

public class DownloadPlainTextTask
    extends AsyncTask<URL, Void, byte[]>
{
    private AsyncTaskAware<Void, byte[]> delegator;

    public DownloadPlainTextTask(AsyncTaskAware<Void, byte[]> obj)
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
            L.e("Exception", e);
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        
        return null;
    }

    protected void onPreExecute() {
        try {
            delegator.onTaskPreExecute();
        } catch (UnsupportedOperationException e) {
        }
    }

    protected void onProgressUpdate(Void... progress) {
        try {
            delegator.onTaskProgressUpdate(progress);
        } catch (UnsupportedOperationException e) {
        }
    }

    protected void onPostExecute(byte[] result) {
        try {
            delegator.onTaskPostExecute(result);
        } catch (UnsupportedOperationException e) {
        }
    }

    protected void onCancelled(byte[] result) {
        try {
            delegator.onTaskCancelled(result);
        } catch (UnsupportedOperationException e) {
        }
    }

    protected void onCancelled() {
        try {
            delegator.onTaskCancelled();
        } catch (UnsupportedOperationException e) {
        }
    }
}
