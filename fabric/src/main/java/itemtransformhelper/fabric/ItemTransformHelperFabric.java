package itemtransformhelper.fabric;

import itemtransformhelper.ItemTransformHelper;
import itemtransformhelper.StartupClientOnly;
import net.fabricmc.api.ClientModInitializer;

public class ItemTransformHelperFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ItemTransformHelper.init();
        StartupClientOnly.clientSetup();
    }

}
