package itemtransformhelper.neoforge;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent;

import static itemtransformhelper.StartupClientOnly.clientTickHandler;
import static itemtransformhelper.StartupClientOnly.modelBakeEventHandler;
import static itemtransformhelper.neoforge.ItemTransformHelperNeoForge.modEventBus;

public class StartupClientOnlyImpl {

    public static void clientSetup() {
        modEventBus.register(new ModBusListeners());
        NeoForge.EVENT_BUS.register(new ForgeEventListeners());
    }

    public static class ModBusListeners {
        @SubscribeEvent
        public void modelBakeEvent(ModelEvent.BakingCompleted event) {
            modelBakeEventHandler.modelBakeEvent(event.getModelBakery().getBakedTopLevelModels());
        }
    }

    public static class ForgeEventListeners {
        @SubscribeEvent
        public void clientTickEvent(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.START) {
                clientTickHandler.clientTickEvent();
            }
        }
    }

}
