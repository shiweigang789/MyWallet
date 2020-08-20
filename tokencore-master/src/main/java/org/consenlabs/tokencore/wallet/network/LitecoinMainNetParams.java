package org.consenlabs.tokencore.wallet.network;

import com.google.common.base.Preconditions;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Utils;
import org.bitcoinj.net.discovery.HttpDiscovery;
import org.bitcoinj.params.AbstractBitcoinNetParams;

import java.net.URI;

public class LitecoinMainNetParams extends AbstractBitcoinNetParams {
    public static final int MAINNET_MAJORITY_WINDOW = 1000;
    public static final int MAINNET_MAJORITY_REJECT_BLOCK_OUTDATED = 950;
    public static final int MAINNET_MAJORITY_ENFORCE_BLOCK_UPGRADE = 750;
    private static LitecoinMainNetParams instance;

    private LitecoinMainNetParams() {
        this.interval = 2016;
        this.targetTimespan = 1209600;
        this.maxTarget = Utils.decodeCompactBits(486604799L);
        this.dumpedPrivateKeyHeader = 176;
        this.addressHeader = 48;
        this.p2shHeader = 5;
        this.acceptableAddressCodes = new int[]{this.addressHeader, this.p2shHeader};
        this.port = 8333;
        this.packetMagic = 4190024921L;
        this.bip32HeaderPub = 76067358;
        this.bip32HeaderPriv = 76066276;
        this.majorityEnforceBlockUpgrade = 750;
        this.majorityRejectBlockOutdated = 950;
        this.majorityWindow = 1000;
        this.genesisBlock.setDifficultyTarget(486604799L);
        this.genesisBlock.setTime(1231006505L);
        this.genesisBlock.setNonce(2083236893L);
        this.id = "org.bitcoin.production";
        this.subsidyDecreaseBlockCount = 210000;
        this.spendableCoinbaseDepth = 100;
        String genesisHash = this.genesisBlock.getHashAsString();
        Preconditions.checkState(genesisHash.equals("000000000019d6689c085ae165831e934ff763ae46a2a6c172b3f1b60a8ce26f"), genesisHash);
        this.checkpoints.put(91722, Sha256Hash.wrap("00000000000271a2dc26e7667f8419f2e15416dc6955e5a6c6cdf3f2574dd08e"));
        this.checkpoints.put(91812, Sha256Hash.wrap("00000000000af0aed4792b1acee3d966af36cf5def14935db8de83d6f9306f2f"));
        this.checkpoints.put(91842, Sha256Hash.wrap("00000000000a4d0a398161ffc163c503763b1f4360639393e0e4c8e300e0caec"));
        this.checkpoints.put(91880, Sha256Hash.wrap("00000000000743f190a18c5577a3c2d2a1f610ae9601ac046a38084ccb7cd721"));
        this.checkpoints.put(200000, Sha256Hash.wrap("000000000000034a7dedef4a161fa058a2d67a173a90155f3a2fe6fc132e0ebf"));
        this.dnsSeeds = new String[]{"seed.bitcoin.sipa.be", "dnsseed.bluematt.me", "dnsseed.bitcoin.dashjr.org", "seed.bitcoinstats.com", "seed.bitnodes.io", "bitseed.xf2.org", "seed.bitcoin.jonasschnelli.ch", "bitcoin.bloqseeds.net"};
        this.httpSeeds = new HttpDiscovery.Details[]{new HttpDiscovery.Details(ECKey.fromPublicOnly(Utils.HEX.decode("0238746c59d46d5408bf8b1d0af5740fe1a6e1703fcb56b2953f0b965c740d256f")), URI.create("http://httpseed.bitcoin.schildbach.de/peers"))};
        this.addrSeeds = new int[]{500895794, 1648545344, 1389798469, 769107013, -1974219449, 1931884368, -635190697, -321589929, 172026969, 1977409888, 2113365905, -1992881235, -1527638867, 1987510446, 1670054936, -2037772264, -845204690, 19490110, -862790594, -1771628992, 1447591488, 1533473856, -1311760575, -237215679, -1043256510, -2034625981, 1810161475, 1833758531, -779221436, 447787844, 1703191365, -406739899, 1489937477, -975526843, 568173637, 1027298118, 333796167, 276877898, -1906237877, -2125361589, 1820565067, -491976116, -71562676, 2136698444, 1917067854, 318788942, 543753810, 1514714706, 1975895890, 1127599188, 873451860, -2017430443, 142289750, -1978451369, -1406942633, 1882671449, -77343653, -1180604324, 403535452, 1733256285, 80955742, 501500766, 1323869026, 233642851, -1045410963, -1919806079, -1749554558, 486029966, -1857483859, 165442990, -1704101458, -1569883730, 265326510, 255737778, 1335119794, -861648712, 1053649592, -1445616196, -1929307711, 72980437, -905863719, 223505485, -1410221500, 1722919765, 291894370, 1054329773, -1550864786, 1003670445, -747698873, 551516224, 2050193218, 1419742795, -1872611748, -744884894, 726824097, -1897946797, 1006613319, 1886699352, -1219507283, 1532655963, 49949134, 1516714196, -392048066, -275200955, 1259293004, -1564729770, 1989455203, -1532493629, 100509389, -1625155746, 910850395, 1398965717, -1404778028, 184952141, 353120212, 849677772, -42348984, -245564845, 1723866720, 1807902305, -1069856850, 1306139521, 847619265, -2009290387, 903219788, -622815420, 774983009, -2065545265, 1820411218, 974964312, -1730612350, -444282021, 367550551, 2063683662, -665658040, 1446522710, 173929556, -1596879592, 780775752, -1482944180, -874721196, 1428405315, -1451022784, -1979293372, 949273532, -1418281128, 1778485953, -21016765, 791070296, 823144270, -2048316971, 706484062, -60019503, 665865069, -553499056, 370624696, 683566464, 1472095616, 485386106, -1435781310, -1705798069, 173681997, 607438424, -1706471593, 515575368, -166676387, 125751108, 139536572, 1537309208, 667556674, -123729066, -53165481, -818953890, -661135278, 1286447183, 792065749, 942467273, -450652085, 1066690630, 1623768643, 1104831063, -569646782, -1366666165, -269434284, 504330587, -1364031344, -904483623, 508026328, -1936963543, -763508921, -717547933, 1083617635, -1668425384, -887949216, -1636224932, 1086432325, 1251001020, 742016589, 656929340, 816860013, 1493322936, -1601050963, 833637229, -1029880154, 1670598578, -652063298, -1621552936, 2136148587, 1965989013, 1699034440, -1534804410, -832955559, 1896744728, 1184972056, -626044136, -995557549, -2018792878, -1218130095, -1308467894, 1832197697, -1070218434, -1540177566, 1643066542, -781197892, 141617345, 812705985, -1670256447, -1533467805, -2052983197, 1099228505, 416902872, 397990869, 1625804056, -437383092, -1424511902, 511208987, 1562621794, -231174226, -2010709152, -1638187648, 2107971762, -1251919035, 450109246, -716143286, -2045253207, -1839521124, -219601598, 1202652484, 1941284931, -1179148115, 61294800, 175110242, 1721032546, 1006291272, -1911976113, 1953044163, 49516111, 1086001731, 1783943522, 1611153739, -1525450420, 1363845955, 1668814663, -608798373, -318353320, 1121078086, 353925452, -1691937151, 1944387501, 106199119, 1133365573, 1679312550, 756746564, -1800040120, -1925802664, -1515620766, -1955570878, -829291346, -1923595184, 771102037, -289408701, 779483981, 1056504919, -1725885602, -1909390290, -1246778045, -1980961327, -1661157305, -1882386612, -1102449235, 342527301, -1738106184, -1730603170, -777460627, -2087044424, 1084227948, 1831322199, -1753219155, 2074762589, 303283779, -1318591063, 1678764234, -1130453419, 260180556, 252276825, 1772918140, 913146963, -1313200060, -2007502719, 1890958947, -985316021, -1355358511, -1247498158, 569402065, 1923844530, 1956926232, -2095913284, -1189702049, -1968715688, -25231443};
    }

    public static synchronized LitecoinMainNetParams get() {
        if (instance == null) {
            instance = new LitecoinMainNetParams();
        }

        return instance;
    }

    public String getPaymentProtocolId() {
        return "main";
    }
}