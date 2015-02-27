package dk.muj.derius.lib;

/**
 * This will just get a single value.
 * Perfect for easy getting values from a runtime changing config,
 * or for storing the values somewhere else.
 */
@FunctionalInterface
public interface Getter<T>
{
	public T get();
}
