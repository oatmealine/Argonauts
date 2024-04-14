package earth.terrarium.argonauts.client.compat.prometheus;

import earth.terrarium.argonauts.common.compat.prometheus.ArgonautsOptions;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.olympus.client.components.textbox.IntTextBox;
import earth.terrarium.prometheus.api.roles.client.Page;
import earth.terrarium.prometheus.client.utils.UiUtils;
import earth.terrarium.prometheus.common.handlers.role.Role;
import earth.terrarium.prometheus.common.menus.content.RoleEditContent;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.Layout;

public class ArgonautsOptionsPage implements Page {

    private final RoleEditContent content;

    private IntTextBox maxClaimsBox;
    private IntTextBox maxChunkLoaded;

    public ArgonautsOptionsPage(RoleEditContent content, Runnable ignored) {
        this.content = content;
    }

    @Override
    public Layout getContents(int width, int height) {
        GridLayout layout = new GridLayout().rowSpacing(5);

        Role role = content.selected();
        ArgonautsOptions options = role.getNonNullOption(ArgonautsOptions.SERIALIZER);

        maxClaimsBox = UiUtils.addLine(
            layout, 0, width,
            ConstantComponents.MAX_GUILD_MEMBERS,
            (w) -> new IntTextBox(
                maxClaimsBox,
                w, 20,
                options.maxGuildMembers(), i -> {}
            )
        );

        maxChunkLoaded = UiUtils.addLine(
            layout, 1, width,
            ConstantComponents.MAX_PARTY_MEMBERS,
            (w) -> new IntTextBox(
                maxChunkLoaded,
                w, 20,
                options.maxPartyMembers(), i -> {}
            )
        );

        return layout;
    }

    @Override
    public void save(Role role) {
        ArgonautsOptions options = role.getNonNullOption(ArgonautsOptions.SERIALIZER);
        ArgonautsOptions newOptions = new ArgonautsOptions(
            maxClaimsBox.getIntValue().orElse(options.maxGuildMembers()),
            maxChunkLoaded.getIntValue().orElse(options.maxPartyMembers())
        );
        if (!newOptions.equals(options)) {
            role.setData(newOptions);
        }
    }
}
