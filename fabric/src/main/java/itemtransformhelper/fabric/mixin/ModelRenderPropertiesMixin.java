package itemtransformhelper.fabric.mixin;

import itemtransformhelper.UpdateLink;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ModelRenderProperties.class)
public class ModelRenderPropertiesMixin {

    @Redirect(method = "applyToLayer", at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/renderer/item/ModelRenderProperties;"
                     + "transforms:Lnet/minecraft/client/resources/model/cuboid/ItemTransforms;",
            opcode = Opcodes.GETFIELD))
    public ItemTransforms getTopTransforms(ModelRenderProperties instance) {
        UpdateLink link = UpdateLink.INSTANCE;
        if (link.foundCamera && link.isRenderingHeldItem) {
            link.originalTransforms = instance.transforms();
            return link.forcedTransforms;
        }
        return instance.transforms();
    }

}
