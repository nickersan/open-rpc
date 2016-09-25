package com.tn.openrpc.core.channel;

/**
 * The exception thrown when an internal error occurs in a <code>RemoteChannel</code>.
 *
 * @see RemoteChannel
 */
public class RemoteChannelException extends Exception
{
  /**
   * Creates a new <code>RemoteChannelException</code> initialized with the <code>message</code>.
   */
  public RemoteChannelException(String message)
  {
    super(message);
  }

  /**
   * Creates a new <code>RemoteChannelException</code> initialized with the <code>message</code> and
   * <code>cause</code>.
   */
  public RemoteChannelException(String message, Throwable cause)
  {
    super(message, cause);
  }

  /**
   * Creates a new <code>RemoteChannelException</code> initialized with the <code>cause</code>.
   */
  public RemoteChannelException(Throwable cause)
  {
    super(cause);
  }
}
