package goldenshadow.gearviewspoofer.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

@Config(name = "gearviewspoofer")
public class ModConfig implements ConfigData {

    @ConfigEntry.Gui.Excluded
    public static ModConfig INSTANCE;

    public static void init() {
        ConfigHolder<ModConfig> holder = AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        INSTANCE = holder.getConfig();
    }

    public SlotConfig getConfig(int slot) {
        return switch (slot) {
            case 0 -> bootsConfig;
            case 1 -> leggingsConfig;
            case 2 -> chestplateConfig;
            case 3 -> helmetConfig;
            case 9 -> ring1Config;
            case 10 -> ring2Config;
            case 11 -> braceletConfig;
            case 12 -> necklaceConfig;
            default -> weaponConfig;
        };
    }

    @ConfigEntry.Gui.CollapsibleObject
    public SlotConfig helmetConfig = new SlotConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public SlotConfig chestplateConfig = new SlotConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public SlotConfig leggingsConfig = new SlotConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public SlotConfig bootsConfig = new SlotConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public SlotConfig ring1Config = new SlotConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public SlotConfig ring2Config = new SlotConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public SlotConfig braceletConfig = new SlotConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public SlotConfig necklaceConfig = new SlotConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public SlotConfig weaponConfig = new SlotConfig();

    public static class SlotConfig {
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Quality quality = Quality.DEFAULT;
        public boolean shiny = false;
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public ShinyType shinyType = ShinyType.MOBS_KILLED;
        public long shinyCount = 0;
        public int shinyRerolls = 0;
        @ConfigEntry.Gui.Tooltip
        public String customItemCode = "";
    }

}
