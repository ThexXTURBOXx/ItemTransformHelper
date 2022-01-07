package itemtransformhelper;

import dev.architectury.injectables.annotations.ExpectPlatform;
import java.util.Locale;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;


/**
 * The menu used to select and alter the different parts of the ItemCameraTransform for the currently selected item.
 * The menu state is rendered on the screen by HUDTextRenderer.
 * The class registers its components on the Forge and FML event buses.
 * Created by TheGreyGhost on 22/01/15.
 */
public class MenuItemCameraTransforms {

    private static final Logger LOGGER = LogManager.getLogger();

    private final HUDTextRenderer.HUDInfoUpdateLink linkToHudRenderer;

    public MenuItemCameraTransforms() {
        linkToHudRenderer = new HUDTextRenderer.HUDInfoUpdateLink();
        registerListeners(new HUDTextRenderer(linkToHudRenderer),
                new MenuKeyHandler(this.new KeyPressCallback()));
    }

    @ExpectPlatform
    public static void registerListeners(HUDTextRenderer hudTextRenderer, MenuKeyHandler menuKeyHandler) {
        throw new UnsupportedOperationException();
    }

    /**
     * get the current ItemCameraTransforms
     *
     * @return the transform
     */
    public ModelTransformation getItemCameraTransforms() {
        return linkToHudRenderer.itemCameraTransforms;
    }

    /**
     * turn menu on or off
     *
     * @param visible true = make visible
     */
    public void changeMenuVisible(boolean visible) {
        linkToHudRenderer.menuVisible = visible;
    }

    public class KeyPressCallback {

        void keyPressed(MenuKeyHandler.ArrowKeys whichKey) {
            if (!linkToHudRenderer.menuVisible) return;

            switch (whichKey) {
            case DOWN:
                linkToHudRenderer.selectedField = linkToHudRenderer.selectedField.getNextField();
                break;
            case UP:
                linkToHudRenderer.selectedField = linkToHudRenderer.selectedField.getPreviousField();
                break;
            case RIGHT:
            case LEFT:
                alterField(whichKey == MenuKeyHandler.ArrowKeys.RIGHT);
                break;
            default:
                break;
            }
        }

    }

    private void alterField(boolean increase) {
        Transformation transformVec3f = getItemTransformRef(linkToHudRenderer, linkToHudRenderer.selectedTransform);
        if (transformVec3f == null) return; // should never happen

        final float SCALE_INCREMENT = 0.01F;
        final float ROTATION_INCREMENT = 2F;
        final float TRANSLATION_INCREMENT = 0.25F * 0.0625F; // 1/4 of a block, with multiplier from
        // Transformation.Deserializer::deserialize
        switch (linkToHudRenderer.selectedField) {
        case TRANSFORM:
            linkToHudRenderer.selectedTransform = increase ? linkToHudRenderer.selectedTransform.getNext()
                    : linkToHudRenderer.selectedTransform.getPrevious();
            break;
        case SCALE_X:
            transformVec3f.scale.add(increase ? SCALE_INCREMENT : -SCALE_INCREMENT, 0, 0);
            break;
        case SCALE_Y:
            transformVec3f.scale.add(0, increase ? SCALE_INCREMENT : -SCALE_INCREMENT, 0);
            break;
        case SCALE_Z:
            transformVec3f.scale.add(0, 0, increase ? SCALE_INCREMENT : -SCALE_INCREMENT);
            break;
        case ROTATE_X:
            float newAngle = transformVec3f.rotation.getX() + (increase ? ROTATION_INCREMENT : -ROTATION_INCREMENT);
            newAngle = MathHelper.wrapDegrees(newAngle - 180) + 180;
            transformVec3f.rotation.set(newAngle, transformVec3f.rotation.getY(), transformVec3f.rotation.getZ());
            break;
        case ROTATE_Y:
            newAngle = transformVec3f.rotation.getY() + (increase ? ROTATION_INCREMENT : -ROTATION_INCREMENT);
            newAngle = MathHelper.wrapDegrees(newAngle - 180) + 180;
            transformVec3f.rotation.set(transformVec3f.rotation.getX(), newAngle, transformVec3f.rotation.getZ());
            break;
        case ROTATE_Z:
            newAngle = transformVec3f.rotation.getZ() + (increase ? ROTATION_INCREMENT : -ROTATION_INCREMENT);
            newAngle = MathHelper.wrapDegrees(newAngle - 180) + 180;
            transformVec3f.rotation.set(transformVec3f.rotation.getX(), transformVec3f.rotation.getY(), newAngle);
            break;
        case TRANSLATE_X:
            transformVec3f.translation
                    .add(increase ? TRANSLATION_INCREMENT : -TRANSLATION_INCREMENT, 0, 0);
            break;
        case TRANSLATE_Y:
            transformVec3f.translation
                    .add(0, increase ? TRANSLATION_INCREMENT : -TRANSLATION_INCREMENT, 0);
            break;
        case TRANSLATE_Z:
            transformVec3f.translation
                    .add(0, 0, increase ? TRANSLATION_INCREMENT : -TRANSLATION_INCREMENT);
            break;
        case RESTORE_DEFAULT_ALL:
        case RESTORE_DEFAULT:
            ItemModelFlexibleCamera.UpdateLink link = StartupClientOnly.modelBakeEventHandler.getItemOverrideLink();
            BakedModel savedModel = link.itemModelToOverride;
            if (savedModel != null) {  // not sure why this would ever be null, but it was (in a bug report), so just
                // check to make sure.
                link.itemModelToOverride = null;
                if (linkToHudRenderer.selectedField == HUDTextRenderer.HUDInfoUpdateLink.SelectedField.RESTORE_DEFAULT) {
                    copySingleTransform(linkToHudRenderer, savedModel, linkToHudRenderer.selectedTransform);
                } else {
                    for (HUDTextRenderer.HUDInfoUpdateLink.TransformName transformName
                            : HUDTextRenderer.HUDInfoUpdateLink.TransformName.VALUES) {
                        copySingleTransform(linkToHudRenderer, savedModel, transformName);
                    }
                }
            }
            link.itemModelToOverride = savedModel;
            break;
        case PRINT:
            StringBuilder output = new StringBuilder();
            output.append("\n\"display\": {\n");
            printTransform(output, "thirdperson_righthand",
                    linkToHudRenderer.itemCameraTransforms.thirdPersonRightHand);
            output.append(",\n");
            printTransform(output, "thirdperson_lefthand", linkToHudRenderer.itemCameraTransforms.thirdPersonLeftHand);
            output.append(",\n");
            printTransform(output, "firstperson_righthand",
                    linkToHudRenderer.itemCameraTransforms.firstPersonRightHand);
            output.append(",\n");
            printTransform(output, "firstperson_lefthand", linkToHudRenderer.itemCameraTransforms.firstPersonLeftHand);
            output.append(",\n");
            printTransform(output, "gui", linkToHudRenderer.itemCameraTransforms.gui);
            output.append(",\n");
            printTransform(output, "head", linkToHudRenderer.itemCameraTransforms.head);
            output.append(",\n");
            printTransform(output, "fixed", linkToHudRenderer.itemCameraTransforms.fixed);
            output.append(",\n");
            printTransform(output, "ground", linkToHudRenderer.itemCameraTransforms.ground);
            output.append("\n}");
            LOGGER.info(output);
            LiteralText text = new LiteralText("\"display\" JSON section printed to console (LOGGER.info)...");
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(text);
            break;
        }
    }

    // points to the appropriate transform based on which transform has been selected.
    private static Transformation getItemTransformRef(HUDTextRenderer.HUDInfoUpdateLink linkToHUDRenderer,
                                                      HUDTextRenderer.HUDInfoUpdateLink.TransformName transformName) {
        switch (transformName) {
        case THIRD_LEFT:
            return linkToHUDRenderer.itemCameraTransforms.thirdPersonLeftHand;
        case THIRD_RIGHT:
            return linkToHUDRenderer.itemCameraTransforms.thirdPersonRightHand;
        case FIRST_LEFT:
            return linkToHUDRenderer.itemCameraTransforms.firstPersonLeftHand;
        case FIRST_RIGHT:
            return linkToHUDRenderer.itemCameraTransforms.firstPersonRightHand;
        case GUI:
            return linkToHUDRenderer.itemCameraTransforms.gui;
        case HEAD:
            return linkToHUDRenderer.itemCameraTransforms.head;
        case FIXED:
            return linkToHUDRenderer.itemCameraTransforms.fixed;
        default:
            return linkToHUDRenderer.itemCameraTransforms.ground;
        }
    }

    private void copySingleTransform(HUDTextRenderer.HUDInfoUpdateLink linkToHUDRenderer, BakedModel savedModel,
                                     HUDTextRenderer.HUDInfoUpdateLink.TransformName transformToBeCopied) {
        Transformation transformation = getItemTransformRef(linkToHUDRenderer, transformToBeCopied);
        ModelTransformation.Mode currentType = transformToBeCopied.getVanillaTransformType();
        Transformation transform = savedModel.getTransformation().getTransformation(currentType);
        copyTransforms(transform, transformation);
    }

    private static void printTransform(StringBuilder output, String transformView, Transformation transformation) {
        output.append("    \"").append(transformView).append("\": {\n");
        output.append("        \"rotation\": [ ");
        output.append(String.format(Locale.US, "%.0f, ", transformation.rotation.getX()));
        output.append(String.format(Locale.US, "%.0f, ", transformation.rotation.getY()));
        output.append(String.format(Locale.US, "%.0f ],", transformation.rotation.getZ()));
        output.append("\n");

        final double TRANSLATE_MULTIPLIER = 1 / 0.0625;   // see Transformation.Deserializer::deserialize
        output.append("        \"translation\": [ ");
        output.append(String.format(Locale.US, "%.2f, ", transformation.translation.getX() * TRANSLATE_MULTIPLIER));
        output.append(String.format(Locale.US, "%.2f, ", transformation.translation.getY() * TRANSLATE_MULTIPLIER));
        output.append(String.format(Locale.US, "%.2f ],", transformation.translation.getZ() * TRANSLATE_MULTIPLIER));
        output.append("\n");
        output.append("        \"scale\": [ ");
        output.append(String.format(Locale.US, "%.2f, ", transformation.scale.getX()));
        output.append(String.format(Locale.US, "%.2f, ", transformation.scale.getY()));
        output.append(String.format(Locale.US, "%.2f ]", transformation.scale.getZ()));
        output.append("\n    }");
    }

    private static void copyTransforms(Transformation from, Transformation to) {
        to.translation.set(from.translation.getX(), from.translation.getY(), from.translation.getZ());
        to.scale.set(from.scale.getX(), from.scale.getY(), from.scale.getZ());
        to.rotation.set(from.rotation.getX(), from.rotation.getY(), from.rotation.getZ());
    }

    /**
     * Intercept arrow keys and handle repeats
     */
    public static class MenuKeyHandler {

        private final KeyPressCallback keyPressCallback;
        private long keyDownTimeTicks = 0;
        private ArrowKeys lastKey = ArrowKeys.NONE;

        public MenuKeyHandler(KeyPressCallback keyPressCallback) {
            this.keyPressCallback = keyPressCallback;
        }

        public void clientTick() {
            ArrowKeys keyPressed = ArrowKeys.NONE;
            if (isKeyDown(GLFW.GLFW_KEY_LEFT)) keyPressed = ArrowKeys.LEFT;
            if (isKeyDown(GLFW.GLFW_KEY_RIGHT)) keyPressed = ArrowKeys.RIGHT;
            if (isKeyDown(GLFW.GLFW_KEY_DOWN)) keyPressed = ArrowKeys.DOWN;
            if (isKeyDown(GLFW.GLFW_KEY_UP)) keyPressed = ArrowKeys.UP;

            if (keyPressed == ArrowKeys.NONE) {
                lastKey = ArrowKeys.NONE;
                return;
            }
            if (keyPressed != lastKey) {
                lastKey = keyPressed;
                keyDownTimeTicks = 0;
            } else {
                ++keyDownTimeTicks;
                final int INITIAL_PAUSE_TICKS = 10;  // wait 10 ticks before repeating
                if (keyDownTimeTicks < INITIAL_PAUSE_TICKS) return;
            }
            keyPressCallback.keyPressed(keyPressed);
        }

        static boolean isKeyDown(int key) {
            return GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), key) == GLFW.GLFW_PRESS;
        }

        public enum ArrowKeys {NONE, UP, DOWN, LEFT, RIGHT}

    }

}
