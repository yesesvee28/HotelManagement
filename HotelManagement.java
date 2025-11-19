import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.IOException;
class CustomerDetails{
    private String name;
    private long phoneNumber;
    private int age;
    private int days;
    private int category;
    private int roomNo;

    public CustomerDetails(String name,long phoneNumber,int age,int days,int category,int roomNo){
        this.name=name;
        this.phoneNumber=phoneNumber;
        this.age=age;
        this.days=days;
        this.category=category;
        this.roomNo=roomNo;
    }

    public String getName(){
        return name;
    }
    public long getPhoneNumber(){
        return phoneNumber;
    }
    public int getAge(){
        return age;
    }
    public int getDays(){
        return days;
    }
    public int getCategory(){
        return category;
    }
    public int getRoomNo(){
        return roomNo;
    }
}
public class HotelManagement{
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        Map<Integer, List<Integer>> categories = new HashMap<>();
        categories.put(1, Arrays.asList(101, 102, 201, 202, 301, 302, 401, 402, 501, 502));
        categories.put(2, Arrays.asList(103, 104, 203, 204, 303, 304, 403, 404, 503, 504));
        categories.put(3, Arrays.asList(105, 106, 205, 206, 305, 306, 405, 406, 505, 506));
        categories.put(4, Arrays.asList(107, 108, 207, 208, 307, 308, 407, 408, 507, 508));

        Map<Integer,Boolean> roomAvailability=Map.ofEntries(
            Map.entry(101,false),Map.entry(102,false),Map.entry(103,false),Map.entry(104,false),
            Map.entry(105,false),Map.entry(106,false),Map.entry(107,false),Map.entry(108,false), 
            Map.entry(201,false),Map.entry(202,false),Map.entry(203,false),Map.entry(204,false),
            Map.entry(205,false),Map.entry(206,false),Map.entry(207,false),Map.entry(208,false),
            Map.entry(301,false),Map.entry(302,false),Map.entry(303,false),Map.entry(304,false),
            Map.entry(305,false),Map.entry(306,false),Map.entry(307,false),Map.entry(308,false), 
            Map.entry(401,false),Map.entry(402,false),Map.entry(403,false),Map.entry(404,false),
            Map.entry(405,false),Map.entry(406,false),Map.entry(407,false),Map.entry(408,false),
            Map.entry(501,false),Map.entry(502,false),Map.entry(503,false),Map.entry(504,false),
            Map.entry(505,false),Map.entry(506,false),Map.entry(507,false),Map.entry(508,false)
            );
        try{
            File myFile=new File("/Users/yesesvee/Desktop/Hotel_Management/Customer_Details.txt");
            if(myFile.createNewFile()){
                System.out.println("New file created:"+myFile.getName());
            }
            else{
                System.out.println("File already exists");
            }
        }
        catch(IOException e){
            System.out.println("An error occured");
            e.printStackTrace();
        }
        sc.close();
    }
}