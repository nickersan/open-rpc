package com.tn.openrpc.core;

/**
 * The exception thrown when an error occurs in a <code>RemoteMethod</code>.
 */
public class RemoteMethodException extends Exception
{
  /**
   * Creates a new <code>RemoteMethodException</code> initialized with the <code>message</code>.
   */
  public RemoteMethodException(String message)
  {
    super(message);
  }

  /**
   * Creates a new <code>RemoteMethodException</code> initialized with the <code>message</code> and
   * <code>cause</code>.
   */
  public RemoteMethodException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
