package dk.muj.derius.lib;

import com.massivecraft.massivecore.ModuloRepeatTask;

/**
 * @deprecated use RepeatTask
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
		
		return;
	}
	
}
