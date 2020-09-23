package com.example.energyefficience;

public class MergeSortImplementationIterative {
    private int[] a;
    private int[] b;    // Hilfsarray
    private int n;

    public void sort(int[] a)
    {
        this.a=a;
        n=a.length;
        b=new int[n/2];
        mergesort();
    }

    private void mergesort()
    {
        int m, s;
        for (s=1; s<n; s+=s)
            for (m=n-1-s; m>=0; m-=s+s)
                merge(max(m-s+1, 0), m, m+s);
    }

    void merge(int lo, int m, int hi)
    {
        int i, j, k;

        i=0; j=lo;
        // vordere Hälfte von a in Hilfsarray b kopieren
        while (j<=m)
            b[i++]=a[j++];

        i=0; k=lo;
        // jeweils das nächstgrößte Element zurückkopieren
        while (k<j && j<=hi)
            if (b[i]<=a[j])
                a[k++]=b[i++];
            else
                a[k++]=a[j++];

        // Rest von b falls vorhanden zurückkopieren
        while (k<j)
            a[k++]=b[i++];
    }

    private int max(int a, int b)
    {
        return a>b? a: b;
    }
}
