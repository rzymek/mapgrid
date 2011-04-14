package pl.mapgrid.utils;

public interface ProgressMonitor {
	public void updateProgress(int percent);
	public void setProgressMessage(String string);
}
