package itemtransformhelper;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

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
                .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, ID)))
                .stacksTo(1).arch$tab(StartupCommon.ITH_ITEM_GROUP));
    }

}
