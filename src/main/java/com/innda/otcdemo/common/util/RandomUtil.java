package com.innda.otcdemo.common.util;

import java.util.Random;

/**
 * @version V3.0
 * @Title: RandomUtil
 * @Company:
 * @Description: 描述
 * * @date 2018/5/23 下午2:26
 */
public class RandomUtil {
    /**
     * 获取随机数 字母数字混合
     *
     * @param length
     */
    public static String createRandomCharData(int length) {
        StringBuilder sb = new StringBuilder();
        //随机用以下三个随机生成器
        Random rand = new Random();
        Random randdata = new Random();
        int data = 0;
        for (int i = 0; i < length; i++) {
            int index = rand.nextInt(3);
            //目的是随机选择生成数字，大小写字母
            switch (index) {
                case 0:
                    //仅仅会生成0~9
                    data = randdata.nextInt(10);
                    sb.append(data);
                    break;
                case 1:
                    //保证只会产生65~90之间的整数
                    data = randdata.nextInt(26) + 65;
                    sb.append((char) data);
                    break;
                case 2:
                    data = randdata.nextInt(26) + 97;
                    ////保证只会产生97~122之间的整数
                    sb.append((char) data);
                    break;
                default:
                    break;
            }
        }
        String result = sb.toString();
        return result;
    }

    /**
     * 创建数字随机数
     *
     * @param length
     */
    public static String createData(int length) {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(rand.nextInt(10));
        }
        String data = sb.toString();
        return data;
    }
}
