package dk.muj.derius.lib.scheduler;

import com.massivecraft.massivecore.ModuloRepeatTask;

/**
 * This is another version of RepeatingTask
 * this will however only be
 */
public abstract class TempTask extends ModuloRepeatTask
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	private final int times;
	public int getTimes() { return this.times; }
	
	private int invocation = 0;
	public int getInvocation() { return this.invocation; }
	public void setInvocation(int invocation) { this.invocation = invocation; }
	
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
			
			this.invoke(lastMillis);
			
			this.setPreviousMillis(lastMillis);
		}
		
	}
	
	@Override
	public void invoke(long l)
	{
		this.invoke();
	}

	// -------------------------------------------- //
	// ABSTRACT
	// -------------------------------------------- //
	
	public abstract void invoke();
}
