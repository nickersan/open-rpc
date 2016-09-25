package com.tn.openrpc.core.channel;

import java.io.Serializable;

/**
 * <code>RemoteChannelResponse</code> represents a response received from a <code>RemoteChannel</code>.
 */
public class RemoteChannelResponse implements Serializable {

    private Object result;

    /**
     * Creates a new <code>RemoteChannelResponse</code> initialized with the <code>result</code>.
     */
    public RemoteChannelResponse(Object result) {

        this.result = result;
    }

    /**
     * Returns the result.
     */
    public Object getResult() {

        return result;
    }
}
