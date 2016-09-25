package com.tn.openrpc.jms;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Test;

import com.tn.openrpc.core.TimeoutException;
import com.tn.openrpc.core.channel.RemoteChannelException;
import com.tn.openrpc.core.channel.RemoteChannelRequest;
import com.tn.openrpc.core.channel.RemoteChannelResponse;
import com.tn.openrpc.core.serialization.Serializer;
import com.tn.openrpc.jms.Factory;
import com.tn.openrpc.jms.JmsRemoteChannel;
import com.tn.openrpc.jms.JmsRemoteChannelRequest;
import com.tn.openrpc.jms.JmsRemoteChannelResponse;

/**
 * Test cases for <code>JmsRemoteChannel</code>.
 */
public class JmsRemoteChannelTest
{
  /**
   * Tests a successful call to <code>JmsRemoteChannel.send(RemoteChannelRequest)</code>.
   */
  @Test
  public void testSend() throws Exception
  {
    RemoteChannelRequest remoteChannelRequest = new RemoteChannelRequest("test");
    RemoteChannelResponse remoteChannelResponse = new RemoteChannelResponse("result");

    String serializedRequest = "test-request";
    String serializedResponse = "test-response";

    Object correlationId = new Object();

    TextMessage requestTextMessage = createMock(TextMessage.class);
    replay(requestTextMessage);

    MessageProducer messageProducer = createMock(MessageProducer.class);
    messageProducer.send(requestTextMessage);
    replay(messageProducer);

    Destination requestDestination = createMock(Destination.class);
    replay(requestDestination);

    Session requestSession = createMock(Session.class);
    expect(requestSession.getTransacted()).andReturn(false).anyTimes();
    expect(requestSession.createProducer(requestDestination)).andReturn(messageProducer);
    expect(requestSession.createTextMessage(serializedRequest)).andReturn(requestTextMessage);
    replay(requestSession);

    Connection requestConnection = createMock(Connection.class);
    expect(requestConnection.createSession(false, Session.AUTO_ACKNOWLEDGE)).andReturn(requestSession);
    replay(requestConnection);

    Destination responseDestination = createMock(Destination.class);
    replay(responseDestination);

    TextMessage responseTextMessage = createMock(TextMessage.class);
    expect(responseTextMessage.getText()).andReturn(serializedResponse);
    replay(responseTextMessage);

    MessageConsumer messageConsumer = createMock(MessageConsumer.class);
    expect(messageConsumer.receive(10000)).andReturn(responseTextMessage);
    replay(messageConsumer);

    Session responseSession = createMock(Session.class);
    expect(responseSession.getTransacted()).andReturn(false).anyTimes();
    expect(responseSession.getAcknowledgeMode()).andReturn(Session.AUTO_ACKNOWLEDGE);
    expect(responseSession.createConsumer(responseDestination, null)).andReturn(messageConsumer);
    replay(responseSession);

    Connection responseConnection = createMock(Connection.class);
    expect(responseConnection.createSession(false, Session.AUTO_ACKNOWLEDGE)).andReturn(responseSession);
    responseConnection.start();
    replay(responseConnection);

    ConnectionFactory connectionFactory = createMock(ConnectionFactory.class);
    expect(connectionFactory.createConnection()).andReturn(requestConnection);
    expect(connectionFactory.createConnection()).andReturn(responseConnection);
    replay(connectionFactory);

    @SuppressWarnings({"unchecked"})
    Factory<Object> correlationIdFactory = createMock(Factory.class);
    expect(correlationIdFactory.newInstance()).andReturn(correlationId);
    replay(correlationIdFactory);

    @SuppressWarnings({"unchecked"})
    Serializer<String> serializer = createMock(Serializer.class);
    expect(serializer.serialize(new JmsRemoteChannelRequest(correlationId, remoteChannelRequest))).andReturn(
      serializedRequest
    );
    expect(serializer.deserialize(serializedResponse)).andReturn(
      new JmsRemoteChannelResponse(correlationId, remoteChannelResponse)
    );
    replay(serializer);

    assertEquals(
      remoteChannelResponse,
      new JmsRemoteChannel(
        connectionFactory,
        requestDestination,
        responseDestination,
        correlationIdFactory,
        serializer
      ).send(
        remoteChannelRequest
      )
    );

    verify(
      requestTextMessage,
      messageProducer,
      requestDestination,
      requestSession,
      requestConnection,
      responseTextMessage,
      messageConsumer,
      responseDestination,
      responseSession,
      responseConnection,
      connectionFactory,
      correlationIdFactory,
      serializer
    );
  }

  /**
   * Tests a call to <code>JmsRemoteChannel.send(RemoteChannelRequest)</code> where the timeout is exceeded.
   */
  @Test
  public void testSendWithTimeout() throws Exception
  {
    RemoteChannelRequest remoteChannelRequest = new RemoteChannelRequest("test");

    String serializedRequest = "test-request";

    Object correlationId = new Object();

    TextMessage requestTextMessage = createMock(TextMessage.class);
    replay(requestTextMessage);

    MessageProducer messageProducer = createMock(MessageProducer.class);
    messageProducer.send(requestTextMessage);
    replay(messageProducer);

    Destination requestDestination = createMock(Destination.class);
    replay(requestDestination);

    Session requestSession = createMock(Session.class);
    expect(requestSession.getTransacted()).andReturn(false).anyTimes();
    expect(requestSession.createProducer(requestDestination)).andReturn(messageProducer);
    expect(requestSession.createTextMessage(serializedRequest)).andReturn(requestTextMessage);
    replay(requestSession);

    Connection requestConnection = createMock(Connection.class);
    expect(requestConnection.createSession(false, Session.AUTO_ACKNOWLEDGE)).andReturn(requestSession);
    replay(requestConnection);

    Destination responseDestination = createMock(Destination.class);
    replay(responseDestination);

    MessageConsumer messageConsumer = createMock(MessageConsumer.class);
    expect(messageConsumer.receive(10000)).andReturn(null);
    replay(messageConsumer);

    Session responseSession = createMock(Session.class);
    expect(responseSession.getTransacted()).andReturn(false).anyTimes();
    expect(responseSession.getAcknowledgeMode()).andReturn(Session.AUTO_ACKNOWLEDGE);
    expect(responseSession.createConsumer(responseDestination, null)).andReturn(messageConsumer);
    replay(responseSession);

    Connection responseConnection = createMock(Connection.class);
    expect(responseConnection.createSession(false, Session.AUTO_ACKNOWLEDGE)).andReturn(responseSession);
    responseConnection.start();
    replay(responseConnection);

    ConnectionFactory connectionFactory = createMock(ConnectionFactory.class);
    expect(connectionFactory.createConnection()).andReturn(requestConnection);
    expect(connectionFactory.createConnection()).andReturn(responseConnection);
    replay(connectionFactory);

    @SuppressWarnings({"unchecked"})
    Factory<Object> correlationIdFactory = createMock(Factory.class);
    expect(correlationIdFactory.newInstance()).andReturn(correlationId);
    replay(correlationIdFactory);

    @SuppressWarnings({"unchecked"})
    Serializer<String> serializer = createMock(Serializer.class);
    expect(serializer.serialize(new JmsRemoteChannelRequest(correlationId, remoteChannelRequest))).andReturn(
      serializedRequest
    );
    replay(serializer);

    try
    {
      new JmsRemoteChannel(
        connectionFactory,
        requestDestination,
        responseDestination,
        correlationIdFactory,
        serializer
      ).send(
        remoteChannelRequest
      );

      fail("No exception thrown when timeout occurred.");
    }
    catch (TimeoutException e)
    {
      //expected.
    }

    verify(
      requestTextMessage,
      messageProducer,
      requestDestination,
      requestSession,
      requestConnection,
      messageConsumer,
      responseDestination,
      responseSession,
      responseConnection,
      connectionFactory,
      correlationIdFactory,
      serializer
    );
  }

  /**
   * Tests a call to <code>JmsRemoteChannel.send(RemoteChannelRequest)</code> where the correlation ids do not match.
   */
  @Test
  public void testSendWithCorrelationError() throws Exception
  {
    RemoteChannelRequest remoteChannelRequest = new RemoteChannelRequest("test");
    RemoteChannelResponse remoteChannelResponse = new RemoteChannelResponse("result");

    String serializedRequest = "test-request";
    String serializedResponse = "test-response";

    Object correlationId1 = new Object();
    Object correlationId2 = new Object();

    TextMessage requestTextMessage = createMock(TextMessage.class);
    replay(requestTextMessage);

    MessageProducer messageProducer = createMock(MessageProducer.class);
    messageProducer.send(requestTextMessage);
    replay(messageProducer);

    Destination requestDestination = createMock(Destination.class);
    replay(requestDestination);

    Session requestSession = createMock(Session.class);
    expect(requestSession.getTransacted()).andReturn(false).anyTimes();
    expect(requestSession.createProducer(requestDestination)).andReturn(messageProducer);
    expect(requestSession.createTextMessage(serializedRequest)).andReturn(requestTextMessage);
    replay(requestSession);

    Connection requestConnection = createMock(Connection.class);
    expect(requestConnection.createSession(false, Session.AUTO_ACKNOWLEDGE)).andReturn(requestSession);
    replay(requestConnection);

    Destination responseDestination = createMock(Destination.class);
    replay(responseDestination);

    TextMessage responseTextMessage = createMock(TextMessage.class);
    expect(responseTextMessage.getText()).andReturn(serializedResponse);
    replay(responseTextMessage);

    MessageConsumer messageConsumer = createMock(MessageConsumer.class);
    expect(messageConsumer.receive(10000)).andReturn(responseTextMessage);
    replay(messageConsumer);

    Session responseSession = createMock(Session.class);
    expect(responseSession.getTransacted()).andReturn(false).anyTimes();
    expect(responseSession.getAcknowledgeMode()).andReturn(Session.AUTO_ACKNOWLEDGE);
    expect(responseSession.createConsumer(responseDestination, null)).andReturn(messageConsumer);
    replay(responseSession);

    Connection responseConnection = createMock(Connection.class);
    expect(responseConnection.createSession(false, Session.AUTO_ACKNOWLEDGE)).andReturn(responseSession);
    responseConnection.start();
    replay(responseConnection);

    ConnectionFactory connectionFactory = createMock(ConnectionFactory.class);
    expect(connectionFactory.createConnection()).andReturn(requestConnection);
    expect(connectionFactory.createConnection()).andReturn(responseConnection);
    replay(connectionFactory);

    @SuppressWarnings({"unchecked"})
    Factory<Object> correlationIdFactory = createMock(Factory.class);
    expect(correlationIdFactory.newInstance()).andReturn(correlationId1);
    replay(correlationIdFactory);

    @SuppressWarnings({"unchecked"})
    Serializer<String> serializer = createMock(Serializer.class);
    expect(serializer.serialize(new JmsRemoteChannelRequest(correlationId1, remoteChannelRequest))).andReturn(
      serializedRequest
    );
    expect(serializer.deserialize(serializedResponse)).andReturn(
      new JmsRemoteChannelResponse(correlationId2, remoteChannelResponse)
    );
    replay(serializer);

    try
    {
      new JmsRemoteChannel(
        connectionFactory,
        requestDestination,
        responseDestination,
        correlationIdFactory,
        serializer
      ).send(
        remoteChannelRequest
      );

      fail("Not exception thrown when correlation ids did not match.");
    }
    catch (RemoteChannelException e)
    {
      //expected.
    }

    verify(
      requestTextMessage,
      messageProducer,
      requestDestination,
      requestSession,
      requestConnection,
      responseTextMessage,
      messageConsumer,
      responseDestination,
      responseSession,
      responseConnection,
      connectionFactory,
      correlationIdFactory,
      serializer
    );
  }
}
