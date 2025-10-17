package goldenshadow.gearviewspoofer.client;

import net.fabricmc.api.ClientModInitializer;

public class GearviewspooferClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModConfig.init();
        ItemData.init();
    }
}
