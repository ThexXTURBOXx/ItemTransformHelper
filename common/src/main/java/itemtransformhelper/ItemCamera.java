package itemtransformhelper;

import java.util.function.Consumer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import static itemtransformhelper.ItemTransformHelper.MOD_ID;

/**
 * User: The Grey Ghost
 * Date: 26/01/2015
 * ItemCamera is very simple item used to activate the ItemCameraTransforms override when it is held in the hotbar.
 * See the Notes.
 */
public class ItemCamera extends Item {

    public static final String ID = "item_camera";

    public ItemCamera() {
        super(new Properties()
                .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, ID)))
                .stacksTo(1).arch$tab(StartupCommon.ITH_ITEM_GROUP));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, TooltipDisplay tooltipDisplay,
                                Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        consumer.accept(Component.literal("1) Place the camera in your hotbar"));
        consumer.accept(Component.literal("2) Hold an item in your hand"));
        consumer.accept(Component.literal("3) Use the cursor keys to"));
        consumer.accept(Component.literal("   modify the item transform."));
    }

}
