package itemtransformhelper;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

/**
 * User: The Grey Ghost
 * Date: 26/01/2015
 * ItemCamera is very simple item used to activate the ItemCameraTransforms override when it is held in the hotbar.
 * See the Notes.
 */
public class ItemCamera extends Item {

    public ItemCamera() {
        super(new Properties().stacksTo(1).arch$tab(StartupCommon.ITH_ITEM_GROUP));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list,
                                TooltipFlag tooltipFlag) {
        list.add(Component.literal("1) Place the camera in your hotbar"));
        list.add(Component.literal("2) Hold an item in your hand"));
        list.add(Component.literal("3) Use the cursor keys to"));
        list.add(Component.literal("   modify the item transform."));
    }

}
