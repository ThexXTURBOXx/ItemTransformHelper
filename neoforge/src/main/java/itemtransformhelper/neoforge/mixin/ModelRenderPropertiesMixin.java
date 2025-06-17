package itemtransformhelper.neoforge.mixin;

import itemtransformhelper.UpdateLink;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ModelRenderProperties.class)
public class ModelRenderPropertiesMixin {

    @Redirect(method = "applyToLayer", at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/renderer/item/ModelRenderProperties;"
                     + "transforms:Lnet/minecraft/client/renderer/block/model/ItemTransforms;"))
    public ItemTransforms getTopTransforms(ModelRenderProperties instance) {
        UpdateLink link = UpdateLink.INSTANCE;
        if (link.foundCamera && link.isRenderingHeldItem) {
            link.originalTransforms = instance.transforms();
            return link.forcedTransforms;
        }
        return instance.transforms();
    }

}
