package com.tn.openrpc.core;

/**
 * A <code>RemoteMethod</code> represents a method in another process.
 */
public interface RemoteMethod
{
  /**
   * Invokes the remote method.
   *
   * @param args the arguments used for the method call.
   *
   * @return the result of the method call.
   *
   * @throws IllegalArgumentException if the arguments do not match those expected by the method.
   * @throws RemoteMethodException if an error occurs invoking this remote method.
   */
  public Object invoke(Object... args) throws IllegalArgumentException, RemoteMethodException;
}
