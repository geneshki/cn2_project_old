/**
 * 
 */
package de.tu.darmstadt.cn2.convolution;

import org.apache.commons.math3.complex.Complex;

/**
 * @author nikola
 *
 */
public class Signal {

    public static Integer[][] convolve2(Integer[][] a, Integer[][] b) {
        Integer[][] ap = getPaddedMatrix(a, b);
        Integer[][] c = new Integer[a.length + b.length - 1][a[0].length
                + b[0].length - 1];
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < c[0].length; j++) {
                c[i][j] = 0;
            }
        }
        for (int k = 0; k < c.length; k++) {
            for (int l = 0; l < c[0].length; l++) {
                calculateElement(b, ap, c, k, l);
            }
        }
        return c;
    }

    private static void calculateElement(Integer[][] b, Integer[][] ap,
            Integer[][] c, int k, int l) {
        for (int m = 0; m < b[0].length; m++) {
            for (int n = 0; n < b.length; n++) {
                c[k][l] += ap[n + k][m + l] * b[n][m];
            }
        }
    }

    private static Integer[][] getPaddedMatrix(Integer[][] a, Integer[][] b) {
        int cols_padded = b[0].length - 1;
        int rows_padded = b.length - 1;
        Integer[][] ap = new Integer[a.length + 2 * (rows_padded)][a[0].length
                + 2 * (cols_padded)];
        for (int i = 0; i < ap.length; i++) {
            for (int j = 0; j < ap[0].length; j++) {
                ap[i][j] = 0;
            }
        }
        for (int i = rows_padded; i < ap.length - rows_padded; i++) {
            for (int j = cols_padded; j < ap[0].length - cols_padded; j++) {
                ap[i][j] = a[i - rows_padded][j - cols_padded];
            }
        }
        return ap;
    }

    public static Integer[][] xcorr2(Integer[][] a, Integer[][] b) {
        return convolve2(a, b);
    }

    public static Complex[][] xcorr_fft2(Integer[][] a, Integer[][] b) {
        // 0.) pad the arrays to the same size (which is a power of 2)
        int ypower = (int) Math.ceil(Math.log(a.length + b.length - 1)
                / Math.log(2));

        int xpower = (int) Math.ceil(Math.log(a[0].length + b[0].length - 1)
                / Math.log(2));
        Integer[][] ap2 = FFT2.padToPowerOf2(a, xpower, ypower);
        Integer[][] bp2 = FFT2.padToPowerOf2(b, xpower, ypower);
        // bp2 = flipud(fliplr(bp2));
        FFT2 fft2;
        Complex[][] fft2aRes;
        Complex[][] fft2bRes;
        fft2 = new FFT2();
        fft2aRes = fft2.fft2(ap2);
        fft2bRes = fft2.fft2(bp2);

        // 1.) multiply fourier transforms (fft2bRes should be conjugated)
        Complex[][] fftProduct = multiplyMatrices(fft2aRes,
                Utils.conjugate(fft2bRes));
        // 2.) perform ifft on the product
        Complex[][] xcorr = fft2.ifft2(fftProduct);
        // 4.) return
        return xcorr;
    }

    // private static double[][] flipud(double[][] a) {
    // final double[][] flipped = new double[a.length][a[0].length];
    // for (int i = 0; i < a.length; i++) {
    // for (int j = 0; j < a[0].length; j++) {
    // flipped[i][j] = a[a.length - i - 1][j];
    // }
    // }
    // return flipped;
    // }
    //
    // private static double[][] fliplr(double[][] a) {
    // final double[][] flipped = new double[a.length][a[0].length];
    // for (int i = 0; i < a.length; i++) {
    // for (int j = 0; j < a[0].length; j++) {
    // flipped[i][j] = a[i][a[0].length - j - 1];
    // }
    // }
    // return flipped;
    // }

    // private static double[][][] conj(double[][][] a) {
    // double[][][] conj = new double[a.length][a[0].length][a[0][0].length];
    // for (int i = 0; i < a[0].length; i++) {
    // for (int j = 0; j < a[0][0].length; j++) {
    // conj[1][i][j] = -a[1][i][j];
    // }
    // }
    // return conj;
    // }

    private static Complex[][] multiplyMatrices(Complex[][] a, Complex[][] b) {
        Complex[][] product = new Complex[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                product[i][j] = a[i][j].multiply(b[i][j]);
            }
        }
        return product;
    }

    // private static double[][] transpose(double[][] a) {
    // double[][] at = new double[a[0].length][a.length];
    // for (int i = 0; i < a.length; i++) {
    // rowToCol(a, at, i);
    // }
    // return at;
    // }

    // private static void rowToCol(double[][] a, double[][] at, int i) {
    // for (int j = 0; j < a[0].length; j++) {
    // at[j][i] = a[i][j];
    // }
    // }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Integer[][] a = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        Integer[][] b = { { 1, 1 }, { 1, 1 } };
        Integer[][] c = { { 1, 1, 2, 3 }, { 2, 4, 5, 6 }, { 3, 7, 8, 9 } };
        long startTime = System.nanoTime();
        Complex[][] result_fft = xcorr_fft2(b, c);
        long endTime = System.nanoTime();
        printArray(result_fft);
        long time = endTime - startTime;
        System.out.println("Time fourier:" + time);
        startTime = System.nanoTime();
        Integer[][] result_conv = xcorr2(b, c);
        endTime = System.nanoTime();
        time = endTime - startTime;
        printArray(result_conv);
        System.out.println("Time convolution:" + time);
    }

    private static void printArray(Integer[][] ap) {
        for (int i = 0; i < ap.length; i++) {
            for (int j = 0; j < ap[0].length; j++) {
                System.out.print(ap[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void printArray(Complex[][] ap) {
        for (int i = 0; i < ap.length; i++) {
            for (int j = 0; j < ap[0].length; j++) {
                System.out.print(ap[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}