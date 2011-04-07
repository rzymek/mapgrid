package pl.mapgrid;

public interface ProgressMonitor {
	public void updateProgress(int percent);
	public void setProgressMessage(String string);
}
