package com.topnetwork.wallet

/**
 * @FileName     : IAssetsManager
* @date         : 2020/7/15 14:23
* @author       : Owen
* @description  : 资产管理器
*/
//interface IAssetsManager {
//    /**
//     * 获取所有的钱包集合
//     */
//    fun getWalletMap(): Map<String, List<Wallet>>
//
//    /**
//     * 根据钱包的key获取钱包的列表
//     */
//    fun getWalletListByKey(key:String):List<Wallet>
//
//    /**
//     * 更新钱包的币价
//     */
//    fun updatePrice()
//
//    /**
//     * 更新钱包币价
//     */
//    fun updatePrice(wallet:Wallet)
//
//    /**
//     * 更新钱包余额
//     */
//    fun updateBalance()
//
//    /**
//     * 更新钱包余额
//     */
//    fun updateBalance(wallet:Wallet)
//
//    /**
//     * 更新钱包信息
//     */
//    fun updateWallet(wallet: Wallet)
//
//    /**
//     * 通知更新
//     */
//    fun notifyChanged()
//
//    /**
//     * 获取所有币种的key
//     */
//    fun getKeyList()
//
//    /**
//     * 打开某个资产
//     */
//    fun showCoin(coinInfo: CoinInfo)
//
//    /**
//     * 隐藏某个资产
//     */
//    fun hideCoin(coinInfo: CoinInfo)
//
//    /**
//     * 添加某个钱包
//     */
//    fun addWallet(wallet: Wallet)
//
//    /**
//     * 删除某个钱包
//     */
//    fun deleteWallet(wallet: Wallet)
//
//    /**
//     * 缓存钱包信息
//     */
//    fun updateCache()
//
//    /**
//     * 更新转账记录信息
//     */
//    fun updateWalletTransactionRecord()
//
//}