package io.horizontalsystems.bitcoincore.utils;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.util.Arrays;

public class HashUtils {

    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

    /**
     * Get SHA-256 hash.
     */
    public static byte[] sha256(byte[] input) {
        Digest d = new SHA256Digest();
        d.update(input, 0, input.length);
        byte[] out = new byte[d.getDigestSize()];
        d.doFinal(out, 0);
        return out;
    }

    /**
     * Get double SHA-256 hash.
     */
    public static byte[] doubleSha256(byte[] input) {
        byte[] round1 = sha256(input);
        return sha256(round1);
    }

    /**
     * Generate a key using the scrypt key derivation function.
     *
     * @param P     the bytes of the pass phrase.
     * @param S     the salt to use for this invocation.
     * @param N     CPU/Memory cost parameter. Must be larger than 1, a power of 2 and less than
     *              <code>2^(128 * r / 8)</code>.
     * @param r     the block size, must be >= 1.
     * @param p     Parallelization parameter. Must be a positive integer less than or equal to
     *              <code>Integer.MAX_VALUE / (128 * r * 8)</code>.
     * @param dkLen the length of the key to generate.
     * @return the generated key.
     */
    public static byte[] scrypt(byte[] P, byte[] S, int N, int r, int p, int dkLen) {
        return SCrypt.generate(P, S, N, r, p, dkLen);
    }

    /**
     * Convert byte array to hex string.
     */
    public static String toHexString(byte[] b) {
        return toHexString(b, false);
    }

    public static String toHexString(byte[] b, boolean sep) {
        StringBuilder sb = new StringBuilder(b.length << 2);
        for (byte x : b) {
            int hi = (x & 0xf0) >> 4;
            int lo = x & 0x0f;
            sb.append(HEX_CHARS[hi]);
            sb.append(HEX_CHARS[lo]);
            if (sep) {
                sb.append(' ');
            }
        }
        return sb.toString().trim();
    }

    /**
     * Convert byte array (little endian) to hex string.
     */
    public static String toHexStringAsLE(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length << 2);
        for (int i = b.length - 1; i >= 0; i--) {
            byte x = b[i];
            int hi = (x & 0xf0) >> 4;
            int lo = x & 0x0f;
            sb.append(HEX_CHARS[hi]);
            sb.append(HEX_CHARS[lo]);
        }
        return sb.toString();
    }

    public static byte[] toBytesAsLE(String hash) {
        byte[] r = toBytes(hash);
        return Arrays.reverse(r);
    }

    public static byte[] toBytes(String hash) {
        if (hash.length() % 2 == 1) {
            throw new IllegalArgumentException("Invalid hash length.");
        }
        byte[] data = new byte[hash.length() / 2];
        for (int i = 0; i < data.length; i++) {
            char c1 = hash.charAt(2 * i);
            char c2 = hash.charAt(2 * i + 1);
            int n1 = char2int(c1);
            int n2 = char2int(c2);
            int n = n1 << 4 | n2;
            data[i] = (byte) n;
        }
        return data;
    }

    static int char2int(char ch) {
        if (ch >= '0' && ch <= '9') {
            return ch - '0';
        }
        if (ch >= 'a' && ch <= 'f') {
            return ch - 'a' + 10;
        }
        if (ch >= 'A' && ch <= 'F') {
            return ch - 'A' + 10;
        }
        throw new IllegalArgumentException("Bad char.");
    }

}
