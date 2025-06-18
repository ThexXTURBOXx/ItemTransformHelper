package itemtransformhelper.fabric.mixin;

import itemtransformhelper.UpdateLink;
import java.util.Objects;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModelResolver.class)
public class ItemModelResolverMixin {

    @Inject(method = "appendItemLayers", at = @At(value = "HEAD"))
    public void appendItemLayers(ItemStackRenderState itemStackRenderState, ItemStack itemStack,
                                 ItemDisplayContext itemDisplayContext, Level level, LivingEntity livingEntity, int i,
                                 CallbackInfo ci) {
        UpdateLink link = UpdateLink.INSTANCE;
        link.isRenderingHeldItem = Objects.equals(itemStack, link.heldItemStack);
        if (link.isRenderingHeldItem)
            itemStackRenderState.appendModelIdentityElement(link.forcedTransforms);
    }

}
