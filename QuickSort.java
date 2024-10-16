import java.util.concurrent.RecursiveAction;

public class QuickSort extends RecursiveAction{

    private int[] arr;
    private int low;
    private int high;

    public QuickSort(int []arr, int low, int high) {
        this.arr = arr;
        this.low = low;
        this.high = high;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private int partition(int[] arr, int l, int h) {
        int pivot = arr[h];
        int pp = l;

        for(int i = l; i <= h-1; i++) {
            if(arr[i] < pivot) {
                swap(arr, pp, i);
                pp = pp + 1;
            }
        }
        swap(arr, pp, h);
        return pp;

    }

    @Override
    protected void compute() {
        if(low < high) {
            int mid = partition(arr, low, high);
            invokeAll(new QuickSort(arr, low, mid-1), new QuickSort(arr, mid+1, high));
        }
    }
}
