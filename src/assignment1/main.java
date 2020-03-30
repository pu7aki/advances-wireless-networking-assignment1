package assignment1;
//YIQIU LEI 16206801
import java.util.Scanner;

public class main {

	public static void main(String[] args) {
		int time = 0;
		while (time < 100) {
			// Input from user:
			Scanner sc = new Scanner(System.in);
			System.out.println("Protocol:(TCP/UDP)"); // Input protocol with prompts
			String protocol = sc.nextLine();
			if (protocol.equals("UDP") || protocol.equals("TCP")) {
				System.out.println("Standard:"); // Input standard with prompts
				System.out.println("(802.11a/ 802.11g/ 802.11n/ 802.11ac_w1/ 802.11ac_w2/ 802.11ax)");
				String standard = sc.nextLine();

				// Input data rate with prompts according to different standard
				if (standard.equals("802.11a") || standard.equals("802.11g")) {
					System.out.println("Data rate");
					System.out.println("(6/9/12/18/24/36/48/54)");
				} else if (standard.equals("802.11n")) {
					System.out.println("Data rate");
					System.out.println("(7.2/14.4/21.7/28.9/43.3/57.8/65/72.2/150)");
				} else if (standard.equals("802.11ac_w1") || standard.equals("802.11ac_w2")) {
					System.out.println("Data rate");
					System.out.println("(7.2/14.4/21.7/28.9/43.3/57.8/65/72.2/86.7/96.3/200/433.3/866.7)");
				} else if (standard.equals("802.11ax")) {
					System.out.println("Data rate");
					System.out.println("(8.6/17.2/25.8/34.4/51.6/68.8/77.4/86/103.2/114.7/129/143.4)");
				} else {
					System.out.println("Wrong standard.");
				}
				double data_rate = sc.nextDouble();

				// initialize some parameters
				all all;
				double throughput_best = 0, throughput_normal = 0, time10g_normal = 0, time10g_best = 0;
				// test part
//					String standard = "802.11ac_w2";
//					double data_rate = 28.9;

				// Use methods to calculate
				if (protocol.equals("UDP")) {
					all = UDP(standard, data_rate);
				} else {
					all = TCP(standard, data_rate);
				}
				throughput_best = throughput(all.getbest());// calculate the best throughput
				throughput_normal = throughput(all.getnormal()); // calculate the normal throughput
				time10g_normal = time10g(all.getnormal()); // calculate the amount of time (normal) needed to transfer
															// 10 GB of data.
				time10g_best = time10g(all.getbest());// calculate the amount of time (best) needed to transfer 10 GB of
														// data.

				// print to console
				if (throughput_normal > 0) {
					System.out.println("-------------------------------------------");
					System.out.println("Results:");
					System.out.println("Time to send 1500 bytes(normal):\t" + all.getnormal() + "\tus");
					System.out.println("Time to send 1500 bytes(best):\t\t" + all.getbest() + "\tus");
					System.out.println("Normal Throughput(normal):\t\t" + throughput_normal + "\tMbps");
					System.out.println("Normal Throughput(best):\t\t" + throughput_best + "\tMbps");
					System.out.println("Time for 10 GB(normal):\t\t\t" + time10g_normal + "\ts");
					System.out.println("Time for 10 GB(best):\t\t\t" + time10g_best + "\ts");
				} else {
					System.out.println("");
				}
			} else {
				System.out.println("Wrong protocol.");
			}
			System.out.println("Do you want to do the next calculation? (y/n)");
			Scanner ac = new Scanner(System.in);
			String next = ac.nextLine();
			if (next.equals("y") || next.equals("Y")) {
				time++;
			} else {
				System.exit(0);
			}
		}
	}

	// calculate actual throughput
	public static double throughput(double all) {
		return 1500 * 8 / all;
	}

	// calculate the amount of time needed to transfer 10 GB of data.
	public static double time10g(double all) {
		long g10 = 10737418240L;//10g=10*1024*1024*1024 bytes
		return g10 / ((1500 / all) * 1000000);
	}

	// When protocol is UDP
	public static all UDP(String standard, double data_rate) {
		// parameters
		double SIFS = 0, Preamble_normal = 0, Preamble_best = 0, Slot = 0;
		double ACK_normal = 0, ACK_best = 0;
		double CTS_normal = 0, CTS_best = 0;
		double RTS_normal = 0, RTS_best = 0;
		double all_normal = 0, all_best = 0;
		double Data_normal = 0, Data_best = 0, each = 0;
		double nbits = 0, crate = 0, nchan_normal = 0, nchan_best = 0;
		int Nss_normal = 0, Nss_best = 0, datasymbol = 0, tail = 0, MAC_header = 0, SNAP_LLC_header = 0, signal_exe = 0;
		// when use different standard
		if (standard.equals("802.11a")) {
			SIFS = 16; // SIFS = 16u
			Slot = 9; // Time slot=9u
			Preamble_normal = 20; // Preamble=20u
			Preamble_best = 20;
			each = 4;
			datasymbol = 1500;// data: 1500bytes
			tail = 6;// tail: 6bits
			MAC_header = 34;// MAC header: 34bytes
			SNAP_LLC_header = 8;// SNAP LLC header: 8bytes
			nchan_normal = 48;// Number of sub-channels :48
			nchan_best = 48;
			// choose Bits per symbol and Coding Rate according to data rate
			if (data_rate == 6) {
				nbits = 1;
				crate = 0.5;
			} else if (data_rate == 9) {
				nbits = 1;
				crate = 0.75;
			} else if (data_rate == 12) {
				nbits = 2;
				crate = 0.5;
			} else if (data_rate == 18) {
				nbits = 2;
				crate = 0.75;
			} else if (data_rate == 24) {
				nbits = 4;
				crate = 0.5;
			} else if (data_rate == 36) {
				nbits = 4;
				crate = 0.75;
			} else if (data_rate == 48) {
				nbits = 6;
				crate = 2.0 / 3.0;
			} else if (data_rate == 54) {
				nbits = 6;
				crate = 0.75;
			} else {
				System.out.println("Wrong Data rate!");
			}
			// Time for 1500 bytes data
			Data_normal = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor(nbits * crate * nchan_normal));
			Data_best = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor(nbits * crate * nchan_best));
			RTS_normal = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			CTS_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			ACK_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			RTS_best = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			CTS_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			ACK_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
		} else if (standard.equals("802.11g")) {
			SIFS = 10; // SIFS = 10u
			Slot = 9; // Time slot=9u
			Preamble_normal = 20; // Preamble= 20u
			Preamble_best = 20;
			each = 4;
			datasymbol = 1500;// data: 1500bytes
			tail = 6;// tail: 6bits
			MAC_header = 34;// MAC header: 34bytes
			SNAP_LLC_header = 8;// SNAP LLC header: 8bytes
			signal_exe = 6;// signal extension :6u (only here)
			nchan_normal = 48;// Number of sub-channels :48
			nchan_best = 48;
			// choose Bits per symbol and Coding Rate according to data rate
			if (data_rate == 6) {
				nbits = 1;
				crate = 0.5;
			} else if (data_rate == 9) {
				nbits = 1;
				crate = 0.75;
			} else if (data_rate == 12) {
				nbits = 2;
				crate = 0.5;
			} else if (data_rate == 18) {
				nbits = 2;
				crate = 0.75;
			} else if (data_rate == 24) {
				nbits = 4;
				crate = 0.5;
			} else if (data_rate == 36) {
				nbits = 4;
				crate = 0.75;
			} else if (data_rate == 48) {
				nbits = 6;
				crate = 2.0 / 3.0;
			} else if (data_rate == 54) {
				nbits = 6;
				crate = 0.75;
			} else {
				System.out.println("Wrong Data rate!");
			}
			// Time for 1500 bytes data
			Data_normal = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor(nbits * crate * nchan_normal));
			Data_best = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor(nbits * crate * nchan_best));
			RTS_normal = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			CTS_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			ACK_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			RTS_best = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			CTS_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			ACK_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
		} else if (standard.equals("802.11n")) {
			SIFS = 16; // SIFS =16u
			Slot = 9; // Time slot=9u
			Preamble_best = 46; // Preamble best=46u
			Preamble_normal = 20;// Preamble normal =20u
			datasymbol = 1500;// 1500bytes
			tail = 6;// 6bits
			MAC_header = 40;// 40bytes
			SNAP_LLC_header = 8;// 8bytes
			each = 3.6;
			nchan_normal = 52;
			nchan_best = 108;
			if (data_rate == 7.2) {
				nbits = 1;
				crate = 0.5;
			} else if (data_rate == 14.4) {
				nbits = 2;
				crate = 0.5;
			} else if (data_rate == 21.7) {
				nbits = 2;
				crate = 0.75;
			} else if (data_rate == 28.9) {
				nbits = 4;
				crate = 0.5;
			} else if (data_rate == 43.3) {
				nbits = 4;
				crate = 0.75;
			} else if (data_rate == 57.8) {
				nbits = 6;
				crate = 2.0 / 3.0;
			} else if (data_rate == 65) {
				nbits = 6;
				crate = 0.75;
			} else if (data_rate == 72.2) {
				nbits = 6;
				crate = 5.0 / 6.0;
			} else {
				System.out.println("Wrong Data rate!");
			}
			// distinguish Nss (normal and best)
			Nss_normal = 1;
			Data_normal = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor(nbits * crate * nchan_normal * Nss_normal));
			Nss_best = 4;
			Data_best = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor(nbits * crate * nchan_best * Nss_best));
			RTS_normal = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			CTS_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			ACK_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			RTS_best = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			CTS_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			ACK_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
		} else if (standard.equals("802.11ac_w1")) {

			SIFS = 16; // 16u
			Slot = 9; // 9u
			Preamble_normal = 20; // 56.8u
			Preamble_best = 56.8; // 56.8u
			datasymbol = 1500;// 1500bytes
			tail = 6;// 6bits
			MAC_header = 40;// 40bytes
			SNAP_LLC_header = 8;// 8bytes
			each = 3.6;
			nchan_normal = 52;
			nchan_best = 234;
			if (data_rate == 7.2) {
				nbits = 1;
				crate = 0.5;
			} else if (data_rate == 14.4) {
				nbits = 2;
				crate = 0.5;
			} else if (data_rate == 21.7) {
				nbits = 2;
				crate = 0.75;
			} else if (data_rate == 28.9) {
				nbits = 4;
				crate = 0.5;
			} else if (data_rate == 43.3) {
				nbits = 4;
				crate = 0.75;
			} else if (data_rate == 57.8) {
				nbits = 6;
				crate = 2.0 / 3.0;
			} else if (data_rate == 65) {
				nbits = 6;
				crate = 0.75;
			} else if (data_rate == 72.2) {
				nbits = 6;
				crate = 5.0 / 6.0;
			} else if (data_rate == 86.7) {
				nbits = 8;
				crate = 0.75;
			} else if (data_rate == 96.3) {
				nbits = 8;
				crate = 5.0 / 6.0;
			} else {
				System.out.println("Wrong Data rate!");
			}

			Nss_normal = 1;
			Data_normal = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor(nbits * crate * nchan_normal * Nss_normal));
			Nss_best = 3;
			Data_best = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor(nbits * crate * nchan_best * Nss_best));
			RTS_normal = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			CTS_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			ACK_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			RTS_best = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			CTS_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			ACK_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
		} else if (standard.equals("802.11ac_w2")) {
			SIFS = 16; // 16u
			Slot = 9; // 9u
			Preamble_best = 92.8; // 92.8u
			Preamble_normal = 20;
			datasymbol = 1500;// 1500bytes
			tail = 6;// 6bits
			MAC_header = 40;// 40bytes
			SNAP_LLC_header = 8;// 8bytes
			each = 3.6;
			nchan_normal = 52;
			nchan_best = 468;
			if (data_rate == 7.2) {
				nbits = 1;
				crate = 0.5;
			} else if (data_rate == 14.4) {
				nbits = 2;
				crate = 0.5;
			} else if (data_rate == 21.7) {
				nbits = 2;
				crate = 0.75;
			} else if (data_rate == 28.9) {
				nbits = 4;
				crate = 0.5;
			} else if (data_rate == 43.3) {
				nbits = 4;
				crate = 0.75;
			} else if (data_rate == 57.8) {
				nbits = 6;
				crate = 2.0 / 3.0;
			} else if (data_rate == 65) {
				nbits = 6;
				crate = 0.75;
			} else if (data_rate == 72.2) {
				nbits = 6;
				crate = 5.0 / 6.0;
			} else if (data_rate == 86.7) {
				nbits = 8;
				crate = 0.75;
			} else if (data_rate == 96.3) {
				nbits = 8;
				crate = 5.0 / 6.0;
			} else {
				System.out.println("Wrong Data rate!");
			}

			Nss_normal = 1;
			Data_normal = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor(nbits * crate * nchan_normal * Nss_normal));
			Nss_best = 8;
			Data_best = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor(nbits * crate * nchan_best * Nss_best));
			RTS_normal = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			CTS_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			ACK_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			RTS_best = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			CTS_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			ACK_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
		} else if (standard.equals("802.11ax")) {
			each = 13.6;
			SIFS = 16; // 16u
			Slot = 9; // 9u
			Preamble_normal = 20;
			Preamble_best = 92.8;
			datasymbol = 1500;// 1500bytes
			tail = 6;// 6bits
			MAC_header = 40;// 40bytes
			SNAP_LLC_header = 8;// 8bytes
			nchan_normal = 234;
			nchan_best = 1960;
			if (data_rate == 8.6) {
				nbits = 1;
				crate = 0.5;
			} else if (data_rate == 25.8) {
				nbits = 2;
				crate = 0.75;
			} else if (data_rate == 51.6) {
				nbits = 4;
				crate = 0.75;
			} else if (data_rate == 86) {
				nbits = 6;
				crate = 5.0 / 6.0;
			} else if (data_rate == 114.7) {
				nbits = 8;
				crate = 5.0 / 6.0;
			} else if (data_rate == 143.4) {
				nbits = 10;
				crate = 5.0 / 6.0;
			} else if (data_rate == 17.2) {
				nbits = 2;
				crate = 0.5;
			} else if (data_rate == 34.4) {
				nbits = 4;
				crate = 0.5;
			} else if (data_rate == 68.8) {
				nbits = 6;
				crate = 2.0 / 3.0;
			} else if (data_rate == 77.4) {
				nbits = 6;
				crate = 0.75;
			} else if (data_rate == 103.2) {
				nbits = 8;
				crate = 0.75;
			} else if (data_rate == 129) {
				nbits = 10;
				crate = 0.75;
			} else {
				System.out.println("Wrong Data rate!");
			}
			Nss_normal = 1;
			Data_normal = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor(nbits * crate * nchan_normal * Nss_normal));
			Nss_best = 8;
			Data_best = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor(nbits * crate * nchan_best * Nss_best));
			RTS_normal = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			CTS_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			ACK_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			RTS_best = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			CTS_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			ACK_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
		} else {
			System.out.println("Wrong standard!");
		}
		// calculate the whole time to send 1500 bytes(best and normal)
		double DIFS = SIFS + 2 * Slot;
		all_normal = DIFS + Preamble_normal + RTS_normal * each + SIFS + Preamble_normal + CTS_normal * each + SIFS
				+ Preamble_normal + Data_normal + SIFS + Preamble_normal + ACK_normal * each + 4 * signal_exe;
		all_best = DIFS + Preamble_best + RTS_normal * each + SIFS + Preamble_best + CTS_normal * each + SIFS
				+ Preamble_best + Data_best + SIFS + Preamble_best + ACK_normal * each + 4 * signal_exe;

		all all_en = new all(all_normal, all_best);// all_normal,all_best
		return all_en;
	}

	// When protocol is TCP
	public static all TCP(String standard, double data_rate) {
		int TCP_ACK = 40;// 40bytes
		double SIFS = 0, Preamble_normal = 0, Preamble_best = 0, Slot = 0;
		double ACK_normal = 0, ACK_best = 0;
		double CTS_normal = 0, CTS_best = 0;
		double RTS_normal = 0, RTS_best = 0;
		double all_normal = 0, all_best = 0;
		double Data_normal = 0, Data_best = 0, each = 0;
		double nbits = 0, crate = 0, nchan_normal = 0, nchan_best = 0;
		int Nss_normal = 0, Nss_best = 0, datasymbol = 0, tail = 0, MAC_header = 0, SNAP_LLC_header = 0, signal_exe = 0;
		// when use different standard
		if (standard.equals("802.11a")) {
			SIFS = 16; // SIFS = 16u
			Slot = 9; // Time slot=9u
			Preamble_normal = 20; // Preamble=20u
			Preamble_best = 20;
			each = 4;
			datasymbol = 1500;// data: 1500bytes
			tail = 6;// tail: 6bits
			MAC_header = 34;// MAC header: 34bytes
			SNAP_LLC_header = 8;// SNAP LLC header: 8bytes
			nchan_normal = 48;// Number of sub-channels :48
			nchan_best = 48;
			Nss_normal = 1;
			Nss_best = 1;
			// choose Bits per symbol and Coding Rate according to data rate
			if (data_rate == 6) {
				nbits = 1;
				crate = 0.5;
			} else if (data_rate == 9) {
				nbits = 1;
				crate = 0.75;
			} else if (data_rate == 12) {
				nbits = 2;
				crate = 0.5;
			} else if (data_rate == 18) {
				nbits = 2;
				crate = 0.75;
			} else if (data_rate == 24) {
				nbits = 4;
				crate = 0.5;
			} else if (data_rate == 36) {
				nbits = 4;
				crate = 0.75;
			} else if (data_rate == 48) {
				nbits = 6;
				crate = 2.0 / 3.0;
			} else if (data_rate == 54) {
				nbits = 6;
				crate = 0.75;
			} else {
				System.out.println("Wrong Data rate!");
			}
			// Time for 1500 bytes data
			Data_normal = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor((nbits * crate * nchan_normal)));
			Data_best = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor(nbits * crate * nchan_best));
			RTS_normal = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			CTS_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			ACK_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			RTS_best = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			CTS_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			ACK_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
		} else if (standard.equals("802.11g")) {
			SIFS = 10; // SIFS = 10u
			Slot = 9; // Time slot=9u
			Preamble_normal = 20; // Preamble= 20u
			Preamble_best = 20;
			each = 4;
			datasymbol = 1500;// data: 1500bytes
			tail = 6;// tail: 6bits
			MAC_header = 34;// MAC header: 34bytes
			SNAP_LLC_header = 8;// SNAP LLC header: 8bytes
			signal_exe = 6;// signal extension :6u (only here)
			nchan_normal = 48;// Number of sub-channels :48
			nchan_best = 48;
			Nss_normal = 1;
			Nss_best = 1;
			// choose Bits per symbol and Coding Rate according to data rate
			if (data_rate == 6) {
				nbits = 1;
				crate = 0.5;
			} else if (data_rate == 9) {
				nbits = 1;
				crate = 0.75;
			} else if (data_rate == 12) {
				nbits = 2;
				crate = 0.5;
			} else if (data_rate == 18) {
				nbits = 2;
				crate = 0.75;
			} else if (data_rate == 24) {
				nbits = 4;
				crate = 0.5;
			} else if (data_rate == 36) {
				nbits = 4;
				crate = 0.75;
			} else if (data_rate == 48) {
				nbits = 6;
				crate = 2.0 / 3.0;
			} else if (data_rate == 54) {
				nbits = 6;
				crate = 0.75;
			} else {
				System.out.println("Wrong Data rate!");
			}
			// Time for 1500 bytes data
			Data_normal = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor(nbits * crate * nchan_normal));
			Data_best = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor(nbits * crate * nchan_best));
			RTS_normal = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			CTS_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			ACK_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			RTS_best = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			CTS_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			ACK_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
		} else if (standard.equals("802.11n")) {
			SIFS = 16; // SIFS =16u
			Slot = 9; // Time slot=9u
			Preamble_best = 46; // Preamble best=46u
			Preamble_normal = 20;// Preamble normal =20u
			datasymbol = 1500;// 1500bytes
			tail = 6;// 6bits
			MAC_header = 40;// 40bytes
			SNAP_LLC_header = 8;// 8bytes
			each = 3.6;
			nchan_normal = 52;
			nchan_best = 108;
			if (data_rate == 7.2) {
				nbits = 1;
				crate = 0.5;
			} else if (data_rate == 14.4) {
				nbits = 2;
				crate = 0.5;
			} else if (data_rate == 21.7) {
				nbits = 2;
				crate = 0.75;
			} else if (data_rate == 28.9) {
				nbits = 4;
				crate = 0.5;
			} else if (data_rate == 43.3) {
				nbits = 4;
				crate = 0.75;
			} else if (data_rate == 57.8) {
				nbits = 6;
				crate = 2.0 / 3.0;
			} else if (data_rate == 65) {
				nbits = 6;
				crate = 0.75;
			} else if (data_rate == 72.2) {
				nbits = 6;
				crate = 5.0 / 6.0;
			} else {
				System.out.println("Wrong Data rate!");
			}
			// distinguish Nss (normal and best)
			Nss_normal = 1;
			Data_normal = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor(nbits * crate * nchan_normal * Nss_normal));
			Nss_best = 4;
			Data_best = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor(nbits * crate * nchan_best * Nss_best));
			RTS_normal = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			CTS_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			ACK_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			RTS_best = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			CTS_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			ACK_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
		} else if (standard.equals("802.11ac_w1")) {

			SIFS = 16; // 16u
			Slot = 9; // 9u
			Preamble_normal = 20; // 56.8u
			Preamble_best = 56.8; // 56.8u
			datasymbol = 1500;// 1500bytes
			tail = 6;// 6bits
			MAC_header = 40;// 40bytes
			SNAP_LLC_header = 8;// 8bytes
			each = 3.6;
			nchan_normal = 52;
			nchan_best = 234;
			if (data_rate == 7.2) {
				nbits = 1;
				crate = 0.5;
			} else if (data_rate == 14.4) {
				nbits = 2;
				crate = 0.5;
			} else if (data_rate == 21.7) {
				nbits = 2;
				crate = 0.75;
			} else if (data_rate == 28.9) {
				nbits = 4;
				crate = 0.5;
			} else if (data_rate == 43.3) {
				nbits = 4;
				crate = 0.75;
			} else if (data_rate == 57.8) {
				nbits = 6;
				crate = 2.0 / 3.0;
			} else if (data_rate == 65) {
				nbits = 6;
				crate = 0.75;
			} else if (data_rate == 72.2) {
				nbits = 6;
				crate = 5.0 / 6.0;
			} else if (data_rate == 86.7) {
				nbits = 8;
				crate = 0.75;
			} else if (data_rate == 96.3) {
				nbits = 8;
				crate = 5.0 / 6.0;
			} else {
				System.out.println("Wrong Data rate!");
			}

			Nss_normal = 1;
			Data_normal = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor(nbits * crate * nchan_normal * Nss_normal));
			Nss_best = 3;
			Data_best = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor(nbits * crate * nchan_best * Nss_best));
			RTS_normal = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			CTS_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			ACK_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			RTS_best = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			CTS_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			ACK_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
		} else if (standard.equals("802.11ac_w2")) {
			SIFS = 16; // 16u
			Slot = 9; // 9u
			Preamble_best = 92.8; // 92.8u
			Preamble_normal = 20;
			datasymbol = 1500;// 1500bytes
			tail = 6;// 6bits
			MAC_header = 40;// 40bytes
			SNAP_LLC_header = 8;// 8bytes
			each = 3.6;
			nchan_normal = 52;
			nchan_best = 468;
			if (data_rate == 7.2) {
				nbits = 1;
				crate = 0.5;
			} else if (data_rate == 14.4) {
				nbits = 2;
				crate = 0.5;
			} else if (data_rate == 21.7) {
				nbits = 2;
				crate = 0.75;
			} else if (data_rate == 28.9) {
				nbits = 4;
				crate = 0.5;
			} else if (data_rate == 43.3) {
				nbits = 4;
				crate = 0.75;
			} else if (data_rate == 57.8) {
				nbits = 6;
				crate = 2.0 / 3.0;
			} else if (data_rate == 65) {
				nbits = 6;
				crate = 0.75;
			} else if (data_rate == 72.2) {
				nbits = 6;
				crate = 5.0 / 6.0;
			} else if (data_rate == 86.7) {
				nbits = 8;
				crate = 0.75;
			} else if (data_rate == 96.3) {
				nbits = 8;
				crate = 5.0 / 6.0;
			} else {
				System.out.println("Wrong Data rate!");
			}

			Nss_normal = 1;
			Data_normal = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor(nbits * crate * nchan_normal * Nss_normal));
			Nss_best = 8;
			Data_best = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor(nbits * crate * nchan_best * Nss_best));
			RTS_normal = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			CTS_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			ACK_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			RTS_best = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			CTS_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			ACK_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
		} else if (standard.equals("802.11ax")) {
			each = 13.6;
			SIFS = 16; // 16u
			Slot = 9; // 9u
			Preamble_normal = 20;
			Preamble_best = 92.8;
			datasymbol = 1500;// 1500bytes
			tail = 6;// 6bits
			MAC_header = 40;// 40bytes
			SNAP_LLC_header = 8;// 8bytes
			nchan_normal = 234;
			nchan_best = 1960;
			if (data_rate == 8.6) {
				nbits = 1;
				crate = 0.5;
			} else if (data_rate == 25.8) {
				nbits = 2;
				crate = 0.75;
			} else if (data_rate == 51.6) {
				nbits = 4;
				crate = 0.75;
			} else if (data_rate == 86) {
				nbits = 6;
				crate = 5.0 / 6.0;
			} else if (data_rate == 114.7) {
				nbits = 8;
				crate = 5.0 / 6.0;
			} else if (data_rate == 143.4) {
				nbits = 10;
				crate = 5.0 / 6.0;
			} else if (data_rate == 17.2) {
				nbits = 2;
				crate = 0.5;
			} else if (data_rate == 34.4) {
				nbits = 4;
				crate = 0.5;
			} else if (data_rate == 68.8) {
				nbits = 6;
				crate = 2.0 / 3.0;
			} else if (data_rate == 77.4) {
				nbits = 6;
				crate = 0.75;
			} else if (data_rate == 103.2) {
				nbits = 8;
				crate = 0.75;
			} else if (data_rate == 129) {
				nbits = 10;
				crate = 0.75;
			} else {
				System.out.println("Wrong Data rate!");
			}
			Nss_normal = 1;
			Data_normal = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor(nbits * crate * nchan_normal * Nss_normal));
			Nss_best = 8;
			Data_best = each * Math.ceil((((datasymbol + MAC_header + SNAP_LLC_header) * 8) + tail)
					/ Math.floor(nbits * crate * nchan_best * Nss_best));
			RTS_normal = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			CTS_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			ACK_normal = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_normal));
			RTS_best = Math.ceil((20 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			CTS_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
			ACK_best = Math.ceil((14 * 8 + 6) / Math.floor(nbits * crate * nchan_best));
		} else {
			System.out.println("Wrong standard!");
		}

		// calculate the whole time to send 1500 bytes(best and normal)
		double DIFS = SIFS + 2 * Slot;
		all_normal = DIFS + Preamble_normal + RTS_normal * each + SIFS + Preamble_normal + CTS_normal * each + SIFS
				+ Preamble_normal + Data_normal + SIFS + Preamble_normal + ACK_normal * each + DIFS + Preamble_normal
				+ RTS_normal * each + SIFS + Preamble_normal + CTS_normal * each + SIFS + Preamble_normal
				+ each * Math.ceil((((TCP_ACK + MAC_header + SNAP_LLC_header) * 8) + tail)
						/ Math.floor(nbits * crate * nchan_normal * Nss_normal))
				+ SIFS + Preamble_normal + ACK_normal * each + 8 * signal_exe;

		all_best = DIFS + Preamble_best + RTS_best * each + SIFS + Preamble_best + CTS_best * each + SIFS
				+ Preamble_best + Data_best + SIFS + Preamble_best + ACK_best * each + DIFS + Preamble_best
				+ RTS_best * each + SIFS + Preamble_best + CTS_best * each + SIFS + Preamble_best
				+ each * Math.ceil((((TCP_ACK + MAC_header + SNAP_LLC_header) * 8) + tail)
						/ Math.floor(nbits * crate * nchan_best * Nss_best))
				+ SIFS + Preamble_best + ACK_best * each + 8 * signal_exe;
		// test
//		System.out.println(nbits);
//		System.out.println(crate);
//		System.out.println(nchan_best);
//		System.out.println(Nss_best);
//		System.out.println(Math.floor(nbits * crate * nchan_best * Nss_best));
		all all_en = new all(all_normal, all_best);
		return all_en;

	}

}
