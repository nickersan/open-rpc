package com.tn.openrpc.core.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tn.openrpc.core.RemoteInvocationTargetException;
import com.tn.openrpc.core.RemoteMethod;
import com.tn.openrpc.core.RemoteMethodException;

/**
 * An implementation of <code>RemoteMethod</code> that works with a <code>RemoteChannel</code> to perform method calls.
 */
public class RemoteChannelBasedRemoteMethod implements RemoteMethod
{
  private static final Logger LOGGER = LoggerFactory.getLogger(RemoteChannelBasedRemoteMethod.class);

  private RemoteChannel remoteChannel;
  private String target;

  /**
   * Creates a new <code>RemoteChannelBasedRemoteMethod</code>.
   *
   * @param remoteChannel the channel over which method invocations will be made.
   * @param target the target of the method invocations.
   */
  public RemoteChannelBasedRemoteMethod(RemoteChannel remoteChannel, String target)
  {
    this.remoteChannel = remoteChannel;
    this.target = target;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object invoke(Object... args) throws IllegalArgumentException, RemoteMethodException
  {
    try
    {
      RemoteChannelRequest remoteChannelRequest = new RemoteChannelRequest(
        target,
        args
      );

      LOGGER.debug("Sending request: {} on remote channel: {}", remoteChannelRequest, remoteChannel);

      RemoteChannelResponse remoteChannelResponse = remoteChannel.send(
        remoteChannelRequest
      );

      LOGGER.debug("Received response: {} on remote channel: {}", remoteChannelRequest, remoteChannel);

      Object result = remoteChannelResponse.getResult();

      if (result instanceof IllegalArgumentException)
      {
        throw (IllegalArgumentException)result;
      }
      else if (result instanceof Throwable)
      {
        throw new RemoteInvocationTargetException((Throwable)result);
      }
      else
      {
        return result;
      }
    }
    catch (RemoteChannelException e)
    {
      throw new RemoteMethodException("An error occurred sending request over the remote channel.", e);
    }
  }
}
