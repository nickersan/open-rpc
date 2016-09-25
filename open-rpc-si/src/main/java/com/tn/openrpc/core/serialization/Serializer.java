package com.tn.openrpc.core.serialization;

/**
 * The interface implemented by an object that serializes and de-serializes other <code>Object</code>s.
 */
public interface Serializer<T> {

    /**
     * Serializes the <code>subject</code>.
     */
    public T serialize(Object subject) throws SerializationException;

    /**
     * De-serializes the <code>source</code>.
     */
    public <S> S deserialize(T source) throws SerializationException;
}
