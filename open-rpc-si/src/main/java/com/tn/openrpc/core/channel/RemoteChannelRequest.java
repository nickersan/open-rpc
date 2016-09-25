package com.tn.openrpc.core.channel;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * <code>RemoteChannelRequest</code> represents a request sent over a <code>RemoteChannel</code>.
 */
public class RemoteChannelRequest implements Serializable
{
  private List<Object> args;
  private String target;

  /**
   * Creates a new <code>RemoteChannelRequest</code>.
   *
   * @param target the target of the request.
   * @param args the arguments.
   */
  public RemoteChannelRequest(String target, Object... args)
  {
    this.target = target;
    this.args = Arrays.asList(args);
  }

  /**
   * Returns the args.
   */
  public List<Object> getArgs()
  {
    return args;
  }

  /**
   * Returns the target.
   */
  public String getTarget()
  {
    return target;
  }

  /**
   * Compares this object to another for equality.  For another object to be considered equal to a
   * <code>RemoteChannelRequest</code> the other object must be a <code>RemoteChannelRequest</code> and must have been
   * initialized with the same values.
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
        .append(getTarget(), ((RemoteChannelRequest) other).getTarget())
        .append(getArgs(), ((RemoteChannelRequest) other).getArgs())
        .isEquals();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder()
      .append(getTarget())
      .append(getArgs())
      .toHashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("target", getTarget())
      .append("args", getArgs())
      .toString();
  }
}
