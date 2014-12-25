package de.tu.darmstadt.cn2.idb34.util;

import org.apache.commons.math3.complex.Complex;

public class Utils {

    public static Complex[][] convertToComplex(Integer[][] rData,
            Integer[][] iData) {
        final Complex[][] result = new Complex[rData.length][rData[0].length];
        for (int i = 0; i < rData.length; i++) {
            for (int j = 0; j < rData[0].length; j++) {
                result[i][j] = new Complex(rData[i][j].doubleValue(),
                        iData[i][j].doubleValue());
            }
        }
        return result;
    }

    public static Complex[][] conjugate(Complex[][] data) {
        final Complex[][] result = new Complex[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                result[i][j] = data[i][j].conjugate();
            }
        }
        return result;
    }

    public static Complex[][] transpose(Complex[][] data) {
        final Complex[][] result = new Complex[data[0].length][data.length];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = data[j][i];
            }
        }
        return result;
    }

    public static Complex[][] convertToComplex(Integer[][] rData) {
        Integer[][] iData = new Integer[rData.length][rData[0].length];
        for (int i = 0; i < iData.length; i++) {
            for (int j = 0; j < iData[0].length; j++) {
                iData[i][j] = 0;
            }
        }
        return convertToComplex(rData, iData);
    }

    public static Complex[][] rollCols(Complex[][] xcorr) {
        Complex[][] result = new Complex[xcorr.length][xcorr[0].length];
        int shift = (int) Math.ceil(xcorr[0].length / 2.0) + 1;
        for (int i = 0; i < xcorr.length; i++) {
            for (int j = 0; j < xcorr[i].length; j++) {
                result[i][j] = xcorr[i][(int) ((j + shift) % xcorr[i].length)];
            }
        }
        return result;
    }

    public static Complex[][] rollRows(Complex[][] xcorr) {
        Complex[][] result = new Complex[xcorr.length][xcorr[0].length];
        int shift = (int) Math.ceil(xcorr.length / 2.0) + 1;
        for (int i = 0; i < xcorr.length; i++) {
            for (int j = 0; j < xcorr[i].length; j++) {
                result[i][j] = xcorr[(int) ((i + shift) % xcorr.length)][j];
            }
        }
        return result;
    }
}
