package org.droidex;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.net.URL;
import org.droidex.adapters.MenuListAdapter;
import org.droidex.os.AsyncTaskAware;
import org.droidex.os.DownloadPlainTextTask;
import org.droidex.util.MenuParser;
import org.droidex.util.NameOrderedDict;
import org.xmlpull.v1.XmlPullParserException;

public class Droidex
	extends ListActivity
	implements AsyncTaskAware<Void, byte[]>
{
	private static final String MENU_FEED_URL = "http://www.pcasc.usp.br/restaurante.xml";
	private static final String MENU_XML_ENCODING = "ISO-8859-1";

	private MenuListAdapter adapter;
	
	private TextView statusText;
	private ListView listView;
	private ProgressDialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.main);

		statusText = (TextView) findViewById(android.R.id.empty);
		listView = (ListView) findViewById(android.R.id.list);
		
		try {
			showLoading();
			downloadXml();
		} catch (Exception e) {
			hideLoading();
			d("exc: " + e);
			statusText.setText(R.string.err_exception);
			e.printStackTrace();
		}
    }
	
	public void onTaskPreExecute() {
		d("onTaskPreExecute()");
	}

	public void onTaskProgressUpdate(Void... values) {
		d("onTaskProgressUpdate()");
	}

	public void onTaskPostExecute(byte[] buffer)
	{
		// String xml = new String(buffer);
		String xml = "";

		try {
			xml = new String(buffer, MENU_XML_ENCODING);
		} catch (UnsupportedEncodingException e) {
			statusText.setText(R.string.err_exception);
			e.printStackTrace();
			d("exc: " + e);
		}

		//d("onTaskPostExecute()\n" + xml + "\n---");

		hideLoading();

		if ("".equals(xml)) {
			statusText.setText(R.string.err_network);
			return;
		}

		try {
			MenuParser parser = new MenuParser();
			NameOrderedDict menu = parser.parse(xml);
			adapter = new MenuListAdapter(this, R.layout.list_item, menu);
			setListAdapter(adapter);
			//listView.expandGroup(adapter.findGroupPositionByDayOfWeek());
		} catch (XmlPullParserException e) {
			statusText.setText(R.string.err_xml);
			e.printStackTrace();
			d("exc: " + e);
		} catch (Exception e) {
			statusText.setText(R.string.err_exception);
			e.printStackTrace();
			d("exc: " + e);
		}
	}

	protected void onListItemClick(ListView l, View v, int pos, long id)
	{
		d("listItemClick " + pos);
		// TODO: hide main screen and show day menu
	}

	protected void onBackPressed() {
		d("backPressed, finish()");
		finish();
	}

	private void downloadXml()
		throws MalformedURLException
	{
		d("downloadXml()");
		URL xmlUrl = new URL(MENU_FEED_URL);
		new DownloadPlainTextTask(this).execute(xmlUrl);
	}

	private void showLoading()
	{
		d("showLoading()");
		loadingDialog = ProgressDialog.show(Droidex.this, "",
				getResources().getText(R.string.loading), true);
	}

	private void hideLoading()
	{
		d("hideLoading()");
		if ((loadingDialog != null) && (loadingDialog.isShowing()))
			loadingDialog.dismiss();
	}

	private void d(String msg) {
		Log.d(Droidex.class.toString(), msg);
	}
}
