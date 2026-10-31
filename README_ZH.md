![CurseForge 下载](https://img.shields.io/curseforge/dt/1748112?style=for-the-badge&logo=curseforge&logoColor=%23F16436)
![GitHub 最后提交](https://img.shields.io/github/last-commit/Black-Moss/Thaumaturge-Additions?style=for-the-badge&logo=github&logoColor=%23181717)

[English Guide](README.md)

# Thaumaturge Additions

[Thaumaturge](https://www.curseforge.com/minecraft/mc-mods/thaumaturge)的内容扩展模组。

添加了会自我修复的虚空旅行者之靴、彩虹笔与墨、虚空缸中之脑、永燃之瓮、核心效果：世界盐、奥术工作台升级等。

## 功能特性

### 奥术工作台升级（旅行者背包 / 精妙背包）

为两个背包模组各提供一对升级，操作方式完全一致：

- **奥术工作台升级**：在背包里装下一整套奥术工作台。3x3 合成格、六系元始水晶槽（气 / 火 / 水 / 地 / 秩序 / 混沌）以及一个法杖槽，布局与原版奥术工作台差不多。消耗的是**你当前所在区块**的灵气。
- **奥术充能板升级**：把灵气吸取范围从脚下的单个区块扩大到**周围 3x3 区块**，与原版奥术工作台充能器的行为一致。带开关，不想用的时候可以随时关掉，避免动到本地灵气。

需要 **[旅行者背包](https://www.curseforge.com/minecraft/mc-mods/travelers-backpack)** 与/或 **[精妙背包](https://www.curseforge.com/minecraft/mc-mods/sophisticated-backpacks)**。两个背包模组都是各自独立的可选依赖——装其中一个，或者两个都装。

### 虚空旅行者之靴

基于 Thaumaturge 虚空装备打造的旅行者之靴强化版：

- **自我修复** —— 自动消耗储存的灵气充能，永远不会真正坏掉。
- **安全坠落距离 +6 格。**
- **移动速度提升**（地面行走与水中涉水时），并提升台阶高度与跳跃高度。
- 所有法术消耗获得 **5% 灵气折扣**。
- **支持染色**，与普通旅行者之靴一致。

### 彩虹笔与墨

这个小瓶子里面无论装什么染料，写出来都是黑色的，名副其实“五彩斑斓的黑”。

### 虚空缸中之脑

缸中之脑的虚空版，也就是说满了会清除多出来的部分。

### 永燃之瓮

无尽之瓮的同类装置，只不过产出的是熔岩而非水。可以用各种模组的管道抽取熔岩。注意它比水版本慢得多，且并不免费——每桶熔岩大约消耗 25 点灵气。为安全起见，它被禁用了自动填充附近容器的功能——总不能让滚烫的熔岩四处飞溅。

### 核心效果：世界盐

以核心形态存在的世界盐。对拥有转化配方的方块施放即可完成转化，且**不会消耗世界盐本身**，让大规模转化工程对库存的压力小得多。

## 运行需求

- Minecraft 26.1.2
- NeoForge 26.1.2.114 及以上
- **[Thaumaturge](https://www.curseforge.com/minecraft/mc-mods/thaumaturge)** 1.0.2 及以上（必需）

可选：

- **[旅行者背包](https://www.curseforge.com/minecraft/mc-mods/travelers-backpack)** 11.2.8+ —— 使用其奥术工作台与充能板升级
- **[精妙背包](https://www.curseforge.com/minecraft/mc-mods/sophisticated-backpacks)** 3.26.9+ —— 使用其奥术工作台与充能板升级

## 鸣谢

- Lorc：详见[COPYRIGHT.md](src/main/resources/COPYRIGHT.md)
- IReyI：提供俄语翻译

## 许可证

[LGPL-3.0](LICENSE.md)
