package saltchannel.v2;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import saltaa.BadEncryptedDataException;
import saltaa.SaltLib;
import saltaa.SaltLibFactory;
import saltchannel.BadPeer;
import saltchannel.ByteChannel;
import saltchannel.ComException;
import saltchannel.util.Bytes;
import saltchannel.v2.packets.EncryptedMessage;
import saltchannel.v2.packets.TTPacket;

/**
 * An implementation of an encrypted channel using a shared symmetric 
 * session key.
 * The read/write methods throws ComException for low-level IO errors
 * and BadPeer if the data format is not OK including cases when the data
 * is not encrypted correctly (when authentication of encrypted data fails).
 * 
 * @author Frans Lundberg
 */
public class EncryptedChannelV2 implements ByteChannel {
    private long readNonceInteger;
    private byte[] readNonceBytes = new byte[SaltLib.crypto_box_NONCEBYTES];
    private long writeNonceInteger;
    private byte[] writeNonceBytes = new byte[SaltLib.crypto_box_NONCEBYTES];
    private byte[] key;
    private final ByteChannel channel;
    private byte[] pushbackMessage;
    private byte[] sessionNonce;
    private EncryptedMessage lastReadEncryptedPacket = null;    
    private SaltLib salt = SaltLibFactory.getLib();

    /**
     * Creates a new EncryptedChannel given the underlying channel to be 
     * encrypted, the key and the role of the peer (client or server).
     * 
     * @param key  
     *      Shared symmetric encryption key for one session. 
     *      A new key must be used for every session.
     */
    public EncryptedChannelV2(ByteChannel channel, byte[] key, Role role) {
        this(channel, key, role, zeroSessionNonce());
    }
    
    private static byte[] zeroSessionNonce() {
        return new byte[TTPacket.SESSION_NONCE_SIZE];
    }
    
    public EncryptedChannelV2(ByteChannel channel, byte[] key, Role role, byte[] sessionNonce) {
        if (key.length != SaltLib.crypto_box_SECRETKEYBYTES) {
            throw new IllegalArgumentException("bad key size, should be " + SaltLib.crypto_box_SECRETKEYBYTES);
        }
        
        this.channel = channel;
        this.key = key;
        this.sessionNonce = sessionNonce;
        
        switch (role) {
        case CLIENT:
            setWriteNonce(1);
            setReadNonce(2);
            break;
        case SERVER:
            setWriteNonce(2);
            setReadNonce(1);
            break;
        default:
            throw new Error("never happens");
        }
    }
    
    /**
     * Role of this peer of the encrypted channel.
     * Used for nonce handling.
     */
    public static enum Role {
        CLIENT, SERVER
    }
    
    public void pushback(byte[] pushbackMessage) {
        this.pushbackMessage = pushbackMessage;
    }

    @Override
    public byte[] read(String debugMessage) throws ComException, BadPeer {
        byte[] message = readOrTakePushback(debugMessage);
        this.lastReadEncryptedPacket = unwrap(message);
        byte[] encrypted = lastReadEncryptedPacket.body;
        byte[] clear = decrypt(encrypted);
        
        increaseReadNonce();
        return clear;
    }
    
    /**
     * Returns the lastFlag of the last read packet.
     */
    public boolean lastFlag() {
        return lastReadEncryptedPacket == null ? false : lastReadEncryptedPacket.lastFlag();
    }
    
    private byte[] readOrTakePushback(String debugMessage) {
        byte[] bytes;
        
        if (this.pushbackMessage != null) {
            bytes = this.pushbackMessage;
            this.pushbackMessage = null;
        } else {
            bytes = channel.read(debugMessage);
        }
        
        return bytes;
    }
    
    /**
     * @deprecated
     */
    @Override
    @Deprecated
    public void write(byte[]...messages) throws ComException {
        write(false, messages);
    }

    /**
     * Takes cleartext messages, encrypts them, and writes them to underlying
     * channel.
     */
    @Override
    public void write(boolean isLast, byte[]... messages) throws ComException, BadPeer {
        byte[][] toWrite = new byte[messages.length][];
        
        for (int i = 0; i < messages.length; i++) {
            byte[] encrypted = encrypt(messages[i]);
            toWrite[i] = wrap(isLast && i == messages.length - 1, encrypted);
            increaseWriteNonce();
        }
        
        channel.write(isLast, toWrite);
    }
    
    /**
     * @throws saltchannel.ComException
     * @throws saltchannel.BadPeer
     */
    byte[] decrypt(byte[] encrypted) {
        if (encrypted == null) {
            throw new Error("encrypted == null");
        }
        
        byte[] clear;
        byte[] c = new byte[SaltLib.crypto_secretbox_OVERHEAD_BYTES + encrypted.length];
        byte[] m = new byte[c.length];
        System.arraycopy(encrypted, 0, c, SaltLib.crypto_secretbox_OVERHEAD_BYTES, encrypted.length);
        if (c.length < 32) {
            throw new BadPeer("ciphertext too small");
        }
        
        try {
            salt.crypto_box_open_afternm(m, c, readNonceBytes, key);            
        } catch(BadEncryptedDataException e) {
            throw new BadPeer("invalid encryption, could not be decrypted");
        }

        clear = Arrays.copyOfRange(m, SaltLib.crypto_secretbox_INTERNAL_OVERHEAD_BYTES, m.length);
        return clear;
    }
    
    /**
     * Needed by ServerChannelV2.
     */
    byte[] encryptAndIncreaseWriteNonce(boolean isLast, byte[] bytes) {
        byte[] encrypted = wrap(isLast, encrypt(bytes));
        increaseWriteNonce();
        return encrypted;
    }
    
    byte[] encrypt(byte[] clear) {
        byte[] m = new byte[SaltLib.crypto_secretbox_INTERNAL_OVERHEAD_BYTES + clear.length];
        byte[] c = new byte[m.length];
        System.arraycopy(clear, 0, m, SaltLib.crypto_secretbox_INTERNAL_OVERHEAD_BYTES, clear.length);        
        salt.crypto_box_afternm(c, m, writeNonceBytes, key);
        return Arrays.copyOfRange(c, SaltLib.crypto_secretbox_OVERHEAD_BYTES, c.length);
    }
    
    private void setWriteNonce(long nonceInteger) {
        this.writeNonceInteger = nonceInteger;
        updateWriteNonceBytes();
    }
    
    /**
     * Not private intentionally. Used by ServerChannel.
     */
    void increaseWriteNonce() {
	// Since we will never in practice have overflow of writeNonceInteget an Error is thrown.
	if (writeNonceInteger > Long.MAX_VALUE - 2) {
	    throw new Error("writeNonce too big");
	}
	
        setWriteNonce(writeNonceInteger + 2);
    }
    
    private void setReadNonce(long nonceInteger) {
        this.readNonceInteger = nonceInteger;
        updateReadNonceBytes();
    }
    
    private void increaseReadNonce() {
	// Since we will never in practice have overflow of readNonceInteger an Error is thrown.
	
	if (readNonceInteger > Long.MAX_VALUE - 2) {
	    throw new Error("readNonce too big");
	}
	
        setReadNonce(readNonceInteger + 2);
    }
    
    private void updateReadNonceBytes() {
        Bytes.longToBytesLE(readNonceInteger, readNonceBytes, 0);
        System.arraycopy(sessionNonce, 0, readNonceBytes, 8, TTPacket.SESSION_NONCE_SIZE);
    }
    
    private void updateWriteNonceBytes() {
        Bytes.longToBytesLE(writeNonceInteger, writeNonceBytes, 0);
        System.arraycopy(sessionNonce, 0, writeNonceBytes, 8, TTPacket.SESSION_NONCE_SIZE);
    }
    
    /**
     * Wrap encrypted bytes in EncryptedPacket.
     */
    static byte[] wrap(boolean isLast, byte[] bytes) {
        EncryptedMessage p = new EncryptedMessage();
        p.body = bytes;
        p.lastFlag = isLast;
        byte[] result = new byte[p.getSize()];
        p.toBytes(result, 0);
        return result;
    }
    
    static byte[] unwrapToBytes(byte[] packetBytes) {
        EncryptedMessage p = unwrap(packetBytes);
        return p.body;
    }
    
    static EncryptedMessage unwrap(byte[] packetBytes) {
        return EncryptedMessage.fromBytes(packetBytes, 0, packetBytes.length);
    }
}
