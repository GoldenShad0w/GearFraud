package goldenshadow.gearviewspoofer.mixin;

import com.wynntils.models.gear.type.GearInstance;
import com.wynntils.models.items.items.game.GearItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = GearItem.class, remap = false)
public interface GearItemAccessor {

    @Mutable
    @Accessor("gearInstance")
    public void setGearInstance(GearInstance gearInstance);
}
