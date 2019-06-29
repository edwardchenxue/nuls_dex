package io.nuls.dex.util.bancor;

import io.nuls.dex.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

/**
 * 类描述：
 *
 * @author qinyifeng
 * @version v1.0
 * @date 2019/6/26 21:31
 */
public class BancorFormula {

    /**
     * connector token：准备金代币，如：eth、btc
     * balance：准备金代币余额数量
     * supply： 智能代币总数量
     * CRR：恒定准备金率（Constant Reserve Ratio） = balance / (supply * price) , 0.2 = 200 / (1000*1)
     * price:  balance / (supply * CRR),  1 = 200 / (1000*0.2)
     * balance：= price * supply * CRR , 200 = 1 * 1000 * 0.2
     * smart token：可以连接多个connector token，此时smart token也叫转换token，用于connector token之间的转换
     */
//    static BigInteger ONE = BigInteger.valueOf(1L);
//    static BigInteger supply = BigInteger.valueOf(10000L);
//    static BigInteger balance = BigInteger.valueOf(2500L);
//    static BigInteger price = BigInteger.valueOf(1L);
//    static BigInteger ONE = BigInteger.valueOf(1L);
    static Double supply = 10000d;
    static Double balance = 2500d;
    static Double price = 0.5d;
    static Double cw = 0.5;

    public static Double calculateBuy(Double supply, Double balance, Double cw, Double paid) {
        Double tokens = BancorFormula.mul(supply, Math.pow(1 + BancorFormula.divide(String.valueOf(paid), String.valueOf(balance)), cw) - 1);
        BancorFormula.supply = BancorFormula.add(BancorFormula.supply, tokens);
        BancorFormula.balance = BancorFormula.add(BancorFormula.balance, paid);
        //BancorFormula.price = BancorFormula.divide(paid.toString(), tokens.toString());
        BancorFormula.price = BancorFormula.divide(BancorFormula.balance, (BancorFormula.mul(BancorFormula.supply, cw)));
        System.out.println("buy price: " + price);
        return tokens;
    }

    public static Double calculateSell(Double supply, Double balance, Double cw, Double sellAmount) {
        Double reserve = BancorFormula.mul(balance, 1 - Math.pow(1 - BancorFormula.divide(sellAmount, supply), 1 / cw));
        //BancorFormula.price = BancorFormula.divide(reserve, sellAmount);
        BancorFormula.supply = BancorFormula.subtract(BancorFormula.supply, sellAmount);
        BancorFormula.balance = BancorFormula.subtract(BancorFormula.balance, reserve);
        BancorFormula.price = BancorFormula.divide(BancorFormula.balance, (BancorFormula.mul(BancorFormula.supply, cw)));
        System.out.println("sell price: " + price);
        return reserve;
    }

    public static void main(String[] args) {
        BancorFormula bancor = new BancorFormula();
        Scanner scanner = new Scanner(System.in);
        String input;
        while (scanner.hasNext() && !(input = scanner.nextLine()).equals("0")) {
            String[] payNumers = input.split("-");
            // buy token
            if (StringUtils.isNotBlank(payNumers[0])) {
                Double paid = Double.parseDouble(payNumers[0]);
                if (paid > 0) {
                    System.out.println("buy tokens: " + BancorFormula.calculateBuy(BancorFormula.supply, BancorFormula.balance, BancorFormula.cw, paid));
                }
            }
            // sell token
            if (payNumers.length > 1 && StringUtils.isNotBlank(payNumers[1])) {
                Double sell = Double.parseDouble(payNumers[1]);
                if (sell > 0) {
                    System.out.println("get reserve: " + BancorFormula.calculateSell(BancorFormula.supply, BancorFormula.balance, BancorFormula.cw, sell));
                }
            }
            System.out.println("supply: " + BancorFormula.supply + ", balance: " + BancorFormula.balance + ", price: " + BancorFormula.price);
        }

    }

    /**
     * 提供精确的乘法运算
     *
     * @param dou1
     * @param dou2
     * @return
     */
    protected static double mul(String dou1, String dou2) {
        BigDecimal big1 = new BigDecimal(dou1);
        BigDecimal big2 = new BigDecimal(dou2);
        return big1.multiply(big2).doubleValue();
    }

    protected static double add(double dou1, double dou2) {
        BigDecimal big1 = new BigDecimal(dou1);
        BigDecimal big2 = new BigDecimal(dou2);
        return big1.add(big2).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    protected static double subtract(double dou1, double dou2) {
        BigDecimal big1 = new BigDecimal(dou1);
        BigDecimal big2 = new BigDecimal(dou2);
        return big1.subtract(big2).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    protected static double mul(double dou1, double dou2) {
        BigDecimal big1 = new BigDecimal(dou1);
        BigDecimal big2 = new BigDecimal(dou2);
        return big1.multiply(big2).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    protected static double divide(double dou1, double dou2) {
        BigDecimal big1 = new BigDecimal(dou1);
        BigDecimal big2 = new BigDecimal(dou2);
        return big1.divide(big2, 3, RoundingMode.HALF_UP).doubleValue();
    }

    protected static double divide(String dou1, String dou2) {
        BigDecimal big1 = new BigDecimal(dou1);
        BigDecimal big2 = new BigDecimal(dou2);
        return big1.divide(big2, 3, RoundingMode.HALF_UP).doubleValue();
    }

    //function calculatePurchaseReturn(uint256 _supply, uint256 _connectorBalance, uint32 _connectorWeight, uint256 _depositAmount) public view returns (uint256);
    //function calculateSaleReturn(uint256 _supply, uint256 _connectorBalance, uint32 _connectorWeight, uint256 _sellAmount) public view returns (uint256);

//    public static BigInteger calculateBuy(BigInteger supply, BigInteger balance, Double cw, BigInteger paid) {
//        //Double a = (1 + paid.divide(balance).doubleValue());
//        System.out.println(paid.divideAndRemainder(balance));
//        BigInteger[] ssss=paid.divideAndRemainder(balance);
//        Double ss=ssss[0].add(ssss[1]).doubleValue();
//        System.out.println(ss);
//        //System.out.println((String) (Math.pow(1 + paid.divide(balance).doubleValue(), cw)));
//        new BigInteger("1.1");
//        BigInteger tokens = supply.multiply(new BigInteger(String.valueOf(Math.pow(1 + paid.divide(balance).doubleValue(), cw))).subtract(ONE));
//        return tokens;
//    }
//
//    public static BigInteger calculateSell(BigInteger supply, BigInteger balance, Double cw, BigInteger sellAmount) {
//        //$paid = $balance * (1 - pow(1 - $token / $supply, 1 / $cw));
//        BigInteger tokens = balance.multiply(new BigInteger(String.valueOf(1 - Math.pow(1 - sellAmount.divide(supply).doubleValue(), 1 / cw))));
//        return tokens;
//    }

//    public function buy() {
//        $supply = $this -> get["supply"];
//        $balance = $this -> get["balance"];
//        $paid = $this -> get["paid"];
//        $cw = 0.5;
//
//        $token = $supply * (pow(1 + $paid / $balance, $cw) - 1);
//        $price = $paid / $token;
//
//        ApiFunc::api_export ([
//                "token" = > $token,
//                "priceBefore" =>$balance / $supply / $cw,
//                "priceEnd" =>$price
//        ]);
//    }
//
//    public function sell() {
//        $cw = 0.5;
//        $supply = $this -> get["supply"];
//        $balance = $this -> get["balance"];
//        $token = $this -> get["token"];
//
//        $paid = $balance * (1 - pow(1 - $token / $supply, 1 / $cw));
//        $price = $paid / $token;
//        ApiFunc::api_export ([
//                "paid" = > $paid,
//                "priceBefore" =>$price,
//                "priceEnd" =>($balance - $paid) / ($supply - $token) / $cw
//        ]);
//    }

}