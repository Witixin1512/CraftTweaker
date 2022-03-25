package com.blamejared.crafttweaker.api.tag.manager.type;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.tag.unknown.ActionUnknownTagAdd;
import com.blamejared.crafttweaker.api.action.tag.unknown.ActionUnknownTagCreate;
import com.blamejared.crafttweaker.api.action.tag.unknown.ActionUnknownTagRemove;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.tag.MutableLoadResult;
import com.blamejared.crafttweaker.api.tag.manager.ITagManager;
import com.blamejared.crafttweaker.api.tag.type.UnknownTag;
import com.blamejared.crafttweaker.api.util.GenericUtil;
import com.blamejared.crafttweaker.mixin.common.access.tag.AccessTag;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagManager;
import org.openzen.zencode.java.ZenCodeType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ZenRegister
@Document("vanilla/api/tag/manager/type/UnknownTagManager")
@ZenCodeType.Name("crafttweaker.api.tag.manager.type.UnknownTagManager")
public class UnknownTagManager implements ITagManager<UnknownTag> {
    
    private final ResourceKey<? extends Registry<?>> resourceKey;
    private final MutableLoadResult<?> backingResult;
    private Map<ResourceLocation, UnknownTag> tagCache;
    
    public UnknownTagManager(ResourceKey<? extends Registry<?>> resourceKey) {
        
        this.resourceKey = resourceKey;
        this.backingResult = new MutableLoadResult<>();
        this.tagCache = new HashMap<>();
    }
    
    @Override
    public ResourceKey<? extends Registry<?>> resourceKey() {
        
        return resourceKey;
    }
    
    @ZenCodeType.Method
    public final void addElements(UnknownTag to, ResourceLocation... values) {
        
        if(!exists(to)) {
            CraftTweakerAPI.apply(new ActionUnknownTagCreate(to, List.of(values)));
        } else {
            CraftTweakerAPI.apply(new ActionUnknownTagAdd(to, List.of(values)));
        }
        recalculate();
    }
    
    @ZenCodeType.Method
    public List<ResourceLocation> elements(UnknownTag of) {
        
        if(!exists(of)) {
            return List.of();
        }
        return getInternal(of).getValues()
                .stream()
                .map(Holder::unwrapKey)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ResourceKey::location)
                .collect(Collectors.toList());
    }
    
    @ZenCodeType.Method
    public final void removeElements(UnknownTag from, ResourceLocation... values) {
        
        if(!exists(from)) {
            throw new IllegalArgumentException("Cannot remove elements from empty tag: " + from);
        }
        CraftTweakerAPI.apply(new ActionUnknownTagRemove(from, List.of(values)));
        recalculate();
    }
    
    @ZenCodeType.Method
    public UnknownTag tag(String id) {
        
        return tag(new ResourceLocation(id));
    }
    
    @ZenCodeType.Method
    public UnknownTag tag(ResourceLocation id) {
        
        return tagMap().getOrDefault(id, new UnknownTag(id, this));
    }
    
    @Override
    public void recalculate() {
        
        this.tagCache = backingResult.tagMap()
                .keySet()
                .stream()
                .map(id -> Pair.of(id, new UnknownTag(id, this)))
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
    }
    
    @ZenCodeType.Method
    @ZenCodeType.Getter("tagMap")
    public Map<ResourceLocation, UnknownTag> tagMap() {
        
        if(this.tagCache.isEmpty()) {
            this.recalculate();
        }
        return tagCache;
    }
    
    public Map<ResourceLocation, Tag<Holder<?>>> internalTags() {
        
        return GenericUtil.uncheck(Collections.unmodifiableMap(backingResult.tagMap()));
    }
    
    @Nullable
    public Tag<Holder<?>> getInternal(UnknownTag tag) {
        
        return GenericUtil.uncheck(backingResult.tagMap().get(tag.id()));
    }
    
    @Override
    public List<ResourceLocation> tagKeys() {
        
        return new ArrayList<>(tagMap().keySet());
    }
    
    @Override
    public <U> void addTag(ResourceLocation id, Tag<Holder<U>> tag) {
        
        AccessTag accessTag = (AccessTag) tag;
        accessTag.crafttweaker$setElements(new ArrayList<>(accessTag.crafttweaker$getElements()));
        this.backingResult.addTag(id, GenericUtil.uncheck(tag));
        recalculate();
    }
    
    @Override
    public void bind(TagManager.LoadResult<?> result) {
        
        this.backingResult.bind(GenericUtil.uncheck(result));
    }
    
    @ZenCodeType.Method
    @ZenCodeType.Getter("tags")
    public List<UnknownTag> tags() {
        
        return new ArrayList<>(tagMap().values());
    }
    
    @ZenCodeType.Method
    public List<UnknownTag> getTagsFor(ResourceLocation element) {
        
        return tags().stream().filter(tag -> tag.contains(element)).toList();
    }
    
}
