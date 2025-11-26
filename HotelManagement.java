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

    @Override
    public String toString(){
        return name+","+phoneNumber+","+age+","+roomNo+","+leavingDate;
    }

    public static CustomerDetails fromString(String line){
        String[] parts=line.split(",");
        return new CustomerDetails(
            parts[0],
            Long.parseLong(parts[1]),
            Integer.parseInt(parts[2]),
            Integer.parseInt(parts[3]),
            parts[4]
        );
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

    public String toString(DateTimeFormatter format){
        String leavingStr=(leavingDate==null)? "null": leavingDate.format(format);
        return roomNo+","+isAvailable+","+leavingStr;
    }

    public static RoomAvailability fromString(String line,DateTimeFormatter format){
        String[] parts=line.split(",");
        RoomAvailability r=new RoomAvailability(Integer.parseInt(parts[0]));
        r.isAvailable=Boolean.parseBoolean(parts[1]);
        r.leavingDate=(parts[2].equals("null"))? null:LocalDateTime.parse(parts[2],format);
        return r;
    }
}
public class HotelManagement{
    //floorCategoryRooms is a hashmap where 1st map contains key as floor(1,2,3,4,5) and its values are 
    // map containing key as categories(1-Non AC,2-AC,3-Beach view Non-AC,4-Beach view AC) and its 
    // values are room nos on each floor which match the category(1-1(Non-AC)-[101,102])
    public static final Map<Integer,Map<Integer,List<Integer>>> floorCategoryRooms=new HashMap<>();
    
    //roomAvailabilityStatus is a hashmap where integer is room no(101) and RoomAvailabile is a newly created object 
    // of room no(101) which has isAvailable-true and leavingDate=null.
    public static final Map<Integer,RoomAvailability> roomAvailabilityStatus=new HashMap<>();
    
    //It stores the list of customers details.
    public static final List<CustomerDetails> customers=new ArrayList<>();
    
    //It stores the String names of categories.
    public static final Map<Integer,String> roomCategory=new HashMap<>();

    //Defining customers file name
    public static final String CUSTOMER_FILE="customers.csv";
    //Defining room file name
    public static final String ROOM_FILE="rooms.csv";
    //Defining date time format
    public static final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    
    public static void main(String[] args) throws IOException{
        //Initialises the rooms
        initializeRooms();
        //Displays the categories and rooms each floor has
        // availableRooms();
        loadCustomersFromFile();
        loadRoomsFromFile();


        Scanner sc=new Scanner(System.in);

        while(true){
            System.out.println("Enter customer Details:");

            System.out.print("Enter a name:");
            String name=sc.nextLine();
            System.out.print("Enter Phone number:");
            long phoneNumber=Long.parseLong(sc.nextLine());
            System.out.print("Enter age:");
            int age=Integer.parseInt(sc.nextLine());
            if(age<18){
                System.out.println("You must be 18 or above to book a room.");
                break;
            }
            boolean isRoomAllocated=false;
            while(!isRoomAllocated){
                System.out.print("Enter a Floor(1-5):");
                int floor=Integer.parseInt(sc.nextLine());
                if(floor<1||floor>5){
                    System.out.println("Floor not available.Enter available floor");
                    continue;
                }
                availableRooms(floor);
                System.out.print("Enter a Category(1:Non AC, 2:AC, 3:Beach View Non AC,4:Beach View AC):");
                int category=Integer.parseInt(sc.nextLine());
                if(category<1||category>4){
                    System.out.println("Category not available.Enter available category");
                    continue;
                }
                System.out.print("Enter Days of stay:");
                int days=Integer.parseInt(sc.nextLine());
                LocalDateTime leavingDate=LocalDateTime.now().plusDays(days);
                Optional<Integer> assignedRoom=allocateRoom(floor,category,leavingDate);
                if(assignedRoom.isPresent()){
                    int roomNo=assignedRoom.get();
                    System.out.println("Room assigned:"+roomNo);
                    CustomerDetails c=new CustomerDetails(name, phoneNumber, age, roomNo,
                    leavingDate.toLocalDate().toString());
                    customers.add(c);
                    isRoomAllocated=true;
                    saveCustomerToFile(c);
                    saveRoomToFile();
                }
                else{
                    System.out.println(
                    "No rooms available for this floor and category.Enter another floor and category.");
                }
            }
            System.out.print("If you want to exit type 'exit':");
            String s=sc.nextLine();
            if(s.equalsIgnoreCase("exit")){
                break;
            }
        }
        sc.close();
    }
    private static void initializeRooms(){
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
        roomCategory.put(1,"Non AC Rooms");
        roomCategory.put(2,"AC Rooms");
        roomCategory.put(3,"Beach View Non AC Rooms");
        roomCategory.put(4,"Beach View AC Rooms");
    }
    // private static void availableRooms(){
    //     for(Map.Entry<Integer,Map<Integer,List<Integer>>> floorEntry:floorCategoryRooms.entrySet()){
    //         int floor=floorEntry.getKey();
    //         System.out.println("Floor:"+floor);
    //         Map<Integer,List<Integer>> categories=floorEntry.getValue();
    //         for(Map.Entry<Integer,List<Integer>> categoryEntry:categories.entrySet()){
    //             int category=categoryEntry.getKey();
    //             System.out.print(roomCategory.get(category)+":");
    //             List<Integer> rooms=categoryEntry.getValue();
    //             for(int room:rooms){
    //                 if(roomAvailabilityStatus.get(room).isAvailable){
    //                     System.out.print(room+" ");
    //                 }
    //             }
    //             System.out.println();
    //         }
    //     }
    // }
    private static void availableRooms(int f){
        for(Map.Entry<Integer,Map<Integer,List<Integer>>> floorEntry:floorCategoryRooms.entrySet()){
            int floor=floorEntry.getKey();
            if(floor!=f){
                continue;
            }
            Map<Integer,List<Integer>> categories=floorEntry.getValue();
            for(Map.Entry<Integer,List<Integer>> categoryEntry:categories.entrySet()){
                int category=categoryEntry.getKey();
                System.out.print(roomCategory.get(category)+":");
                List<Integer> rooms=categoryEntry.getValue();
                for(int room:rooms){
                    if(roomAvailabilityStatus.get(room).isAvailable){
                        System.out.print(room+" ");
                    }
                }
                System.out.println();
            }
        }
    }
    private static Optional<Integer> allocateRoom(int floor,int category,LocalDateTime leavingDate){
        List<Integer> rooms=floorCategoryRooms.get(floor).get(category);
        for(int room:rooms){
            RoomAvailability availability=roomAvailabilityStatus.get(room);
            if(availability.isAvailable){
                availability.isAvailable=false;
                availability.leavingDate=leavingDate;
                return Optional.of(room);
            }
        }
        return Optional.empty();
    }

    //File I/O methods
    private static void saveCustomerToFile(CustomerDetails c) throws IOException{
        try(PrintWriter out=new PrintWriter(new FileWriter(CUSTOMER_FILE,true))){
            out.println(c);
        }
    }

    private static void loadCustomersFromFile() throws IOException{
        File file=new File(CUSTOMER_FILE);
        if(!file.exists())return;
        try(BufferedReader in=new BufferedReader(new FileReader(file))){
            String line;
            while((line=in.readLine())!=null){
                customers.add(CustomerDetails.fromString(line));
            }
        }
    }

    private static void saveRoomToFile() throws IOException{
        try(PrintWriter out=new PrintWriter(new FileWriter(ROOM_FILE))){
            for(RoomAvailability r:roomAvailabilityStatus.values()){
                out.println(r.toString(dtFormatter));
            }
        }
    }

    private static void loadRoomsFromFile() throws IOException{
        File file=new File(ROOM_FILE);
        if(!file.exists())return;
        try(BufferedReader in=new BufferedReader(new FileReader(file))){
            String line;
            while((line=in.readLine())!=null){
                RoomAvailability r=RoomAvailability.fromString(line, dtFormatter);
                roomAvailabilityStatus.put(r.roomNo,r);
            }
        }
    }
}