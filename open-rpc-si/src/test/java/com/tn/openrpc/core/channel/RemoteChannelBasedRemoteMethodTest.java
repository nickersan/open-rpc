package com.tn.openrpc.core.channel;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.tn.openrpc.core.RemoteInvocationTargetException;

/**
 * Test cases for <code>RemoteChannelBasedRemoteMethod</code>.
 */
public class RemoteChannelBasedRemoteMethodTest
{
  /**
   * Tests a successful call to <code>RemoteChannelBasedRemoteMethod.invoke(Object...)</code>.
   */
  @Test
  public void testInvoke() throws Exception
  {
    String target = "test";
    Object[] args = new Object[]{"one", 2};
    Object result = new Object();

    RemoteChannel remoteChannel = createMock(RemoteChannel.class);
    expect(remoteChannel.send(new RemoteChannelRequest(target, args))).andReturn(new RemoteChannelResponse(result));
    replay(remoteChannel);

    assertEquals(
      result,
      new RemoteChannelBasedRemoteMethod(remoteChannel, target).invoke(args)
    );

    verify(remoteChannel);
  }

  /**
   * Tests a call to <code>RemoteChannelBasedRemoteMethod.invoke(Object...)</code> when an exception is returned.
   */
  @Test
  public void testInvokeWithException() throws Exception
  {
    String target = "test";
    Object[] args = new Object[]{"one", 2};
    Object result = new Exception();

    RemoteChannel remoteChannel = createMock(RemoteChannel.class);
    expect(remoteChannel.send(new RemoteChannelRequest(target, args))).andReturn(new RemoteChannelResponse(result));
    replay(remoteChannel);

    try
    {
      new RemoteChannelBasedRemoteMethod(remoteChannel, target).invoke(args);
    }
    catch (RemoteInvocationTargetException e)
    {
      assertEquals(result, e.getCause());
    }

    verify(remoteChannel);
  }
}
