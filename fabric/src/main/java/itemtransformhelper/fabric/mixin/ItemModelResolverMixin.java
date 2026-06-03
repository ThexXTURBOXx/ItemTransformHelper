package itemtransformhelper.fabric.mixin;

import itemtransformhelper.UpdateLink;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.ItemOwner;
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
    public void appendItemLayers(ItemStackRenderState output, ItemStack item,
                                 ItemDisplayContext displayContext, Level level, ItemOwner owner, int seed,
                                 CallbackInfo ci) {
        UpdateLink link = UpdateLink.INSTANCE;
        link.isRenderingHeldItem = link.heldItemStack != null &&
                                   ItemStack.isSameItem(item, link.heldItemStack);
        if (link.isRenderingHeldItem)
            output.appendModelIdentityElement(link.forcedTransforms);
    }

}
