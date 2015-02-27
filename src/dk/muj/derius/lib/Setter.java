package dk.muj.derius.lib;

/**
 * This will just set a single value.
 * Perfect for storing the values somewhere else.
 */
@FunctionalInterface
public interface Setter<T>
{
	public void set(T value);
}
