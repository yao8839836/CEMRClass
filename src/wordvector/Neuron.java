package wordvector;

public abstract class Neuron implements Comparable<Neuron> {
	public int freq;
	public Neuron parent;
	public int code;

	@Override
	public int compareTo(Neuron o) {
		// TODO Auto-generated method stub
		if (this.freq > o.freq) {
			return 1;
		} else {
			return -1;
		}
	}

}
