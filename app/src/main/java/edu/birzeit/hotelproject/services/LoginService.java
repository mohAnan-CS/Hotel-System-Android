package edu.birzeit.hotelproject.services;

import java.util.List;

import edu.birzeit.hotelproject.models.Customer;
import edu.birzeit.hotelproject.models.Receptionist;

public class LoginService {


    public static boolean isCustomer(String  username, String password, List<Customer>customerList){
            Customer customer=new Customer(username,password);
        for (int i=0;i<customerList.size();i++)
            if (customerList.get(i).equals(customer)) return true;
        return false;
    }

    public static boolean isReceptionist(String username, String password, List<Receptionist> receptionistList){
        Receptionist receptionist=new Receptionist(username,password);
        for (int i = 0; i< receptionistList.size(); i++) if (receptionistList.get(i).equals(receptionist)) return true;
        return false;
    }

    public static boolean isUsernameUsed(String username,List<Customer>customerList){
        for (Customer customer : customerList)
            if (customer.getCustomer_username().equalsIgnoreCase(username)) return true;
        return false;
    }


}
