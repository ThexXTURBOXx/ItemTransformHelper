package itemtransformhelper;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.client.ClientTooltipEvent;
import net.minecraft.network.chat.Component;

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
public class StartupClientOnly {

    public static final ClientTickHandler clientTickHandler = new ClientTickHandler();
    public static final MenuItemCameraTransforms menuItemCameraTransforms = new MenuItemCameraTransforms();

    public static void clientSetup() {
        ClientTickEvent.CLIENT_PRE.register(mc -> clientTickHandler.clientTickEvent());

        ClientTooltipEvent.ITEM.register((stack, lines, tooltipContext, flag) -> {
            if (stack.is(StartupCommon.ITEM_CAMERA.get())) {
                lines.addLast(Component.literal("1) Place the camera in your hotbar"));
                lines.addLast(Component.literal("2) Hold an item in your hand"));
                lines.addLast(Component.literal("3) Use the cursor keys to"));
                lines.addLast(Component.literal("   modify the item transform."));
            }
        });
    }

}
