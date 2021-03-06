package dk.muj.derius.lib;

public class Mutable<T>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private T content;
	public T get() { return content; }
	public void set(T content) { this.content = content; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Mutable()
	{
		this.content = null;
	}
	
	public Mutable(T startContent)
	{
		this.content = startContent;
	}
	
}
