import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: brad
 * Date: 4/22/13
 * Time: 12:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class LibraryLoader {

    public static void loadNativeLibraries() throws Exception {
        if ((System.getProperty("os.name")).equals("Mac OS X")) {
            addLibraryPath("natives/macosx");
        } else if ((System.getProperty("os.name")).equals("Linux")) {
            addLibraryPath("natives/linux");
        } else {
            addLibraryPath("natives/windows");
            if (System.getProperty("os.arch").equals("amd64") || System.getProperty("os.arch").equals("x86_64")) {
                System.loadLibrary("OpenAL64");
            } else {
                System.loadLibrary("OpenAL32");
            }
        }

    }

    public static void addLibraryPath(String s) throws Exception {
        final Field usr_paths_field = ClassLoader.class.getDeclaredField("usr_paths");
        usr_paths_field.setAccessible(true);

        final String[] paths = (String[]) usr_paths_field.get(null);

        for (String path : paths) {
            if (path.equals(s)) {
                return;
            }
        }

        final String[] new_paths = Arrays.copyOf(paths, paths.length + 1);
        new_paths[paths.length - 1] = s;
        usr_paths_field.set(null, new_paths);
    }

}
