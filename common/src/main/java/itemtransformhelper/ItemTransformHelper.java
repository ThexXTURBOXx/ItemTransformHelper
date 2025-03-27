package itemtransformhelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ItemTransformHelper {

    public static final String MOD_ID = "itemtransformhelper";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static volatile boolean warned = false;

    public static void init() {
        StartupCommon.init();
        warnClient();
    }

    public static void warnClient() {
        if (!warned) {
            warned = true;
            ItemTransformHelper.LOGGER.warn("Warning - The Item Transform Helper hijacks certain functions that "
                                            + "reference ItemTransforms.");
            ItemTransformHelper.LOGGER.warn("  I am not yet aware of any problems regarding this, but here is your "
                                            + "warning anyway!");
            ItemTransformHelper.LOGGER.warn("  I recommend you disable the mod when you're not actively using it to "
                                            + "transform your items.");
        }
    }

}
