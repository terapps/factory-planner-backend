package terapps.factoryplanner.bootstrap.dto

data class FGUnlock(
        val Class: String?,
        val mScannableObjects: String?,
        val mRecipes: String?,
        val mNumInventorySlotsToUnlock: String?,
        val mResourcesToAddToScanner: String?,
        val mResourcePairsToAddToScanner: String?,
        val mEmotes: String?,
        val mSchematics: String?,
        val mNumArmEquipmentSlotsToUnlock: String?,
        val mItemsToGive: String?,
        val mTapeUnlocks: String?
)
