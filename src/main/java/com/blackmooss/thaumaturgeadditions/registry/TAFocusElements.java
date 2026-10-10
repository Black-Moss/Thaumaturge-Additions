// -----------------------------------------------------------------------------
// 已停用：依赖已被移除的 FocusElement / FocusElementType API。
// 与 SalisMundusFocusEffect 一同待重做。
// 停用日期：2026-10-10
// -----------------------------------------------------------------------------
// package com.blackmooss.thaumaturgeadditions.registry;

// import com.blackmooss.thaumaturgeadditions.ThaumaturgeAdditions;
// import com.blackmooss.thaumaturgeadditions.focus.SalisMundusFocusEffect;
// import com.leclowndu93150.thaumaturge.api.casters.FocusElement;
// import com.leclowndu93150.thaumaturge.api.casters.FocusElementType;
// import net.minecraft.resources.Identifier;
// import net.neoforged.neoforge.registries.DeferredHolder;
// import net.neoforged.neoforge.registries.DeferredRegister;

// public final class TAFocusElements {
//     public static final DeferredRegister<FocusElementType> ELEMENTS = DeferredRegister.create(FocusElementType.REGISTRY_KEY, ThaumaturgeAdditions.MODID);

//     public static final DeferredHolder<FocusElementType, FocusElementType> SALIS_MUNDUS =
//             element(SalisMundusFocusEffect.KEY.getPath(), new SalisMundusFocusEffect(), 0xC0A0FF);

//     private TAFocusElements() {
//     }

//     private static DeferredHolder<FocusElementType, FocusElementType> element(String path, FocusElement element, int color) {
//         return ELEMENTS.register(path, () -> new FocusElementType(element, icon(path), color));
//     }

//     private static Identifier icon(String path) {
//         return ThaumaturgeAdditions.identifier("textures/foci/" + path + ".png");
//     }
// }
