package dk.muj.derius.lib;

import com.massivecraft.massivecore.ModuloRepeatTask;

/**
 * This is our better version of ModuloRepeatTask
 * that makes sure it will be executed at least X times per second
 * even in lag intensive situations.
 * This is especially useful for tasks being executed several times per second.
 */
public abstract class Task extends ModuloRepeatTask
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Task(long delayMilis)
	{
		this(delayMilis, System.currentTimeMillis());
	}
	
	public Task(long delayMilis, long previousMillis)
	{
		this.setDelayMillis(delayMilis);
		this.setPreviousMillis(previousMillis);
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
			long lastMillis = this.getPreviousMillis() + this.getDelayMillis();
			
			this.invoke(lastMillis);
			
			this.setPreviousMillis(lastMillis);
		}
	}
	
}
