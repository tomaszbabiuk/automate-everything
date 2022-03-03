package saltchannel;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A two-way, reliable communication channel.
 * Byte arrays can be read and written; a simple blocking model.
 * 
 * Concurrency note: an implementation of this interface must handle that 
 * one thread calls read() while another one calls write(). However, 
 * multiple threads calling read() concurrently, or multiple threads calling
 * write() concurrently *cannot* be assumed to work. The consumer of this
 * interface should in such cases ensure that only one thread calls read() at
 * time, and only one thread calls write() at a time.
 * 
 * @author Frans Lundberg
 */
public interface ByteChannel {
    
    /**
     * Reads one message; blocks until one is available.
     * 
     * @throws saltchannel.ComException
     *          If there is an IO error or data format error in 
     *          the underlying layer.
     */
    public byte[] read(String debugMessage) throws ComException;

    /**
     * Writes a sequence of non-last application messages to the byte channel.
     * To write the last messages of the session, use write(isLast, messages) instead.
     * NOTE: consider making this method deprecated and removing it in
     * the long term.
     * 
     * @param messages
     *          The non-last messages to write.
     * @throws saltchannel.ComException
     *          Thrown if there is a communication error.
     * @deprecated Starting 2017-10-03.
     */
    @Deprecated
    public void write(byte[]... messages) throws ComException;
   
    /**
     * Writes application messages to the channel.
     * 
     * @param isLast
     *     Must be set to true if messages[messages.length-1] is the
     *     last application message of the session. Otherwise, it must be false.
     * @throws saltchannel.ComException
     *     If there in an error in the underlying layer communication layer.
     */
    public void write(boolean isLast, byte[]... messages);
}
