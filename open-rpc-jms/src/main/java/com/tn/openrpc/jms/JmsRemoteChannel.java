package com.tn.openrpc.jms;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.tn.openrpc.core.channel.RemoteChannel;
import com.tn.openrpc.core.channel.RemoteChannelException;
import com.tn.openrpc.core.channel.RemoteChannelRequest;
import com.tn.openrpc.core.channel.RemoteChannelResponse;
import com.tn.openrpc.core.serialization.Serializer;
import com.tn.openrpc.core.serialization.SerializationException;

/**
 * An implementation of <code>RemoteChannel</code> that uses <i>JMS</i>.  The <code>Destination</code>s used by this
 * <code>JmsRemoteChannel</code> should be for its exclusive use.  Any messages received on the <i>response</i>
 * <code>Destination</code> that do not correlate with a <i>request</i> will cause an exception.
 */
public class JmsRemoteChannel implements RemoteChannel
{
  private static final Logger LOGGER = LoggerFactory.getLogger(JmsRemoteChannel.class);

  private static final long DEFAULT_TIMEOUT = 10 * 1000;

  private ConnectionFactory connectionFactory;
  private Factory<Object> correlationIdFactory;
  private Destination requestDestination;
  private Destination responseDestination;
  private Serializer<String> serializer;
  private long timeout;

  /**
   * Creates a new <code>JmsRemoteChannel</code>.
   *
   * @param connectionFactory    the connection factory used to connection to the destinations.
   * @param requestDestination   the destination used to send requested.
   * @param responseDestination  the destination used to receive responses.
   * @param correlationIdFactory the factory that creates correlation ids used to correlate requests and responses.
   * @param serializer           the serializer used to serialize requests and de-serialize the responses.
   */
  public JmsRemoteChannel(
    ConnectionFactory connectionFactory,
    Destination requestDestination,
    Destination responseDestination,
    Factory<Object> correlationIdFactory,
    Serializer<String> serializer
  )
  {
    this(
      connectionFactory,
      requestDestination,
      responseDestination,
      correlationIdFactory,
      serializer,
      DEFAULT_TIMEOUT
    );
  }

  /**
   * Creates a new <code>JmsRemoteChannel</code>.
   *
   * @param connectionFactory    the connection factory used to connection to the destinations.
   * @param requestDestination   the destination used to send requested.
   * @param responseDestination  the destination used to receive responses.
   * @param correlationIdFactory the factory that creates correlation ids used to correlate requests and responses.
   * @param serializer           the serializer used to serialize requests and de-serialize the responses.
   * @param timeout              the timeout allowed for each send to complete in.
   */
  public JmsRemoteChannel(
    ConnectionFactory connectionFactory,
    Destination requestDestination,
    Destination responseDestination,
    Factory<Object> correlationIdFactory,
    Serializer<String> serializer,
    long timeout
  )
  {
    this.connectionFactory = connectionFactory;
    this.requestDestination = requestDestination;
    this.responseDestination = responseDestination;
    this.correlationIdFactory = correlationIdFactory;
    this.serializer = serializer;
    this.timeout = timeout;
  }

  /**
   * Returns the timeout, in milliseconds, allowed for the method call to return.
   */
  public long getTimeout()
  {
    return timeout;
  }

  /**
   * Sets the timeout, in milliseconds, allowed for the method call to return.
   */
  public void setTimeout(long timeout)
  {
    this.timeout = timeout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized RemoteChannelResponse send(
    RemoteChannelRequest remoteChannelRequest
  )
    throws RemoteChannelException, TimeoutException
  {
    try
    {
      JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
      jmsTemplate.setReceiveTimeout(timeout);

      JmsRemoteChannelRequest jmsRemoteChannelRequest = new JmsRemoteChannelRequest(
        correlationIdFactory.newInstance(),
        remoteChannelRequest
      );

      LOGGER.debug("Sending request: " + jmsRemoteChannelRequest);

      jmsTemplate.send(
        requestDestination,
        new JmsRemoteChannelRequestMessageCreator(jmsRemoteChannelRequest)
      );

      Message message = jmsTemplate.receive(responseDestination);

      if (message == null)
      {
        throw new TimeoutException("Timeout waiting for response.");
      }

      if (!(message instanceof TextMessage))
      {
        throw new RemoteChannelException("Response message is not a handled type: " + message);
      }

      JmsRemoteChannelResponse jmsRemoteChannelResponse = serializer.deserialize(
        ((TextMessage)message).getText()
      );

      if (!jmsRemoteChannelRequest.getCorrelationId().equals(jmsRemoteChannelResponse.getCorrelationId()))
      {
        throw new RemoteChannelException("Response does not correlate with request.");
      }

      return jmsRemoteChannelResponse.getRemoteChannelResponse();
    }
    catch (RuntimeException e)
    {
      if (e.getCause() instanceof RemoteChannelException)
      {
        throw (RemoteChannelException)e.getCause();
      }
      else
      {
        throw e;
      }
    }
    catch (SerializationException e)
    {
      throw new RemoteChannelException("An error occurred deserializing a response.", e);
    }
    catch (JMSException e)
    {
      throw new RemoteChannelException("An error occurred reading the response.", e);
    }
  }

  /**
   * An implementation of <code>MessageCreator</code> that creates a <code>TextMessage</code> containing a
   * <code>JmsRemoteChannelRequest</code>.
   */
  private class JmsRemoteChannelRequestMessageCreator implements MessageCreator
  {
    private JmsRemoteChannelRequest jmsRemoteChannelRequest;

    /**
     * Creates a new <code>JmsRemoteChannelRequestMessageCreator</code> initialized with the
     * <code>jmsRemoteChannelRequest</code> that will contained by the message this object creates.
     */
    public JmsRemoteChannelRequestMessageCreator(JmsRemoteChannelRequest jmsRemoteChannelRequest)
    {
      this.jmsRemoteChannelRequest = jmsRemoteChannelRequest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message createMessage(Session session) throws JMSException
    {
      try
      {
        return session.createTextMessage(serializer.serialize(jmsRemoteChannelRequest));
      }
      catch (SerializationException e)
      {
        throw new RuntimeException(new RemoteChannelException(e));
      }
    }
  }
}
