package cn.merryyou.blockchain;

import java.security.Provider;
import java.security.Security;

public class Main {
    public static void main(String[] args) {
        Provider[] providers = Security.getProviders();
        for(Provider provider : providers) {
            System.out.println("-------" + provider.getName() + "------------");
        }
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
        for(Provider provider : providers) {
            System.out.println("-------1111----" + provider.getName() + "-----11111------------");
        }
    }
}
