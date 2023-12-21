package itemtransformhelper.neoforge;

import itemtransformhelper.ItemTransformHelper;
import itemtransformhelper.StartupClientOnly;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import static itemtransformhelper.ItemTransformHelper.MODID;

@Mod(MODID)
public class ItemTransformHelperForge {

    static IEventBus modEventBus;

    public ItemTransformHelperForge(IEventBus modEventBus) {
        ItemTransformHelperForge.modEventBus = modEventBus;

        // Register the setup method for modloading
        modEventBus.addListener(this::setup);

        // Register the doClientStuff method for modloading
        modEventBus.addListener(this::clientSetup);

        ItemTransformHelper.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        // nothing left to do here with item reg moved to deferred reg
    }

    public void clientSetup(final FMLClientSetupEvent event) {
        StartupClientOnly.clientSetup();
    }

}