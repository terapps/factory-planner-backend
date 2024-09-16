package terapps.factoryplanner.bootstrap.dto.generated

import terapps.factoryplanner.bootstrap.dto.GameEntity

data class FGObjectScanner(
        val `ClassName`: String,
        val `mAngleToClosestObject`: Double,
        val `mArmAnimation`: String,
        val `mAttachSocket`: String,
        val `mBackAnimation`: String,
        val `mBeepDelayMax`: Double,
        val `mBeepDelayMin`: Double,
        val `mClosestObjectInScanRange`: String,
        val `mComponentNameToFirstPersonMaterials`: String,
        val `mCostToUse`: String,
        val `mDefaultEquipmentActions`: Int,
        val `mDetectionRange`: Double,
        val `mEquipMontage`: String,
        val `mEquipmentLookAtDescOverride`: String,
        val `mEquipmentSlot`: String,
        val `mHasStingerMontage`: String,
        val `mMontageBlendOutTime`: Double,
        val `mNeedsDefaultEquipmentMappingContext`: String,
        val `mNormalizedDistanceToClosestObject`: Double,
        val `mReceivedDamageModifiers`: String,
        val `mScannerCycleLeftMontage`: String,
        val `mScannerCycleRightMontage`: String,
        val `mStingerMontage`: String,
        val `mSwappedOutThirdPersonMaterials`: String,
        val `mUnEquipMontage`: String,
        val `mUpdateClosestObjectTime`: Double
) : GameEntity()
    