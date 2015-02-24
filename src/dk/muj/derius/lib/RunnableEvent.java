package dk.muj.derius.lib;

import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public interface RunnableEvent extends Runnable
{
	// -------------------------------------------- //
	// OVERRIDE: RUNNABLE
	// -------------------------------------------- //
	
	@Override
	public default void run()
	{
		if ( ! (this instanceof Event)) throw new UnsupportedOperationException("This interface should only be implemented by Bukkit events");
		this.preRun();
		Bukkit.getPluginManager().callEvent((Event) this);
		this.postRun();
		if (this instanceof CancellableEvent) CancellableEvent.events.remove(this);
	}
	
	// -------------------------------------------- //
	// SMART RUN
	// -------------------------------------------- //
	
	/**
	 * this is a smarter run, that will tell you whether or not it suceeded.
	 * @return true if event was not cancelled
	 */
	public default boolean runEvent()
	{
		this.run();
		
		if (this instanceof Cancellable) return ((Cancellable) this).isCancelled();
		return true;
	}
	
	// -------------------------------------------- //
	// PRE & POST
	// -------------------------------------------- //
	
	public default void preRun() {};
	public default void postRun() {};
	
}
