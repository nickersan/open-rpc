package com.tn.openrpc.core;

/**
 * The exception thrown when a timeout occurs making invoking a <code>RemoteMethod</code>.
 *
 * @see RemoteMethod
 */
public class TimeoutException extends RemoteMethodException
{
  /**
   * Creates a new <code>TimeoutException</code> initialized with the <code>message</code>.
   */
  public TimeoutException(String message)
  {
    super(message);
  }
}
