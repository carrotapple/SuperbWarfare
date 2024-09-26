package net.mcreator.superbwarfare.item.armor;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.mcreator.superbwarfare.ModUtils;
import net.mcreator.superbwarfare.client.renderer.armor.RuChest6b43ArmorRenderer;
import net.mcreator.superbwarfare.init.ModAttributes;
import net.mcreator.superbwarfare.tiers.ModArmorMaterial;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;
import java.util.function.Consumer;

public class RuChest6b43 extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public RuChest6b43() {
        super(ModArmorMaterial.CEMENTED_CARBIDE, Type.CHESTPLATE, new Properties());
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.renderer == null)
                    this.renderer = new RuChest6b43ArmorRenderer();
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        Multimap<Attribute, AttributeModifier> map = super.getDefaultAttributeModifiers(equipmentSlot);
        UUID uuid = new UUID(equipmentSlot.toString().hashCode(), 0);
        if (equipmentSlot == EquipmentSlot.CHEST) {
            map = HashMultimap.create(map);
            map.put(ModAttributes.BULLET_RESISTANCE.get(), new AttributeModifier(uuid, ModUtils.ATTRIBUTE_MODIFIER, 0.5f, AttributeModifier.Operation.ADDITION));

        }
        return map;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
