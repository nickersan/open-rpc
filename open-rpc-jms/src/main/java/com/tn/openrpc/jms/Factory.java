package com.tn.openrpc.jms;

/**
 * The interface to a simple factory object.
 */
public interface Factory<T>
{
  /**
   * Returns a new instance of <code>T</code>.
   */
  public T newInstance();
}
