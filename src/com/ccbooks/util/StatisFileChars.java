package com.ccbooks.util;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;

/**
 * ʹ��StreamTokenizer4ͳ���ļ��е��ַ���
 * StreamTokenizer ���ȡ������������Ϊ����ǡ�������һ�ζ�ȡһ���ǡ�
 * ��������һ��������������Ϊ����״̬�ı�־���ơ�
 * ����ı����������ʶ���ʶ�����֡����õ��ַ�͸���ע����ʽ��
 * 
 *  Ĭ������£�StreamTokenizer��Ϊ����������Token: ��ĸ�����֡���C��C++ע�ͷ������������š�
 *  ����"/"����Token��ע�ͺ������Ҳ���ǣ���"\"��Token������ź�˫����Լ����е����ݣ�ֻ������һ��Token��
 *  ͳ�������ַ���ĳ��򣬲��Ǽ򵥵�ͳ��Token������´󼪣���Ϊ�ַ������Token������Token�Ĺ涨��
 *  ����е����ݾ�����10ҳҲ��һ��Token�����ϣ����ź�����е����ݶ�����Token��Ӧ�õ�������Ĵ��룺
 *     st.ordinaryChar('\'');
 * st.ordinaryChar('\"');
 */
public class StatisFileChars {

	

   

	/**
     * ͳ���ַ���
     * @param fileName �ļ���
     * @return    �ַ���
     */
    public static long statis(String fileName) {

        FileReader fileReader = null;
        try {
            fileReader = new FileReader(fileName);
            //����������ַ���ı�������
            StreamTokenizer st = new StreamTokenizer(new BufferedReader(
                    fileReader));

            //ordinaryChar����ָ���ַ�����ڴ˱����������ǡ���ͨ���ַ�
            //����ָ������š�˫��ź�ע�ͷ������ͨ�ַ�
            st.ordinaryChar('\'');
            st.ordinaryChar('\"');
            st.ordinaryChar('/');

            String s;
            int numberSum = 0;
            int wordSum = 0;
            int symbolSum = 0;
            int total = 0;
            //nextToken������ȡ��һ��Token.
            //TT_EOFָʾ�Ѷu���ĩβ�ĳ���
            while (st.nextToken() != StreamTokenizer.TT_EOF) {
                //�ڵ��� nextToken ����֮��ttype�ֶν���ն�ȡ�ı�ǵ�����
                switch (st.ttype) {
                //TT_EOLָʾ�Ѷu���ĩβ�ĳ���
                case StreamTokenizer.TT_EOL:
                    break;
                //TT_NUMBERָʾ�Ѷu�һ�����ֱ�ǵĳ�
                case StreamTokenizer.TT_NUMBER:
                    //���ǰ�����һ�����֣�nval�ֶν�������ֵ�ֵ
                    s = String.valueOf((st.nval));
                    System.out.println(s);
                    numberSum += s.length();
                    break;
                //TT_WORDָʾ�Ѷu�һ�����ֱ�ǵĳ�
                case StreamTokenizer.TT_WORD:
                    //���ǰ�����һ�����ֱ�ǣ�sval�ֶΰ�һ��������ֱ�ǵ��ַ���ַ�
                    s = st.sval;
                    wordSum += s.length();
                    break;
                default:
                    //�������3�����Ͷ����ǣ���ΪӢ�ĵı����
                    s = String.valueOf((char) st.ttype);
                    symbolSum += s.length();
                }
            }
            System.out.println("sum of number = " + numberSum);
            System.out.println("sum of word = " + wordSum);
            System.out.println("sum of symbol = " + symbolSum);
            total = symbolSum + numberSum + wordSum;
            System.out.println("Total = " + total);
            return total;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

   
}