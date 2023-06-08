package itemtransformhelper;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static itemtransformhelper.ItemTransformHelper.MODID;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 * <p>
 * The Startup classes for this example are called during startup, in the following order:
 * preInitCommon
 * preInitClientOnly
 * initCommon
 * initClientOnly
 * postInitCommon
 * postInitClientOnly
 * See MinecraftByExample class for more information
 */
public class StartupCommon {

    // Registries
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(MODID, Registries.ITEM);
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(MODID, Registries.CREATIVE_MODE_TAB);

    // Items
    public static final RegistrySupplier<ItemCamera> ITEM_CAMERA = ITEMS.register("item_camera", ItemCamera::new);

    // Item Groups
    public static final RegistrySupplier<CreativeModeTab> ITH_ITEM_GROUP = TABS.register("items",
            () -> CreativeTabRegistry.create(Component.translatable("itemGroup." + MODID + ".items"),
                    () -> new ItemStack(ITEM_CAMERA.get())));

    public static void init() {
        ITEMS.register();
        TABS.register();
    }

}
