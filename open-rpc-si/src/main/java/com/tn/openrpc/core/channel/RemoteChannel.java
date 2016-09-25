package com.tn.openrpc.core.channel;


import com.tn.openrpc.core.TimeoutException;

/**
 * The interface to an object that provides connectivity to a remote process.
 */
public interface RemoteChannel
{
  /**
   * Returns the <code>RemoteChannelResponse</code> received after sending the <code>remoteChannelRequest</code>.
   *
   * @throws RemoteChannelException the exception thrown an error occurs related to the channel.
   * @throws TimeoutException       the exception thrown if a response is not received in the allowed time.
   */
  public RemoteChannelResponse send(
    RemoteChannelRequest remoteChannelRequest
  ) throws RemoteChannelException, TimeoutException;
}
