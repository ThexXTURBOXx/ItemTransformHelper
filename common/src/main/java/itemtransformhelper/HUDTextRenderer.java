package itemtransformhelper;

import java.util.ArrayList;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * User: The Grey Ghost
 * Date: 20/01/2015
 * Class to draw the menu on the screen
 */
public class HUDTextRenderer {

    private static final HUDInfoUpdateLink.SelectedField NOT_SELECTABLE = null;
    private static final int MED_GRAY_HALF_TRANSPARENT = 0x6FAFAFB0;
    private static final int GREEN_HALF_TRANSPARENT = 0x6F00FF00;
    private static final int LIGHT_GRAY = 0xE0E0E0;
    private static final int BLACK = 0x000000;

    private final HUDInfoUpdateLink hudInfoUpdateLink;

    /**
     * Create the HUDTextRenderer; displayHUDText needs to be called on RenderGameOverlayEvent.Text
     *
     * @param hudInfoUpdateLink the menu state information needed to draw the Heads Up Display
     */
    public HUDTextRenderer(HUDInfoUpdateLink hudInfoUpdateLink) {
        this.hudInfoUpdateLink = hudInfoUpdateLink;
    }

    /**
     * Draw the Head Up Display menu on screen.
     * The information is taken from the hudInfoUpdateLink which is updated by other classes.
     */
    public void displayHUDText(GuiGraphics guiGraphics) {
        if (hudInfoUpdateLink == null || !hudInfoUpdateLink.menuVisible)
            return;
        ArrayList<String> displayText = new ArrayList<>();
        ArrayList<HUDInfoUpdateLink.SelectedField> selectableField = new ArrayList<>();

        displayText.add("======");
        selectableField.add(NOT_SELECTABLE);
        displayText.add("VIEW  ");
        selectableField.add(NOT_SELECTABLE);
        ItemTransform transformation;

        switch (hudInfoUpdateLink.selectedTransform) {
        case THIRD_LEFT -> displayText.add("3rd-L");
        case THIRD_RIGHT -> displayText.add("3rd-R");
        case FIRST_LEFT -> displayText.add("1st-L");
        case FIRST_RIGHT -> displayText.add("1st-R");
        case GUI -> displayText.add("gui");
        case HEAD -> displayText.add("head");
        case FIXED -> displayText.add("fixed");
        case GROUND -> displayText.add("grnd");
        }
        transformation = hudInfoUpdateLink.getItemTransform();
        selectableField.add(HUDInfoUpdateLink.SelectedField.TRANSFORM);

        displayText.add("======");
        selectableField.add(NOT_SELECTABLE);
        displayText.add("SCALE");
        selectableField.add(NOT_SELECTABLE);
        displayText.add("X:" + String.format("%.2f", transformation.scale().x()));
        selectableField.add(HUDInfoUpdateLink.SelectedField.SCALE_X);
        displayText.add("Y:" + String.format("%.2f", transformation.scale().y()));
        selectableField.add(HUDInfoUpdateLink.SelectedField.SCALE_Y);
        displayText.add("Z:" + String.format("%.2f", transformation.scale().z()));
        selectableField.add(HUDInfoUpdateLink.SelectedField.SCALE_Z);

        displayText.add("======");
        selectableField.add(NOT_SELECTABLE);
        displayText.add("ROTATE");
        selectableField.add(NOT_SELECTABLE);
        displayText.add("X:" + String.format("%3.0f", transformation.rotation().x()));
        selectableField.add(HUDInfoUpdateLink.SelectedField.ROTATE_X);
        displayText.add("Y:" + String.format("%3.0f", transformation.rotation().y()));
        selectableField.add(HUDInfoUpdateLink.SelectedField.ROTATE_Y);
        displayText.add("Z:" + String.format("%3.0f", transformation.rotation().z()));
        selectableField.add(HUDInfoUpdateLink.SelectedField.ROTATE_Z);

        final double TRANSLATE_MULTIPLIER = 1 / 0.0625;   // see ItemTransform.Deserializer::deserialize
        displayText.add("======");
        selectableField.add(NOT_SELECTABLE);
        displayText.add("TRANSL");
        selectableField.add(NOT_SELECTABLE);
        displayText.add("X:" + String.format("%.2f", transformation.translation().x() * TRANSLATE_MULTIPLIER));
        selectableField.add(HUDInfoUpdateLink.SelectedField.TRANSLATE_X);
        displayText.add("Y:" + String.format("%.2f", transformation.translation().y() * TRANSLATE_MULTIPLIER));
        selectableField.add(HUDInfoUpdateLink.SelectedField.TRANSLATE_Y);
        displayText.add("Z:" + String.format("%.2f", transformation.translation().z() * TRANSLATE_MULTIPLIER));
        selectableField.add(HUDInfoUpdateLink.SelectedField.TRANSLATE_Z);

        displayText.add("======");
        selectableField.add(NOT_SELECTABLE);
        displayText.add("RESET");
        selectableField.add(HUDInfoUpdateLink.SelectedField.RESTORE_DEFAULT);
        displayText.add("RSTALL");
        selectableField.add(HUDInfoUpdateLink.SelectedField.RESTORE_DEFAULT_ALL);
        displayText.add("PRINT");
        selectableField.add(HUDInfoUpdateLink.SelectedField.PRINT);
        displayText.add("======");
        selectableField.add(NOT_SELECTABLE);

        Font font = Minecraft.getInstance().font;
        int yPos = 2;
        int xPos = 2;
        for (int i = 0; i < displayText.size(); ++i) {
            String msg = displayText.get(i);
            yPos += font.lineHeight;
            if (msg == null) continue;
            boolean fieldIsSelected = hudInfoUpdateLink.selectedField == selectableField.get(i);
            int highlightColour = fieldIsSelected ? GREEN_HALF_TRANSPARENT : MED_GRAY_HALF_TRANSPARENT;
            guiGraphics.fill(xPos - 1, yPos - 1,
                    xPos + font.width(msg) + 1, yPos + font.lineHeight - 1, highlightColour);
            int stringColour = fieldIsSelected ? BLACK : LIGHT_GRAY;
            guiGraphics.drawString(font, msg, xPos, yPos, stringColour, false);
        }
    }

    /**
     * Used to provide the information that the HUDTextRenderer needs to draw the menu
     */
    public static class HUDInfoUpdateLink {

        private static final Vector3f ROTATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
        private static final Vector3f TRANSLATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
        private static final Vector3f SCALE_DEFAULT = new Vector3f(1.0F, 1.0F, 1.0F);

        public ItemTransforms itemCameraTransforms;
        public SelectedField selectedField;
        public TransformName selectedTransform;
        public boolean menuVisible;

        public HUDInfoUpdateLink() {
            ItemTransform trThirdLeft = new ItemTransform(ROTATION_DEFAULT, TRANSLATION_DEFAULT, SCALE_DEFAULT);
            ItemTransform trThirdRight = new ItemTransform(ROTATION_DEFAULT, TRANSLATION_DEFAULT, SCALE_DEFAULT);
            ItemTransform trFirstLeft = new ItemTransform(ROTATION_DEFAULT, TRANSLATION_DEFAULT, SCALE_DEFAULT);
            ItemTransform trFirstRight = new ItemTransform(ROTATION_DEFAULT, TRANSLATION_DEFAULT, SCALE_DEFAULT);
            ItemTransform trHead = new ItemTransform(ROTATION_DEFAULT, TRANSLATION_DEFAULT, SCALE_DEFAULT);
            ItemTransform trGui = new ItemTransform(ROTATION_DEFAULT, TRANSLATION_DEFAULT, SCALE_DEFAULT);
            ItemTransform trGround = new ItemTransform(ROTATION_DEFAULT, TRANSLATION_DEFAULT, SCALE_DEFAULT);
            ItemTransform trFixed = new ItemTransform(ROTATION_DEFAULT, TRANSLATION_DEFAULT, SCALE_DEFAULT);
            itemCameraTransforms = new ItemTransforms(trThirdLeft, trThirdRight,
                    trFirstLeft, trFirstRight, trHead, trGui, trGround, trFixed);

            selectedField = SelectedField.TRANSFORM;
            selectedTransform = TransformName.FIRST_RIGHT;
            menuVisible = false;
        }

        // points to the appropriate transform based on which transform has been selected.
        public ItemTransform getItemTransform() {
            return selectedTransform.getItemTransform(itemCameraTransforms);
        }

        // points to the appropriate transform vector based on which field has been selected.
        public Vector3fc getItemTransformVec() {
            return selectedField.getItemTransformVec(itemCameraTransforms, selectedTransform);
        }

        // points to the appropriate transform vector based on which field has been selected.
        public float getItemTransformVecValue() {
            return selectedField.getItemTransformVecValue(itemCameraTransforms, selectedTransform);
        }

        public enum TransformName {

            THIRD_LEFT(ItemDisplayContext.THIRD_PERSON_LEFT_HAND),
            THIRD_RIGHT(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND),
            FIRST_LEFT(ItemDisplayContext.FIRST_PERSON_LEFT_HAND),
            FIRST_RIGHT(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND),
            HEAD(ItemDisplayContext.HEAD),
            GUI(ItemDisplayContext.GUI),
            GROUND(ItemDisplayContext.GROUND),
            FIXED(ItemDisplayContext.FIXED);

            public static final TransformName[] VALUES = TransformName.values();

            private final ItemDisplayContext vanillaType;

            public TransformName getNext() {
                for (TransformName transformName : VALUES) {
                    if (transformName.ordinal() == this.ordinal() + 1) return transformName;
                }
                return THIRD_LEFT;
            }

            public TransformName getPrevious() {
                for (TransformName transformName : VALUES) {
                    if (transformName.ordinal() == this.ordinal() - 1) return transformName;
                }
                return FIXED;
            }

            public ItemDisplayContext getVanillaTransformType() {
                return vanillaType;
            }

            TransformName(ItemDisplayContext vanillaType) {
                this.vanillaType = vanillaType;
            }

            public ItemTransform getItemTransform(ItemTransforms transforms) {
                return switch (this) {
                    case THIRD_LEFT -> transforms.thirdPersonLeftHand();
                    case THIRD_RIGHT -> transforms.thirdPersonRightHand();
                    case FIRST_LEFT -> transforms.firstPersonLeftHand();
                    case FIRST_RIGHT -> transforms.firstPersonRightHand();
                    case GUI -> transforms.gui();
                    case HEAD -> transforms.head();
                    case FIXED -> transforms.fixed();
                    case GROUND -> transforms.ground();
                };
            }

        }

        public enum SelectedField {

            TRANSFORM(0),
            SCALE_X(1), SCALE_Y(2), SCALE_Z(3),
            ROTATE_X(4), ROTATE_Y(5), ROTATE_Z(6),
            TRANSLATE_X(7), TRANSLATE_Y(8), TRANSLATE_Z(9),
            RESTORE_DEFAULT(10), RESTORE_DEFAULT_ALL(11), PRINT(12);

            public static final SelectedField[] VALUES = SelectedField.values();
            private static final SelectedField FIRST_FIELD = TRANSFORM;
            private static final SelectedField LAST_FIELD = PRINT;

            public final int fieldIndex;

            SelectedField(int index) {
                fieldIndex = index;
            }

            public static SelectedField getFieldName(int indexToFind) {
                for (SelectedField checkField : VALUES) {
                    if (checkField.fieldIndex == indexToFind) return checkField;
                }
                return null;
            }

            public SelectedField getNextField() {
                SelectedField nextField = getFieldName(fieldIndex + 1);
                if (nextField == null) nextField = FIRST_FIELD;
                return nextField;
            }

            public SelectedField getPreviousField() {
                SelectedField previousField = getFieldName(fieldIndex - 1);
                if (previousField == null) previousField = LAST_FIELD;
                return previousField;
            }

            public Vector3fc getItemTransformVec(ItemTransforms transforms, TransformName selectedTransform) {
                ItemTransform transform = selectedTransform.getItemTransform(transforms);
                if (isScale()) return transform.scale();
                if (isRotation()) return transform.rotation();
                if (isTranslation()) return transform.translation();
                return null;
            }

            public float getItemTransformVecValue(ItemTransforms transforms, TransformName selectedTransform) {
                Vector3fc vec = getItemTransformVec(transforms, selectedTransform);
                if (isX()) return Objects.requireNonNull(vec).x();
                if (isY()) return Objects.requireNonNull(vec).y();
                if (isZ()) return Objects.requireNonNull(vec).z();
                return 0;
            }

            public boolean isScale() {
                return this == SCALE_X || this == SCALE_Y || this == SCALE_Z;
            }

            public boolean isRotation() {
                return this == ROTATE_X || this == ROTATE_Y || this == ROTATE_Z;
            }

            public boolean isTranslation() {
                return this == TRANSLATE_X || this == TRANSLATE_Y || this == TRANSLATE_Z;
            }

            public boolean isX() {
                return this == SCALE_X || this == ROTATE_X || this == TRANSLATE_X;
            }

            public boolean isY() {
                return this == SCALE_Y || this == ROTATE_Y || this == TRANSLATE_Y;
            }

            public boolean isZ() {
                return this == SCALE_Z || this == ROTATE_Z || this == TRANSLATE_Z;
            }

        }

    }

}
