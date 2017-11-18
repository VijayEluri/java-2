import java.util.Random;

/**
 * Implement key-index counting to sort an array of chars.
 */
public class KeyIndexCounting
{

    static final int R = 26;
    static void sort(char[] a)
    {
        int[] c = new int[R+1];

        // compute frequencies
        for (int i = 0; i < a.length; i++)
        {
            c[a[i] - 'a' +1] +=1;
        }

        // Compute cumulative
        for (int k = 1; k < R; k++)
        {
            c[k] += c[k-1];
        }

        char[] ax = new char[a.length];
        for (int i = 0; i < a.length; i++)
        {
            ax[ c[a[i]- 'a']++ ] = a[i];
        }

        for (int i = 0; i < a.length; i++)
        {
            a[ i ] = ax[i];
        }
    }

    public static void main(String[] args)
    {
        // char array
        char a[] = new char[10];
        Random r = new Random();
        for (int i = 0; i < a.length; i++)
        {
            a[i] = (char) ('a' + r.nextInt(R));
        }

        sort(a);

        for (char x: a)
        {
            System.out.println(x);
        }
    }
}
