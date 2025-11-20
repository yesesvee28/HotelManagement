import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class CustomerDetails{
    //Created a class of customer details(name,phone number,age,room no,leaving date) which can be used to 
    // view the customer details. 
    String name;
    long phoneNumber;
    int age;
    int roomNo;
    String leavingDate;
    
    public CustomerDetails(String name,long phoneNumber,int age,int roomNo,String leavingDate){
        this.name=name;
        this.phoneNumber=phoneNumber;
        this.age=age;
        this.roomNo=roomNo;
        this.leavingDate=leavingDate;
    }
}

class RoomAvailability{
    //Created a room availability class so that we can check how many rooms are currently available and 
    // leaving date is stored in local date time format to check and replace the room availability after 
    // customer leaves.
    int roomNo;
    boolean isAvailable;
    LocalDateTime leavingDate;

    public RoomAvailability(int roomNo){
        this.roomNo=roomNo;
        this.isAvailable=true;
        this.leavingDate=null;
    }

}
public class HotelManagement{
    public static final Map<Integer,Map<Integer,List<Integer>>> floorCategoryRooms=new HashMap<>();
    //floorCategoryRooms is a hashmap where 1st map contains key as floor(1,2,3,4,5) and its values are 
    // map containing key as categories(1-Non AC,2-AC,3-Beach view Non-AC,4-Beach view AC) and its 
    // values are room nos on each floor which match the category(1-1(Non-AC)-[101,102])
    public static final Map<Integer,RoomAvailability> roomAvailabilityStatus=new HashMap<>();
    //roomAvailabilityStatus is a hashmap where integer is room no(101) and RoomAvailabile is a newly created object 
    // of room no(101) which has isAvailable-true and leavingDate=null.
    public static final List<CustomerDetails> customer=new ArrayList<>();
    //It stores the list of customers details.
    
    
    public static void main(String[] args) throws IOException{
        initializeRooms();
        displayFloorCategoryRooms();
    }
    public static void initializeRooms(){
        for(int floor=1;floor<=5;floor++){
            Map<Integer,List<Integer>> categoryRooms=new HashMap<>();
            categoryRooms.put(1,Arrays.asList(floor*100+1,floor*100+2));
            categoryRooms.put(2,Arrays.asList(floor*100+3,floor*100+4));
            categoryRooms.put(3,Arrays.asList(floor*100+5,floor*100+6));
            categoryRooms.put(4,Arrays.asList(floor*100+7,floor*100+8));
            floorCategoryRooms.put(floor,categoryRooms);

            for(List<Integer> rooms:categoryRooms.values()){
                for(int room:rooms){
                    roomAvailabilityStatus.put(room,new RoomAvailability(room));
                }
            }
        }
    }
    public static void displayFloorCategoryRooms(){
        for(Map.Entry<Integer,Map<Integer,List<Integer>>> floorEntry:floorCategoryRooms.entrySet()){
            int floor=floorEntry.getKey();
            Map<Integer,List<Integer>> categories=floorEntry.getValue();
            for(Map.Entry<Integer,List<Integer>> categoryEntry:categories.entrySet()){
                int category=categoryEntry.getKey();
                List<Integer> rooms=categoryEntry.getValue();
                System.out.println("Floor:"+floor+" Category:"+category+" Rooms:"+rooms);
            }
        }
    }
}