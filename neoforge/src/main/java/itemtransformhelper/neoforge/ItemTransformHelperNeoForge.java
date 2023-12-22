package itemtransformhelper.neoforge;

import itemtransformhelper.ItemTransformHelper;
import itemtransformhelper.StartupClientOnly;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import static itemtransformhelper.ItemTransformHelper.MOD_ID;

@Mod(MOD_ID)
public class ItemTransformHelperNeoForge {

    static IEventBus modEventBus;

    public ItemTransformHelperNeoForge(IEventBus modEventBus) {
        ItemTransformHelperNeoForge.modEventBus = modEventBus;

        // Register the doClientStuff method for modloading
        modEventBus.addListener(this::clientSetup);

        ItemTransformHelper.init();
    }

    public void clientSetup(FMLClientSetupEvent event) {
        StartupClientOnly.clientSetup();
    }

}
