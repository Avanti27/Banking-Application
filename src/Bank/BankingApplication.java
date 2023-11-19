package Bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

//simport database.BankingApplicationn.MinimumBalanceException;


public class BankingApplication {
    
	static Connection conn;
	
	public static void connectDb(){
			try {
			Class.forName("com.mysql.cj.jdbc.Driver");		
			String url="jdbc:mysql://localhost:3306/Banking_Project";
			String username="root";
			String password="root";		
    		 conn= DriverManager.getConnection(url,username,password);
			}catch(Exception e) {
				e.printStackTrace();
			}

	}
    public static void disconnectDb() {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			}
	
    public static class MinimumBalanceException extends Exception{
		public MinimumBalanceException() {
			System.out.println("=====================================================");
			System.err.println("You have not sufficient balance ");
			System.out.println("======================================================");
		}
	}
		    		   
    
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.println("********************************************************************************************");
		System.out.println("...................................Welcome to HDFC Bank....................................");
		System.out.println("********************************************************************************************");
		System.out.println();
	   
		while (true) {
		System.out.println("                                Select Your Choice To Proceed                              ");
		System.out.println("1. Open A Bank Account");
		System.out.println("2. Perform Transaction For An Account");
		System.out.println("3. Exit");
	   	
		int op = s.nextInt();				
		if(op==3) {
			System.out.println("========================================================================================");
			System.out.println("..........................Thank You For Using HDFC BANK................................");
			System.out.println("========================================================================================");
			break;
		}
		 else{	
				String name;
				String address;
				String phoneno;
				String DOB;
				int amount;
				int pin;
			  	
		    connectDb();
		    PreparedStatement ps = null;
		    ResultSet rs = null;
		    try {
			switch (op) {
			case 1:			
			    System.out.print("Enter Name: ");
			    name = s.next();
			    System.out.println("Enter Your Address: ");			    
			    address = s.next();
			    System.out.println("Enter your Date of birth (YYYY-MM-DD)");
		    	DOB =s.next();	
		    	LocalDate dateOfBirth=LocalDate.parse(DOB);
			    System.out.println("Enter your Phone Number");
		    	phoneno =s.next();
		    	System.out.println("Enter your opening Bank amount which is Not less than 5000");
		    	amount =s.nextInt();
		    	if(amount<5000) {
					 throw new MinimumBalanceException(); 
				  } 
		    	System.out.println("Enter pin to generate");
		    	pin =s.nextInt();
			    
		
				ps=conn.prepareStatement("insert into bankDetails(name,address,DOB,phoneno,amount,pin) values(?,?,?,?,?,?)");		   			    
			    ps.setString(1, name);
			    ps.setString(2,address);
		    	ps.setString(3, DOB);
		    	ps.setString(4, phoneno);
		    	ps.setInt(5, amount);
		    	ps.setInt(6, pin);
			    
		    	int val = ps.executeUpdate();			    
				if(val>0) {
					System.out.println("===========================================================");
					System.out.println("            Your Account Is Successfully Created");
					System.out.println("===========================================================");
					
			    }
			    else {
			    	System.out.println("===========================================================");
			    	System.err.println("              Your Account Is Not Created");
			    	System.out.println("===========================================================");
			    }	
				break;
			case 2:
				System.out.println("===========================================================");
				System.out.println("             Perform Transactions For An Account           ");	
				System.out.println("===========================================================");
			  long deposit;
	    	  long withdraw;
	    	  long currentbalance;     
			  
			     System.out.println("Enter Your Choice: ");
			     System.out.println("1. Display The Number Of Transactions And Closing Balance");
				 System.out.println("2. Deposit Money");
				 System.out.println("3. Withdraw Money");
				 System.out.println("4. Change Account Holder Name: ");
				 System.out.println("5. Main Menu");
				 Scanner ss = new Scanner(System.in);
				 int choice = ss.nextInt();
				 if (choice>5) {
					 System.out.println("=================================================================");
	    			  System.out.println("                      Invalid choice                      ");
	    			  System.out.println("================================================================");
	    			  System.out.println();
	    			  System.out.println();
				 }
	    		else {	  
				    switch (choice) {
				    case 1:	    				  
	    				  System.out.println("Enter your pin");
	    				  pin=s.nextInt();
	    				  
	    				  ps=conn.prepareStatement("select * from bankDetails where pin=?");
	    				  ps.setInt(1, pin);
	    				  rs=ps.executeQuery();
	    				  rs.next();
	    				  System.out.println("''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''");
					  System.out.println("AccountNumber\t Name\t\t PhoneNumber");
					  System.out.println(rs.getInt(1)+"\t\t"
							             +rs.getString(2)+"\t\t"
							             +rs.getString(5));
					  System.out.println("'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''");
	    				
	    				  ps=conn.prepareStatement("select * from transaction where pin=?");
	    				  ps.setInt(1, pin);
	    				  rs=ps.executeQuery();
	    				  
	    				  int count=0;
	    				  while(rs.next()) {
	    					  System.out.println("--------------------------------------------------------");
	    					  System.out.println("DepositeBalance\t WithdrawBalance\t CurrentBalance");
	    					  System.out.println(rs.getLong(1)+"\t\t\t"
	    							             +rs.getLong(2)+"\t\t\t"
	    							             +rs.getLong(3));
	    					  System.out.println("--------------------------------------------------------");
	    					  count++;
	    				  }System.out.println("Total number of transections::"+count);
	    				  break;

				    case 2:
				    	  System.out.println("Enter amount to deposit");
  				          deposit=s.nextLong();
  				          System.out.println("Enter your Phone Number");
  				          phoneno=s.next();
  				          System.out.println("Enter your pin");
  				          pin=s.nextInt();
  				  
  				          ps=conn.prepareStatement("Select pin from bankDetails where phoneno=?");
				          ps.setString(1, phoneno);
				          rs=ps.executeQuery();
				          rs.next();
				          int pin1=rs.getInt(1);
				          
				          ps=conn.prepareStatement("Select amount from bankDetails where phoneno=?");
	    				  ps.setString(1, phoneno);
	    				  rs=ps.executeQuery();
	    				  rs.next();
		    			  int amount1=rs.getInt(1);
		    			  		    			 
    					  if(pin==pin1) {
    						  withdraw=0;			    					  
        					  currentbalance=amount1+deposit;
        					  
        					  ps=conn.prepareStatement("update bankDetails set amount=? where phoneno=?");
	    					  ps.setLong(1, currentbalance);
	    					  ps.setNString(2, phoneno);
	    					  ps.executeUpdate();
	    					  
	    					  ps=conn.prepareStatement("insert into transaction(deposit,withdraw,currentbalance,pin) values(?,?,?,?)");
	    					  ps.setLong(1, deposit);
	    					  ps.setLong(2, withdraw);
	    					  ps.setLong(3, currentbalance);
	    					  ps.setLong(4, pin);
	    					  int val1=ps.executeUpdate();
	    					  
	    					  if(val1>0) {
	    						    System.out.println("===========================================================");
	    					    	System.out.println("               Your Amount is Deposited");
	    					    	System.out.println("===========================================================");
	    					      }
	    					  else {
	    						    System.out.println("===========================================================");
	    						    System.err.println("               Your Amount is Not Deposited");
	    						    System.out.println("===========================================================");
	    						  }
    					    }
    					  else
						        System.err.println("Enter a Wrong Pin");
	    				  break;
				    
				    case 3:
	    				  System.out.println("Enter amount to withdraw");
	    				  withdraw=s.nextLong();
	    				  System.out.println("Enter your Phone Number");
	    				  phoneno=s.next();
	    				  System.out.println("Enter your pin");
	    				  pin=s.nextInt();
	    				  
	    				  ps=conn.prepareStatement("Select pin from bankDetails where phoneno=?");
	    				  ps.setString(1, phoneno);
	    				  rs=ps.executeQuery();
	    				  rs.next();
	    				  int pin11=rs.getInt(1);
	    				  
	    				  ps=conn.prepareStatement("Select amount from bankDetails where phoneno=?");
	    				  ps.setString(1, phoneno);
	    				  rs=ps.executeQuery();
	    				  rs.next();
		    			  int amount11=rs.getInt(1);
	    				  
	    				  if(pin==pin11) {
	    					  deposit=0;	    					  
	    					  currentbalance=amount11-withdraw;
	    					  try {
	    						  if(currentbalance<1000) {
	    							 throw new MinimumBalanceException(); 
	    						  }
	    							  	    						  	    					  
	    					  ps=conn.prepareStatement("update bankDetails set amount=? where phoneno=?");
	    					  ps.setLong(1, currentbalance);
	    					  ps.setNString(2, phoneno);
	    					  ps.executeUpdate();
	    					  
	    					  ps=conn.prepareStatement("insert into transaction(deposit,withdraw,currentbalance,pin) values(?,?,?,?)");
	    					  ps.setLong(1, deposit);
	    					  ps.setLong(2, withdraw);
	    					  ps.setLong(3, currentbalance);
	    					  ps.setLong(4, pin);
	    					  int val1=ps.executeUpdate();
	    					  
	    					  if(val1>0) {
	    						  System.out.println("============================================================");
	    						  System.out.println("                  Your Amount is withdrawed Successfully");
	    						  System.out.println("============================================================");
	    					  }else {
	    						  System.out.println("============================================================");
	    						  System.err.println("                  Fail to withdraw amount");
	    						  System.out.println("============================================================");
	    						  }
	    					  }catch(MinimumBalanceException e) {
	    						  System.out.println(e.getMessage());
	    					  }
	    				  }else {
						        System.out.println("===========================================================");
						        System.err.println("                You Have Enter a Wrong Pin");
						        System.out.println("===========================================================");
					     }
	    				  break;
	    			  case 4:    				  
	    				  System.out.println("Enter a phoneno");
	    				  phoneno= s.next();
	    				  System.out.println("Change your name");
	    				  String name1= s.next();
	    				  
	    				  ps=conn.prepareStatement("update bankDetails set name=? where phoneno=?");
	    				  ps.setString(1, name1);
	    				  ps.setString(2, phoneno);
	    				  val=ps.executeUpdate();
	    				   if(val>0) {
	    					   System.out.println("============================================================");
	    					   System.out.println("                         Updated");
	    					   System.out.println("============================================================");
	    				   }
	    				   else {
	    					   System.out.println("============================================================");
	    					   System.err.println("                 Not updated/ phoneno not found");
	    					   System.out.println("============================================================");
	    				   }
	    				  break;
	    			   case 5:
	    				  break;		    		
				    }
	    		}
			}disconnectDb();
		 }catch(Exception e) {
				e.printStackTrace();
			}	   
		}
	     
	}
}
}

	





   
	

