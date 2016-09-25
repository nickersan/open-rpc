package com.tn.openrpc.jms;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.tn.openrpc.core.channel.RemoteChannelResponse;

/**
 * A wrapper class for a <code>RemoteChannelResponse</code> that introduces a <i>correlation id</i>.
 */
public class JmsRemoteChannelResponse implements Serializable {

  private Object correlationId;
  private RemoteChannelResponse remoteChannelResponse;

  /**
   * Creates a new <code>JmsRemoteChannelResponse</code> initialized with the <code>correlationId</code> and
   * <code>remoteChannelResponse</code>.
   */
  public JmsRemoteChannelResponse(Object correlationId, RemoteChannelResponse remoteChannelResponse)
  {
    this.correlationId = correlationId;
    this.remoteChannelResponse = remoteChannelResponse;
  }

  /**
   * Returns the correlation id.
   */
  public Object getCorrelationId()
  {
    return correlationId;
  }

  /**
   * Returns the remote channel response.
   */
  public RemoteChannelResponse getRemoteChannelResponse()
  {
    return remoteChannelResponse;
  }

  /**
   * Compares this object to another for equality.  For another object to be considered equal to a
   * <code>JmsRemoteChannelResponse</code> the other object must be a <code>JmsRemoteChannelResponse</code> and must
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
        .append(getCorrelationId(), ((JmsRemoteChannelResponse)other).getCorrelationId())
        .append(getRemoteChannelResponse(), ((JmsRemoteChannelResponse)other).getRemoteChannelResponse())
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
      .append(getRemoteChannelResponse())
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
      .append("remoteChannelResponse", getRemoteChannelResponse())
      .toString();
  }
}
