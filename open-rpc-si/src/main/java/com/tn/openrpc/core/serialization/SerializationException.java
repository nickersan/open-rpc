package com.tn.openrpc.core.serialization;

/**
 * A <code>SerializationException</code> is thrown when an error occurs in a <code>Serializer</code>.
 *
 * @see Serializer
 */
public class SerializationException extends Exception {

    /**
     * Creates a new <code>SerializationException</code> initialized with the <code>message</code>.
     */
    public SerializationException(String message) {

        super(message);
    }

    /**
     * Creates a new <code>SerializationException</code> initialized with the <code>message</code> and
     * <code>cause</code>.
     */
    public SerializationException(String message, Throwable cause) {

        super(message, cause);
    }
}
