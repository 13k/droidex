package org.droidex.os;

public interface AsyncTaskAware<Progress, Result>
{
	public void onTaskPreExecute();
	public void onTaskProgressUpdate(Progress... values);
	public void onTaskPostExecute(Result result);
}
