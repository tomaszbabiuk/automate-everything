package saltchannel.dev;

import java.util.ArrayList;
import java.util.List;
import saltchannel.ByteChannel;
import saltchannel.ComException;
import saltchannel.util.Hex;

/**
 * Decorator for ByteChannel, stores read and written packages.
 */
public class LoggingByteChannel implements ByteChannel {
    private ByteChannel inner;
    private ArrayList<Entry> log;

    public LoggingByteChannel(ByteChannel channel) {
        this.log = new ArrayList<Entry>();
        this.inner = channel;
    }
    
    public List<Entry> getLog() {
        return log;
    }
    
    public static enum ReadOrWrite {
        READ, WRITE, WRITE_WITH_PREVIOUS
    }
    
    public static class Entry {
        /** Time in nanos (from System.nanoTime()). */
        public long time;
        
        public ReadOrWrite type;
        
        public boolean isLast;
        
        public byte[] bytes;
        
        public String toString() {
            StringBuilder b = new StringBuilder();
            b.append(type.name() + ", " + time + "\n");
            b.append("    " + bytes.length + ", " + Hex.create(bytes));
            return b.toString();
        }
    }

    @Override
    public byte[] read() throws ComException {
        byte[] result = inner.read();
        
        Entry entry = new Entry();
        entry.time = System.nanoTime();
        entry.type = ReadOrWrite.READ;
        entry.bytes = result;
        log.add(entry);
        
        return result;
    }
    
    @Override
    public void write(byte[]... messages) throws ComException {
        write(false, messages);
    }

    @Override
    public void write(boolean isLast, byte[]... messages) throws ComException {
        inner.write(isLast, messages);
        
        long time = System.nanoTime();
        
        for (int i = 0; i < messages.length; i++) {
            Entry entry = new Entry();
            entry.time = time;
            entry.type = i == 0 ? ReadOrWrite.WRITE : ReadOrWrite.WRITE_WITH_PREVIOUS;
            entry.bytes = messages[i];
            entry.isLast = i == messages.length - 1 ? isLast : false;
            log.add(entry);
        }
    }
}