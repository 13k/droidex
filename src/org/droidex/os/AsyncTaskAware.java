package org.droidex.os;

public interface AsyncTaskAware<Progress, Result>
{
	public void onTaskPreExecute()
		throws UnsupportedOperationException;

	public void onTaskProgressUpdate(Progress... values)
		throws UnsupportedOperationException;

	public void onTaskPostExecute(Result result)
		throws UnsupportedOperationException;

	public void onTaskCancelled(Result result)
		throws UnsupportedOperationException;

	public void onTaskCancelled()
		throws UnsupportedOperationException;
}
