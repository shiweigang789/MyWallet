package com.topnetwork.wallet.core.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.topnetwork.wallet.entities.BlockchainSetting
import com.topnetwork.wallet.entities.EnabledWallet
import com.topnetwork.wallet.entities.PriceAlertRecord
import com.topnetwork.wallet.entities.Rate

/**
 * @FileName     : AppDatabase
 * @date         : 2020/6/11 10:47
 * @author       : Owen
 * @description  :
 */
@Database(
    version = 15, exportSchema = false, entities = [
        Rate::class,
        EnabledWallet::class,
        PriceAlertRecord::class,
        AccountRecord::class,
        BlockchainSetting::class]
)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun ratesDao(): RatesDao
    abstract fun walletsDao(): EnabledWalletsDao
    abstract fun accountsDao(): AccountsDao
    abstract fun priceAlertsDao(): PriceAlertsDao
    abstract fun blockchainSettingDao(): BlockchainSettingDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "dbBankWallet")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
//                .addMigrations(
//                    MIGRATION_8_9,
//                    MIGRATION_9_10,
//                    MIGRATION_10_11,
//                    renameCoinDaiToSai,
//                    moveCoinSettingsFromAccountToWallet,
//                    storeBipToPreferences,
//                    addBlockchainSettingsTable
//                )
                .build()
        }

    }

}