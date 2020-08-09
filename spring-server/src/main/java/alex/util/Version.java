package alex.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Alex.Sun
 * @created 2020-05-09 10:11
 */
public final class Version {

    public static String getVersion() {
        return StringUtils.defaultString(Version.class.getPackage().getImplementationVersion(), "-");
    }

}
