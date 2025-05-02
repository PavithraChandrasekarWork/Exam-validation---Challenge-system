package iTestcasesForCaptcha;

public class Test {

	public static void main(String[] args) {
		
//		String a = "1. Question id [ 10651 ]";
//		
//		System.out.println(a.contains(" ["));
		
//		String str="1) 0\r\n"
//				+ "\r\n"
//				+ "2) \r\n"
//				+ "\r\n"
//				+ "\r\n"
//				+ "3) 1\r\n"
//				+ "\r\n"
//				+ "4) None\r\n"
//				+ "इनमें से कोई नहीं";
		
//		String str = "1) (3,4)\r\n"
//	            + "2) (2,3)\r\n"
//	            + "3) (3,3)\r\n"
//	            + "4) (1,1)";
		String str=" \r\n"
				+ "\r\n"
				+ "(A)\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "(B)\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "(C)\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "(D)";
	        
	int dd = str.trim().indexOf("(A)");

	System.out.println(dd);
//	System.out.printf(sheik);)
		

	}

}
