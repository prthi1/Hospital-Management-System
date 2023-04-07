import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class WestminsterSkinConsultationManager implements SkinConsultationManager{

    // Generating objects of KeyGenerator & SecretKey
    KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
    SecretKey myDesKey = keygenerator.generateKey();

    private ArrayList<Doctor> doctors = new ArrayList<>();
    private ArrayList<Consultation> consultations = new ArrayList<>();
    private int currentNumOfDoctors = 0;

    Scanner input = new Scanner(System.in);

    public WestminsterSkinConsultationManager() throws NoSuchAlgorithmException {
    }

    public ArrayList<Doctor> getDoctors() {
        return doctors;
    }

    public ArrayList<Consultation> getConsultations() {
        return consultations;
    }

    public void setConsultations(Consultation consultation) {
        this.consultations.add(consultation);
    }

    @Override
    public void addNewDoctor(String fName, String lName, String dateOfBirth, String mobile, int licenseNumber, String specialization) {
        //this method adds a new doctor
        System.out.println("**ADD A NEW DOCTOR**");
        if(currentNumOfDoctors >= 10) {
            System.out.println("**Exceeding the maximum number of doctors!**");
        }
        else {
            Doctor doctor = new Doctor(fName,lName,dateOfBirth,mobile,licenseNumber,specialization);
            doctors.add(doctor);
            currentNumOfDoctors++;
            System.out.println("**Success**");
        }
    }

    @Override
    public void deleteDoctor(int licenseNumber) {
        //this method removes a doctor
        boolean isDeleted =  false;
        System.out.println("**REMOVE A DOCTOR**");
        Doctor toDelete = null;
        for (Doctor tempDoc : doctors){
            if(tempDoc.getLicenceNum() == licenseNumber){
                toDelete = tempDoc;
                isDeleted = true;
                break;
            }
        }
        if(isDeleted){
            doctors.remove(toDelete);
            currentNumOfDoctors--;
            System.out.println("\t\t\tDETAILS");
            System.out.println("\t\t\t\t\tName : " + toDelete.getName() + " " + toDelete.getSurname());
            System.out.println("\t\t\t\t\tDate of birth : " + toDelete.getDateOfBirth());
            System.out.println("\t\t\t\t\tMobile Number : " + toDelete.getMobile());
            System.out.println("\t\t\t\t\tLicence Number : " + toDelete.getLicenceNum());
            System.out.println("\t\t\t\t\tSpecialization : " + toDelete.getSpecialization() + "\n");
            System.out.println("**Doctor eliminated**");
        }else{
            System.out.println("**There are No license Number which you are given**");
        }
    }

    @Override
    public void printDoctorsList() {
        //This method displays all the doctors
        int count = 0;
        boolean isEmpty = true;
        ArrayList<Doctor> arrayListCopy = new ArrayList<>();
        //Getting a copy of doctors array list
        arrayListCopy.addAll(doctors);
        System.out.println("**DOCTORS LIST**\n");
        Collections.sort(arrayListCopy,new SurnameComparator());
        for (Doctor temp : arrayListCopy){
            isEmpty = false;
            System.out.println("DOCTOR " + String.format("%2d",count+1));
            System.out.println("Name : " + temp.getName() + " " + temp.getSurname());
            System.out.println("Date of birth : " + temp.getDateOfBirth());
            System.out.println("Mobile : " + temp.getMobile());
            System.out.println("Licence Number : " + temp.getLicenceNum());
            System.out.println("Specialization : " + temp.getSpecialization() + "\n\n");
            count++;
        }
        if(isEmpty){
            System.out.println("**There are no doctors in Consultation");
        }
    }

    @Override
    public void saveToFile() {
        //This method saves necessary data to a file (Array lists containing doctors and consultations)
        try {
            File file = new File("SkinConsultation.txt");
            file.createNewFile(); // creates a new file if there isn't a file named as above

            //Output Streams
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);

            objOut.writeObject(doctors); //write doctors array list to the file
            objOut.writeObject(consultations);  //write consultations array list to the file
            objOut.close();
            fileOut.close();
            System.out.println("**DATA SAVED**");
        }catch (IOException e){
            System.out.println("**Error**");
        }
    }

    @Override
    public void readFromFile() {
        //This method reads necessary data from a file (Array lists containing doctors and consultations)

        //Input streams
        try {
            FileInputStream fileIn = new FileInputStream("SkinConsultation.txt");
            ObjectInputStream objOut = new ObjectInputStream(fileIn);
            doctors = (ArrayList<Doctor>) objOut.readObject(); //Read doctors array list from the file
            currentNumOfDoctors = doctors.size();
            consultations = (ArrayList<Consultation>) objOut.readObject(); //Read consultation array list from the file
            //input close
            objOut.close();
            fileIn.close();
            System.out.println("**DATA LOADED**");
        }catch (IOException|ClassNotFoundException e) {
            System.out.println("**Error**");
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Scanner input = new Scanner(System.in);
        WestminsterSkinConsultationManager skinConsultationManager = new WestminsterSkinConsultationManager();
        skinConsultationManager.readFromFile(); //Reads data from the previously saved file
        boolean noExit = true;
        while (noExit) {
            //Display Menu
            System.out.println("******WELCOME TO WESTMINSTER SKIN CONSULTATION MANAGER*****\n");
            System.out.println("100 - Add new Doctor\n" +
                    "101 - Remove a Doctor\n" +
                    "102 - Print Doctors\n" +
                    "103 - Save Data\n" +
                    "104 - Go to GUI\n" +
                    "105 - Quit");
            System.out.println("***************************************************************************\n");
            System.out.print("Choice : \n");
            String choice = input.next();
            //Runs menu items according to user input
            switch (choice) {
                case "105":
                    noExit = false;
                    System.exit(0); //Close the programme forcibly to close gui
                    break;
                case "100":
                    try {
                        System.out.print("first name : ");
                        String fName = input.next();
                        System.out.print("last name : ");
                        String lName = input.next();
                        System.out.print("date of birth : ");
                        String dateOfBirth = input.next();
                        System.out.print("mobile number: ");
                        String mobile = input.next();
                        System.out.print("licence number: ");
                        int licenseNumber = Integer.parseInt(input.next());
                        System.out.print("specialized field: ");
                        String specialization = input.next();
                        skinConsultationManager.addNewDoctor(fName,lName,dateOfBirth,mobile,licenseNumber,specialization);
                    }
                    catch (NumberFormatException e){
                        System.out.println("**Error**");
                    }
                    break;
                case "101":
                    try {
                        System.out.print("licence number of the doctor: ");
                        int licenseNumber = input.nextInt();
                        skinConsultationManager.deleteDoctor(licenseNumber);
                    }catch (NumberFormatException e){
                        System.out.println("**Error**");
                    }
                    break;
                case "102":
                    skinConsultationManager.printDoctorsList();
                    break;
                case "103":
                    skinConsultationManager.saveToFile();
                    break;
                case "104":
                    new Gui(skinConsultationManager);
                    break;
                default:
                    System.out.println("**Not recognized**");
            }
        }
    }
}
