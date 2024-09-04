
        data class FGSchematic(
val ClassName: String,
val FullName: String,
val mCost: String,
val mDependenciesBlocksSchematicAccess: String,
val mDescription: String,
val mDisplayName: String,
val mHiddenUntilDependenciesMet: String,
val mIncludeInBuilds: String,
val mMenuPriority: Float,
val mRelevantEvents: String,
val mRelevantShopSchematics: String,
val mSchematicDependencies: Set<Map<String, Any>>,
val mSchematicIcon: String,
val mSmallSchematicIcon: String,
val mSubCategories: String,
val mTechTier: Int,
val mTimeToComplete: Float,
val mType: String,
val mUnlocks: Set<Map<String, Any>>,
val mUnlockDescription: String? = null,
val mUnlockIconBig: String? = null,
val mUnlockIconCategory: String? = null,
val mUnlockIconSmall: String? = null,
val mUnlockName: String? = null
): GameEntity()
    