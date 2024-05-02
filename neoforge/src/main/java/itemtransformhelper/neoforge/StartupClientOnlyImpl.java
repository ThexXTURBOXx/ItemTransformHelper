package itemtransformhelper.neoforge;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

import static itemtransformhelper.StartupClientOnly.modelBakeEventHandler;
import static itemtransformhelper.neoforge.ItemTransformHelperNeoForge.modEventBus;

public class StartupClientOnlyImpl {

    public static void platformClientSetup() {
        modEventBus.register(new ModBusListeners());
    }

    public static class ModBusListeners {
        @SubscribeEvent
        public void modelBakeEvent(ModelEvent.BakingCompleted event) {
            modelBakeEventHandler.modelBakeEvent(event.getModelBakery().getBakedTopLevelModels());
        }
    }

}
