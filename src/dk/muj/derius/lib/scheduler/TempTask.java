package dk.muj.derius.lib.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;


/**
 * This is another version of RepeatingTask
 * this will however only be
 */
public abstract class TempTask implements Runnable
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private BukkitTask task;
	public Integer getTaskId() { return this.task.getTaskId(); }
	
	private final int times;
	public int getTimes() { return this.times; }
	
	private int invocation = 0;
	public int getInvocation() { return this.invocation; }
	public void setInvocation(int invocation) { this.invocation = invocation; }
	
	private long delayMillis;
	public long getDelayMillis() { return this.delayMillis; }
	public void setDelayMillis(long delayMillis) { this.delayMillis = delayMillis; }
	
	// When did the last invocation occur?
	private long previousMillis;
	public long getPreviousMillis() { return this.previousMillis; }
	public void setPreviousMillis(long previousMillis) { this.previousMillis = previousMillis; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public TempTask(long delayMilis, int times)
	{
		this(delayMilis, System.currentTimeMillis(), times);
	}
	
	public TempTask(long delayMillis, long previousMillis, int times)
	{
		this.setDelayMillis(delayMillis);
		this.setPreviousMillis(previousMillis);
		this.times = times;
	}
	
	// -------------------------------------------- //
	// ACTIVATION
	// -------------------------------------------- //
	
	public void activate()
	{
		task = Bukkit.getScheduler().runTaskTimer(this.getPlugin(), this, 1, 1);
	}

	public void deactivate()
	{
		this.task.cancel();
		this.task = null;
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void run()
	{
		if (this.getDelayMillis() < 1) return;
		
		long nowMillis = System.currentTimeMillis();
		
		while (this.getPreviousMillis() + this.getDelayMillis() < nowMillis)
		{
			if (invocation++ >= times)
			{
				this.deactivate();
				return;
			}
			
			long lastMillis = this.getPreviousMillis() + this.getDelayMillis();
			
			this.invoke();
			
			this.setPreviousMillis(lastMillis);
		}
		
	}

	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract void invoke();
	public abstract Plugin getPlugin();
}
