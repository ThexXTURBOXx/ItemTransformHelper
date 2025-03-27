package itemtransformhelper.fabric.mixin;

import itemtransformhelper.UpdateLink;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ModelRenderProperties.class)
public class ModelRenderPropertiesMixin {

    @Redirect(method = "applyToLayer", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/block/model/ItemTransforms;getTransform"
                     + "(Lnet/minecraft/world/item/ItemDisplayContext;)"
                     + "Lnet/minecraft/client/renderer/block/model/ItemTransform;"))
    public ItemTransform getTopTransforms(ItemTransforms instance, ItemDisplayContext itemDisplayContext) {
        UpdateLink link = UpdateLink.INSTANCE;
        if (link.foundCamera && link.isRenderingHeldItem) {
            link.originalTransforms = instance;
            return link.forcedTransforms.getTransform(itemDisplayContext);
        }
        return instance.getTransform(itemDisplayContext);
    }

}
