package com.topnetwork.wallet.core.factories

import com.topnetwork.wallet.core.IAddressParser
import com.topnetwork.wallet.core.utils.AddressParser
import com.topnetwork.wallet.entities.Coin
import com.topnetwork.wallet.entities.CoinType

class AddressParserFactory {
    fun parser(coin: Coin): IAddressParser {
        return when (coin.type) {
            is CoinType.Bitcoin -> AddressParser("bitcoin", true)
            is CoinType.Litecoin -> AddressParser("litecoin", true)
            is CoinType.BitcoinCash -> AddressParser("bitcoincash", false)
            is CoinType.Dash -> AddressParser("dash", true)
            is CoinType.Ethereum, is CoinType.Erc20 -> AddressParser("ethereum", true)
            is CoinType.Eos -> AddressParser("eos", true)
            is CoinType.Binance -> AddressParser("binance", true)
        }
    }

}
