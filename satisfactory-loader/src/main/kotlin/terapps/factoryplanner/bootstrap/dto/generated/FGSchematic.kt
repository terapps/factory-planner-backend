package terapps.factoryplanner.bootstrap.dto.generated

import terapps.factoryplanner.bootstrap.dto.GameEntity

data class FGSchematic(
        val `ClassName`: String,
        val `FullName`: String,
        val `mCost`: String,
        val `mDependenciesBlocksSchematicAccess`: String,
        val `mDescription`: String,
        val `mDisplayName`: String,
        val `mHiddenUntilDependenciesMet`: String,
        val `mIncludeInBuilds`: String,
        val `mIsPlayerSpecific`: String,
        val `mMenuPriority`: Double,
        val `mRelevantEvents`: String,
        val `mRelevantShopSchematics`: String,
        val `mSchematicDependencies`: Set<Map<String, Any>>,
        val `mSchematicIcon`: String,
        val `mSchematicUnlockTag`: String,
        val `mSmallSchematicIcon`: String,
        val `mStatisticGameplayTag`: String,
        val `mSubCategories`: String,
        val `mTechTier`: Int,
        val `mTimeToComplete`: Double,
        val `mType`: String,
        val `mUnlocks`: Set<Map<String, Any>>,
        val `mUnlockDescription`: String? = null,
        val `mUnlockIconBig`: String? = null,
        val `mUnlockIconCategory`: String? = null,
        val `mUnlockIconSmall`: String? = null,
        val `mUnlockName`: String? = null
) : GameEntity()
    