# wallet-eos
 wallet-eos EOS钱包 助记词 私钥 转账
 
## 生成助记词 私钥 公钥 
 ``` Java
 // 生成助记词
 String mnemonic = MnemonicUtils.generateMnemonic();
 System.out.println("mnemonic:"+ mnemonic);

 // 生成种子
 byte[] seed = MnemonicUtils.generateSeed(mnemonic, "");
 System.out.println("seed:"+ Numeric.toHexString(seed));

 // bip44 bip32 私钥
 byte[] privateKeyBytes = KeyPairUtils.generatePrivateKey(seed, KeyPairUtils.CoinTypes.EOS);
 System.out.println("privateKeyBytes:"+ Numeric.toHexString(privateKeyBytes));

 // 生成EOS私钥
 String pk = EccTool.privateKeyFromSeed(privateKeyBytes);
 System.out.println("private key :" + pk);

 // 生成EOS公钥
 String pu = EccTool.privateToPublic(pk);
 System.out.println("public key :" + pu);

 ``` 
 
 mnemonic:unfair raccoon electric valve session fish catch near industry increase pipe nominee<br>
 seed:0xb1c8816b33c7a42afe2f41bbe83d197962eb31de1cbc8ce647c64bf30a803dda859207d2a0cc098229f7874692dee7e3a82af3cfc6b2fe9ec2af32291183cd73<br>
 privateKeyBytes:0xc41e9c899be9e822e918b0206709c24f56ae174a6244676eb71c024cd4a0ad2a<br>
 private key :5JQ5H8ktrPeHyGU4gmEDUTK7jM8Te2QXc4J1NzeQk6ZinASQvCT<br>
 public key :EOS8EzuaoP3kELe2WorLyMwKRD3KNypJzGomXTjg6dB1tGEGitgKt<br>
 
 
``` Java
 String accountA = "a1111111111a", accountB = "a1111111111b";
 String privateKeyA = "5JVubbh5s6RP5zunU8TVEdhQKBTY4BNkbsxP2abbjCQi4HqpUvG", privateKeyB = "5JbZ4RyKGqNNrhRzyDVjRdERbjZMJfe1XTnsLqqB1hXZCLH5Mgd";

 // 随机生成EOS账号
 String account = EosUtils.generateAccount();
 System.out.println("account :" + account + ", eos = " + EosUtils.isEosAccount(account));

 // 查询系统代币总额
 WalletManager.querySupplys();

 // 生成EOS账号（随机）
 WalletManager.generateWalletAddress();

 // 生成EOS账号
 WalletManager.generateWalletAddress("a1111111111b");

 // 查询账号
 WalletManager.queryAccount(accountA);

 WalletManager.queryAccount(accountB);

 // 转账
 WalletManager.transfer(accountA, privateKeyA, accountB, "1.0000 SYS", "xxx");

 WalletManager.up(accountA, privateKeyA, accountB, "1.0000 SYS");
```