package earth.terrarium.argonauts.client.compat.rei;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import com.teamresourceful.resourcefullib.client.utils.ScreenUtils;
import earth.terrarium.argonauts.Argonauts;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.favorites.FavoriteEntry;
import me.shedaniel.rei.api.client.favorites.FavoriteEntryType;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

@SuppressWarnings("UnstableApiUsage")
public class PartyChatFavoriteEntry extends FavoriteEntry {
    public static final ResourceLocation ID = new ResourceLocation(Argonauts.MOD_ID, "party_chat");
    private static final ResourceLocation TEXTURE = new ResourceLocation(Argonauts.MOD_ID, "textures/gui/icons/teamwork.png");

    @Override
    public boolean isInvalid() {
        return false;
    }

    @Override
    public Renderer getRenderer(boolean showcase) {
        return new Renderer() {
            @Override
            public void render(GuiGraphics graphics, Rectangle bounds, int mouseX, int mouseY, float delta) {
                graphics.pose().pushPose();
                graphics.pose().translate(bounds.getCenterX(), bounds.getCenterY(), 0);
                graphics.pose().scale(bounds.getWidth() / 16f, bounds.getHeight() / 16f, 1);
                graphics.blit(TEXTURE, -8, -8, 0, 0, 16, 16, 16, 16);
                graphics.pose().popPose();
            }

            @Override
            public Tooltip getTooltip(TooltipContext context) {
                return Tooltip.create(context.getPoint(), Component.translatable(ID.toLanguageKey("rei", "tooltip")));
            }
        };
    }

    @Override
    public boolean doAction(int button) {
        if (button == 0) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            ScreenUtils.sendCommand("party chat");
            return true;
        }
        return false;
    }

    @Override
    public long hashIgnoreAmount() {
        return 31290831290L;
    }

    @Override
    public FavoriteEntry copy() {
        return this;
    }

    @Override
    public ResourceLocation getType() {
        return ID;
    }

    @Override
    public boolean isSame(FavoriteEntry other) {
        return other instanceof PartyChatFavoriteEntry;
    }

    public enum Type implements FavoriteEntryType<PartyChatFavoriteEntry> {
        INSTANCE;

        @Override
        public DataResult<PartyChatFavoriteEntry> read(CompoundTag object) {
            return DataResult.success(new PartyChatFavoriteEntry(), Lifecycle.stable());
        }

        @Override
        public DataResult<PartyChatFavoriteEntry> fromArgs(Object... args) {
            return DataResult.success(new PartyChatFavoriteEntry(), Lifecycle.stable());
        }

        @Override
        public CompoundTag save(PartyChatFavoriteEntry entry, CompoundTag tag) {
            return tag;
        }
    }
}