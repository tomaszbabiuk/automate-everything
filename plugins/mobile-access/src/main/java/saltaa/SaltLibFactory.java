package saltaa;

/**
 * Creates SaltLib instances.
 * One instance of each library is created when first requested (lazily).
 * 
 * @author Frans Lundberg, truncated by Tomasz Babiuk (see original file in: https://github.com/assaabloy-ppi/salt-channel/blob/master/src-in/saltaa/SaltLibFactory.java)
 */
public class SaltLibFactory {
    private static final LibHolder javaLib = new LibHolder();

    public static SaltLib getLib() {
        initJava();
        return javaLib.lib;
    }

    private static void initJava() {
        if (javaLib.status == LibStatus.NOT_INITIATED) {
            javaLib.lib = new JavaSaltLib();
            javaLib.status = LibStatus.OK;
        }
    }

    private enum LibStatus {NOT_INITIATED, OK }

    /**
     * Lib reference and its status.
     */
    private static class LibHolder {
        LibStatus status;
        SaltLib lib;
        
        LibHolder() {
            status = LibStatus.NOT_INITIATED;
        }
    }
}
