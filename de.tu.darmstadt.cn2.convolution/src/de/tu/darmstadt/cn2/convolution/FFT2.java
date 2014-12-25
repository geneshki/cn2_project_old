/**
 * 
 */
package de.tu.darmstadt.cn2.convolution;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

/**
 * A "not so bad but still" implementation of 2-dimensional FFT
 * It calculates first the FT of the columns of a matrix and then of each row of
 * the result.
 * The same is applied for the IFFT.
 * 
 * @author nikola
 * 
 */
public class FFT2 {
    private FastFourierTransformer fft = new FastFourierTransformer(
            DftNormalization.STANDARD);

    /**
     * Constructor
     */
    public FFT2() {
    }

    /**
     * Calculates the 2-dimensional Fourier transform of {@code data}
     * 
     * @param rData
     *            - the 2-dimensional matrix which transform should be
     *            calculated
     * @return - a Fourier Transform of {@code data}
     */
    public Complex[][] fft2(Integer[][] rData) {
        // Complex[][] fftResult =
        // Utils.transpose(Utils.convertToComplex(rData));
        Complex[][] fftResult = Utils.convertToComplex(rData);

        for (int i = 0; i < fftResult.length; i++) {
            fftResult[i] = fft.transform(fftResult[i], TransformType.FORWARD);
        }
        fftResult = Utils.transpose(fftResult);
        for (int i = 0; i < fftResult.length; i++) {
            fftResult[i] = fft.transform(fftResult[i], TransformType.FORWARD);
        }
        fftResult = Utils.transpose(fftResult);
        return fftResult;
    }

    private static Complex[][] scale(Complex[][] data, double factor) {
        Complex[][] result = new Complex[data.length][data[0].length];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                result[i][j] = data[i][j].multiply(factor);
            }
        }
        return result;
    }

    /**
     * Calculates the 2-dimensional inverse Fourier transform of {@code data}
     * 
     * @param data
     *            - the 2-dimensional matrix which transform should be
     *            calculated
     * @return - a Fourier Transform of {@code data}
     */
    public Complex[][] ifft2(Complex[][] inputData) {
        Complex[][] data = Utils.conjugate(Utils.transpose(inputData));
        for (int i = 0; i < data.length; i++) {
            data[i] = fft.transform(data[i], TransformType.FORWARD);
        }
        data = Utils.transpose(data);
        for (int i = 0; i < data.length; i++) {
            data[i] = fft.transform(data[i], TransformType.FORWARD);
        }

        return scale(data, 1.0 / (data.length * data[0].length));
    }

    /**
     * Transpones a 2-dimensional array
     * 
     * @param a
     *            - the two-dimensional array
     * @return - the transponed a
     */
    public static Integer[][] transpone(Integer[][] a) {
        Integer[][] at = new Integer[a[0].length][a.length];
        for (int i = 0; i < a.length; i++) {
            rowToCol(a, at, i);
        }
        return at;
    }

    private static void rowToCol(Integer[][] a, Integer[][] at, int i) {
        for (int j = 0; j < a[0].length; j++) {
            at[j][i] = a[i][j];
        }
    }

    public static Complex[][] transpone(Complex[][] a) {
        Complex[][] at = new Complex[a[0].length][a.length];
        for (int i = 0; i < a.length; i++) {
            rowToCol(a, at, i);
        }
        return at;
    }

    private static void rowToCol(Complex[][] a, Complex[][] at, int i) {
        for (int j = 0; j < a[0].length; j++) {
            at[j][i] = a[i][j];
        }
    }

    // public static void main(String[] args) {
    // Integer[][] a = { { 1, 2, 3 } };
    // Integer[][] b = { { 1, 1 }, { 1, 1 } };
    // Integer[][] c = { { 1, 1, 2, 3 }, { 2, 4, 5, 6 }, { 3, 7, 8, 9 } };
    // Integer[][] t = padToPowerOf2(a);
    // FFT2 fft = new FFT2();
    // Complex[][] result = fft.fft2(t);
    // printArray(result);
    // result = fft.ifft2(result);
    // printArray(result);
    //
    // t = padToPowerOf2(b);
    // result = fft.fft2(t);
    // printArray(result);
    // result = fft.ifft2(result);
    // printArray(result);
    //
    // t = padToPowerOf2(c);
    // result = fft.fft2(t);
    // printArray(result);
    // result = fft.ifft2(result);
    // printArray(result);
    // }

    private static void printArray(Complex[][] result) {
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                System.out.print(result[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static Integer[][] padToPowerOf2(Integer[][] a) {
        Integer m = (int) Math.ceil(Math.log(a.length) / Math.log(2));
        int rows = (int) Math.pow(2, m);
        Integer n = (int) Math.ceil(Math.log(a[0].length) / Math.log(2));
        int cols = (int) Math.pow(2, n);
        Integer[][] result = new Integer[rows][cols];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                result[i][j] = a[i][j];
            }
        }
        return result;
    }

    private static Integer[][] getPaddedMatrix(Integer[][] a, Integer[][] b) {
        int cols_padded = b[0].length;
        int rows_padded = b.length;
        Integer[][] ap = new Integer[a.length + b.length][a[0].length
                + b[0].length];
        for (int i = rows_padded; i < ap.length - rows_padded; i++) {
            for (int j = cols_padded; j < ap[0].length - cols_padded; j++) {
                ap[i][j] = a[i - rows_padded][j - cols_padded];
            }
        }
        return ap;
    }

    public static Integer[][] padToPowerOf2(Integer[][] a, int xpower,
            int ypower) {
        Integer[][] ap = new Integer[(int) Math.round(Math.pow(2, ypower))][(int) Math
                .round(Math.pow(2, xpower))];
        for (int i = 0; i < ap.length; i++) {
            for (int j = 0; j < ap[0].length; j++) {
                if (i < a.length && j < a[0].length) {
                    ap[i][j] = a[i][j];
                } else {
                    ap[i][j] = 0;
                }
            }
        }
        return ap;
    }
}
