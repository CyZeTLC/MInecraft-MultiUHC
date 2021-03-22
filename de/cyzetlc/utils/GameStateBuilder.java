package de.cyzetlc.utils;

public interface GameStateBuilder 
{

	public abstract void start();
	public abstract void stop();
	public abstract void startIdle();
	public abstract void startWating();
	public abstract void stopIdle();
	public abstract boolean isRunning();

}
