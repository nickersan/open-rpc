package com.tn.openrpc.core;

/**
 * <code>RemoteInvocationTargetException</code> is a checked exception that wraps an exception thrown by an invoked
 * <code>RemoteMethod</code>.
 *
 * @see RemoteMethod
 */
public class RemoteInvocationTargetException extends RemoteMethodException
{
  /**
   * Creates a new <code>RemoteInvocationTargetException</code> initialized with the <code>cause</code>.
   */
  public RemoteInvocationTargetException(Throwable cause)
  {
    super(cause.getMessage(), cause);
  }
}
