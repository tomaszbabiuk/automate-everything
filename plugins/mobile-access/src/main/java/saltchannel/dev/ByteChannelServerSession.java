package saltchannel.dev;

import saltchannel.BadPeer;
import saltchannel.ByteChannel;

/**
 * A client-server communication session.
 * 
 * @author Frans Lundberg
 */
public interface ByteChannelServerSession {
    
    /**
     * Runs the client-server session using the provided
     * ByteChannel to communicate with the client.
     * 
     * @throws saltchannel.BadPeer
     *          If the peer does not follow protocol.
     */
    public void runSession(ByteChannel channel);
}
