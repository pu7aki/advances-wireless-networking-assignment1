# advances-wireless-networking-assignment1

Use java.

Calculate the actual throughput for 802.11a/g/n/ac/ax and for all available data rates, for both UDP and TCP.

• Your program should accept 3 arguments:

  • Protocol (UDP/TCP)
  
  • Standard (802.11a, 802.11g, 802.11n, 802.11ac_w1, 802.11ac_w2, 802.11ax)
  
  • Available data rate for each standard (e.g. 802.11a/g: 54, 48, 36,…)
  
  • Note: For standard .11n/ac, consider SDur = 3.6𝜇s only
  
• Your program must return for each scenario:

  • The actual throughput [Mbps] in the normal case (20MHz and 1SS) AND the best case:
  
   • 40MHz/4SS for .11n
   
   • 80MHz/3SS for .11ac_w1 ; 160MHz/8SS for .11ac_w2
   
   • 160MHz/8SS for .11ax
   
  • The amount of time needed to transfer 10 GB of data.
  
  
• “Readme” file – detailing usage, and explaining:

  • Why there is a difference between the actual throughput and the advertised data rate.
  
  • 802.11 performance improves after each release. Briefly discuss the trade-offs involved in such improvements.
  
