package goldenshadow.gearviewspoofer.mixin;

import com.google.gson.JsonObject;
import com.wynntils.core.components.Models;
import com.wynntils.models.gear.type.GearInstance;
import com.wynntils.models.inventory.type.InventoryAccessory;
import com.wynntils.models.inventory.type.InventoryArmor;
import com.wynntils.models.items.WynnItem;
import com.wynntils.models.items.items.game.GearItem;
import com.wynntils.models.stats.type.*;
import com.wynntils.services.hades.HadesService;
import com.wynntils.utils.mc.McUtils;
import com.wynntils.utils.type.RangedValue;
import goldenshadow.gearviewspoofer.client.ItemData;
import goldenshadow.gearviewspoofer.client.ModConfig;
import goldenshadow.gearviewspoofer.client.Quality;
import goldenshadow.gearviewspoofer.client.ShinyType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;


@Mixin(value = HadesService.class, remap = false)
public abstract class HadesServiceMixin {


    @Shadow
    @Final
    private Map<InventoryArmor, String> armor;

    @Shadow
    private NavigableMap<InventoryArmor, WynnItem> armorCache;

    @Shadow
    @Final
    private Map<InventoryAccessory, String> accessories;

    @Shadow
    private NavigableMap<InventoryAccessory, WynnItem> accessoriesCache;


    @Shadow
    protected abstract String encodeItem(Optional<WynnItem> item);

    @Shadow
    private String heldItem;

    @Shadow
    private WynnItem heldItemCache;

    @Inject(at = @At("HEAD"), method = "updateArmorCache", cancellable = true)
    private void updateArmorCache(InventoryArmor inventoryArmor, CallbackInfo ci) {

        ModConfig.SlotConfig config = ModConfig.INSTANCE.getConfig(inventoryArmor.getArmorSlot());

        boolean override = !config.customItemCode.isEmpty();
        Quality quality = config.quality;
        boolean shiny = config.shiny;
        ShinyStat shinyStat = null;

        if (override || quality != Quality.DEFAULT || shiny) {

            if (override) {
                armor.put(inventoryArmor, config.customItemCode);
                armorCache.remove(inventoryArmor);
                ci.cancel();
                return;
            }

            if (shiny) {
                ShinyType shinyType = config.shinyType;
                ShinyStatType shinyStatType = new ShinyStatType(shinyType.id, shinyType.key, shinyType.displayName, StatUnit.RAW);
                shinyStat = new ShinyStat(shinyStatType, config.shinyCount, config.shinyRerolls);
            }

            Optional<WynnItem> wynnItemOptional = Models.Item.getWynnItem(McUtils.inventory().armor.get(inventoryArmor.getArmorSlot()));
            if (wynnItemOptional.isPresent()) {

                if (wynnItemOptional.get() instanceof GearItem gearItem) {
                    armor.put(inventoryArmor, getModifiedEncodedString(gearItem, quality, shiny, shinyStat));
                    armorCache.put(inventoryArmor, gearItem);
                }
            }

            ci.cancel();
        }
    }

    @Inject(at = @At("TAIL"), method = "updateAccessoryCache", cancellable = true)
    private void updateAccessoryCache(InventoryAccessory inventoryAccessory, CallbackInfo ci) {

        ModConfig.SlotConfig config = ModConfig.INSTANCE.getConfig(inventoryAccessory.getSlot());

        boolean override = !config.customItemCode.isEmpty();
        Quality quality = config.quality;
        boolean shiny = config.shiny;
        ShinyStat shinyStat = null;

        if (override || quality != Quality.DEFAULT || shiny) {

            if (override) {
                accessories.put(inventoryAccessory, config.customItemCode);
                accessoriesCache.remove(inventoryAccessory);
                ci.cancel();
                return;
            }

            if (shiny) {
                ShinyType shinyType = config.shinyType;
                ShinyStatType shinyStatType = new ShinyStatType(shinyType.id, shinyType.key, shinyType.displayName, StatUnit.RAW);
                shinyStat = new ShinyStat(shinyStatType, config.shinyCount, config.shinyRerolls);
            }

            Optional<WynnItem> wynnItemOptional = Models.Item.getWynnItem(McUtils.inventory().armor.get(inventoryAccessory.getSlot()));
            if (wynnItemOptional.isPresent()) {

                if (wynnItemOptional.get() instanceof GearItem gearItem) {
                    accessories.put(inventoryAccessory, getModifiedEncodedString(gearItem, quality, shiny, shinyStat));
                    accessoriesCache.put(inventoryAccessory, gearItem);
                }
            }

            ci.cancel();
        }
    }

    @Inject(at = @At("TAIL"), method = "updateHeldItemCache", cancellable = true)
    private void updateHeldItemCache(CallbackInfo ci) {

        ModConfig.SlotConfig config = ModConfig.INSTANCE.getConfig(-1);

        boolean override = !config.customItemCode.isEmpty();
        Quality quality = config.quality;
        boolean shiny = config.shiny;
        ShinyStat shinyStat = null;

        if (override || quality != Quality.DEFAULT || shiny) {

            if (override) {
                heldItem = config.customItemCode;
                heldItemCache = null;
                ci.cancel();
                return;
            }

            if (shiny) {
                ShinyType shinyType = config.shinyType;
                ShinyStatType shinyStatType = new ShinyStatType(shinyType.id, shinyType.key, shinyType.displayName, StatUnit.RAW);
                shinyStat = new ShinyStat(shinyStatType, config.shinyCount, config.shinyRerolls);
            }

            Optional<WynnItem> wynnItemOptional = Models.Item.getWynnItem(McUtils.player().getMainHandStack());
            if (wynnItemOptional.isPresent()) {

                if (wynnItemOptional.get() instanceof GearItem gearItem) {
                    heldItem = getModifiedEncodedString(gearItem, quality, shiny, shinyStat);
                    heldItemCache = gearItem;
                }
            }

            ci.cancel();
        }
    }

    @Unique
    private String getModifiedEncodedString(WynnItem wynnItem, Quality quality, boolean shiny, ShinyStat shinyStat) {

        GearItem gearItem = (GearItem) wynnItem;
        JsonObject itemObject = ItemData.INSTANCE.getItem(gearItem.getName());
        if (itemObject == null || gearItem.getItemInstance().isEmpty()) return "";

        GearInstance instance = gearItem.getItemInstance().get();

        List<StatActualValue> statActualValueList = new ArrayList<>();
        for  (StatActualValue statActualValue : instance.identifications()) {
            if (quality != Quality.DEFAULT) {
                int rangeValue = getRangeValue(quality == Quality.PERFECT, ItemData.INSTANCE.isPositiveValue(itemObject, statActualValue.statType().getApiName(), quality), statActualValue.statType().displayAsInverted());
                statActualValueList.add(new StatActualValue(statActualValue.statType(), statActualValue.value(), statActualValue.stars(), new RangedValue(rangeValue, rangeValue)));
            } else {
                statActualValueList.add(statActualValue);
            }
        }

        GearInstance newInstance = new GearInstance(statActualValueList, instance.powders(), instance.rerolls(), instance.overallQuality(), shiny ? Optional.of(shinyStat) : instance.shinyStat(), instance.meetsRequirements(), instance.setInstance());

        //modify the gear item, encode and then reverse the modification so the item doesn't get changed in the player's inventory
        ((GearItemAccessor) gearItem).setGearInstance(newInstance);
        String encoded = encodeItem(Optional.of(wynnItem));
        ((GearItemAccessor) gearItem).setGearInstance(instance);
        return encoded;
    }

    @Unique
    private int getRangeValue(boolean perfect, boolean positiveStat, boolean greenStat) {
        // red stats go from 70 (best) to 130 (worst), and green stats go from 30 (worst) to 130 (best)
        if (perfect && !positiveStat && !greenStat) return 70;
        if (!perfect && (positiveStat ^ greenStat)) return 30;
        return 130;
    }
}
