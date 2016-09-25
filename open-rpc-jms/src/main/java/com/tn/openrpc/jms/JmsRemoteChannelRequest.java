package com.tn.openrpc.jms;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.tn.openrpc.core.channel.RemoteChannelRequest;

/**
 * A wrapper class for a <code>RemoteChannelRequest</code> that introduces a <i>correlation id</i>.
 */
public class JmsRemoteChannelRequest implements Serializable
{
  private Object correlationId;
  private RemoteChannelRequest remoteChannelRequest;

  /**
   * Creates a new <code>JmsRemoteChannelRequest</code> initialized with the <code>correlationId</code> and
   * <code>remoteChannelRequest</code>.
   */
  public JmsRemoteChannelRequest(Object correlationId, RemoteChannelRequest remoteChannelRequest)
  {
    this.correlationId = correlationId;
    this.remoteChannelRequest = remoteChannelRequest;
  }

  /**
   * Returns the correlation id.
   */
  public Object getCorrelationId()
  {
    return correlationId;
  }

  /**
   * Returns the remote channel request.
   */
  public RemoteChannelRequest getRemoteChannelRequest()
  {
    return remoteChannelRequest;
  }

  /**
   * Compares this object to another for equality.  For another object to be considered equal to a
   * <code>JmsRemoteChannelRequest</code> the other object must be a <code>JmsRemoteChannelRequest</code> and must
   * have been initialized with the same values.
   *
   * @param other the other object.
   *
   * @return <code>true</code> if the objects are considered equal, otherwise <code>false</code>.
   */
  @Override
  public boolean equals(Object other)
  {
    return this == other || other != null && getClass() == other.getClass() &&
      new EqualsBuilder()
        .append(getCorrelationId(), ((JmsRemoteChannelRequest) other).getCorrelationId())
        .append(getRemoteChannelRequest(), ((JmsRemoteChannelRequest) other).getRemoteChannelRequest())
        .isEquals();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder()
      .append(getCorrelationId())
      .append(getRemoteChannelRequest())
      .toHashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("correlationId", getCorrelationId())
      .append("remoteChannelRequest", getRemoteChannelRequest())
      .toString();
  }
}
