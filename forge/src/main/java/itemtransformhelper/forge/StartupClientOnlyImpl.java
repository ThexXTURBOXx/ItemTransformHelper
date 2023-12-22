package itemtransformhelper.forge;

import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static itemtransformhelper.StartupClientOnly.clientTickHandler;
import static itemtransformhelper.StartupClientOnly.modelBakeEventHandler;

public class StartupClientOnlyImpl {

    public static void clientSetup() {
        FMLJavaModLoadingContext.get().getModEventBus().register(new ModBusListeners());
        MinecraftForge.EVENT_BUS.register(new ForgeEventListeners());
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
