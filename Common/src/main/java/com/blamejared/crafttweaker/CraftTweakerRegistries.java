package com.blamejared.crafttweaker;


import com.blamejared.crafttweaker.api.CraftTweakerConstants;
import com.blamejared.crafttweaker.api.ingredient.condition.serializer.ConditionAnyDamagedSerializer;
import com.blamejared.crafttweaker.api.ingredient.condition.serializer.ConditionCustomSerializer;
import com.blamejared.crafttweaker.api.ingredient.condition.serializer.ConditionDamagedSerializer;
import com.blamejared.crafttweaker.api.ingredient.condition.serializer.IIngredientConditionSerializer;
import com.blamejared.crafttweaker.api.ingredient.transform.serializer.IIngredientTransformerSerializer;
import com.blamejared.crafttweaker.api.ingredient.transform.serializer.TransformCustomSerializer;
import com.blamejared.crafttweaker.api.ingredient.transform.serializer.TransformDamageSerializer;
import com.blamejared.crafttweaker.api.ingredient.transform.serializer.TransformReplaceSerializer;
import com.blamejared.crafttweaker.api.ingredient.transform.serializer.TransformerReuseSerializer;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class CraftTweakerRegistries {
    
    public static MappedRegistry<IIngredientTransformerSerializer<?>> REGISTRY_TRANSFORMER_SERIALIZER;
    public static MappedRegistry<IIngredientConditionSerializer<?>> REGISTRY_CONDITIONER_SERIALIZER;
    
    public static void init() {
        
        REGISTRY_TRANSFORMER_SERIALIZER = registerRegistry(CraftTweakerConstants.rl("transformer_serializer"));
        REGISTRY_CONDITIONER_SERIALIZER = registerRegistry(CraftTweakerConstants.rl("condition_serializer"));
        
        registerSerializer(REGISTRY_TRANSFORMER_SERIALIZER, TransformReplaceSerializer.INSTANCE);
        registerSerializer(REGISTRY_TRANSFORMER_SERIALIZER, TransformDamageSerializer.INSTANCE);
        registerSerializer(REGISTRY_TRANSFORMER_SERIALIZER, TransformCustomSerializer.INSTANCE);
        registerSerializer(REGISTRY_TRANSFORMER_SERIALIZER, TransformerReuseSerializer.INSTANCE);
        
        registerSerializer(REGISTRY_CONDITIONER_SERIALIZER, ConditionDamagedSerializer.INSTANCE);
        registerSerializer(REGISTRY_CONDITIONER_SERIALIZER, ConditionAnyDamagedSerializer.INSTANCE);
        registerSerializer(REGISTRY_CONDITIONER_SERIALIZER, ConditionCustomSerializer.INSTANCE);
    }
    
    @SuppressWarnings("rawtypes")
    private static <T> MappedRegistry<T> registerRegistry(ResourceLocation location) {
        
        WritableRegistry registry = (WritableRegistry) Registry.REGISTRY;
        ResourceKey<Registry<T>> regKey = ResourceKey.createRegistryKey(location);
        Lifecycle stable = Lifecycle.stable();
        MappedRegistry<T> mappedReg = new MappedRegistry<>(regKey, stable, null);
        registry.register(regKey, mappedReg, stable);
        return mappedReg;
    }
    
    private static void registerSerializer(MappedRegistry<IIngredientTransformerSerializer<?>> registry, IIngredientTransformerSerializer<?> serializer) {
        
        registry.register(ResourceKey.create(registry.key(), serializer.getType()), serializer, Lifecycle.stable());
    }
    
    private static void registerSerializer(MappedRegistry<IIngredientConditionSerializer<?>> registry, IIngredientConditionSerializer<?> serializer) {
        
        registry.register(ResourceKey.create(registry.key(), serializer.getType()), serializer, Lifecycle.stable());
    }
    
}
