package assignment1;

class all {

	private double all_best;
	private double all_normal;

	// constructor without parameters
	public all() {

	}

	// constructor with parameters
	public all(double all_normal, double all_best) {
		this.all_normal = all_normal;
		this.all_best = all_best;
	}

	// create methods to get two parameters
	public double getnormal() {
		return all_normal;
	}

	public double getbest() {
		return all_best;
	}
}