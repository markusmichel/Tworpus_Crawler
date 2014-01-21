package crawler;

import java.util.Arrays;
import java.util.Random;

public class ToothBrushSelector {
	
	public static final int MAX_RUNS = Integer.MAX_VALUE;
	
	private String[] colors;
	private int[] frequencies;
	private Random rand;
	
	
	public ToothBrushSelector(String[] colors) {
		init(colors);
	}
	
	private void init(String[] colors) {
		this.rand = new Random();
		this.colors = colors;
		this.frequencies = new int[colors.length];
	}
	
	private void initFrequencies() {
		for(int i=0; i<frequencies.length; i++) {
			frequencies[i] = 0;
		}
	}
	
	public void selecToothBrush() {
		if(frequencies.length == 0) return;
		initFrequencies();
		
		for(int i=0; i < MAX_RUNS; i++) {
			int index = rand.nextInt(frequencies.length);
			frequencies[index]++;
		}
		
		int index = 0;
		int tmp_value = -1;
		
		for(int i=0; i < frequencies.length; i++) {
			if(frequencies[i] > tmp_value) {
				tmp_value = frequencies[i];
				index = i;
			}
		}
		
		Arrays.sort(frequencies);
		
		System.out.println("Selected Color after " + MAX_RUNS + " tries: " + colors[index]);
		
	}

    public static void main(String[] args) {
            System.out.println("Starting ToothBrushSelector");
            ToothBrushSelector selector = new ToothBrushSelector(args);
            selector.selecToothBrush();
    }

}
