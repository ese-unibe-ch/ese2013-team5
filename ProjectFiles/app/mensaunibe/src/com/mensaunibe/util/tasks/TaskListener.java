package com.mensaunibe.util.tasks;

/**
 * Classes wishing to be notified of rendering updates implement this
 */
public interface TaskListener {
	/**
	 * Notifies that the task has completed
	 *
	 * @param result Double result of the task
	 */
	public void onTaskComplete(Object result);

	/**
	 * Notifies of progress
	 *
	 * @param percent int value from 0-100
	 */
	public void onProgressUpdate(int percent);
	
	/**
	 * Notifies that the rendering is done
	 */
	public void onRendered();
}