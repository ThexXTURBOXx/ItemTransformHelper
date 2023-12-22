package itemtransformhelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ItemTransformHelper {

    public static final String MOD_ID = "itemtransformhelper";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {
        StartupCommon.init();
    }

}
