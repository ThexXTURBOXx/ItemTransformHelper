package itemtransformhelper.neoforge;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent;

import static itemtransformhelper.StartupClientOnly.clientTickHandler;
import static itemtransformhelper.StartupClientOnly.modelBakeEventHandler;

public class StartupClientOnlyImpl {

    public static void clientSetup() {
        StartupClientOnlyImpl instance = new StartupClientOnlyImpl();
        ItemTransformHelperForge.modEventBus.addListener(instance::modelBakeEvent);
        NeoForge.EVENT_BUS.register(new ClientTickListener());
    }

    @SubscribeEvent
    public void modelBakeEvent(ModelEvent.BakingCompleted event) {
        modelBakeEventHandler.modelBakeEvent(event.getModelBakery().getBakedTopLevelModels());
    }

    private static class ClientTickListener {
        @SubscribeEvent
        public void clientTickEvent(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.START) {
                clientTickHandler.clientTickEvent();
            }
        }
    }

}
