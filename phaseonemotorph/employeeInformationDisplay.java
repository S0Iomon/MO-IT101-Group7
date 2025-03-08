package com.mycompany.phaseonemotorph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 *
 * @author Ezio Auditore
 */
public class employeeInformationDisplay {
    
    public static void main(String[] args) throws FileNotFoundException{
        
        //SCANS FOR INPUT
        int employeeInput = employeeDetails.employeeInput();
        System.out.println(employeeInput);
        
        //SCANS CSV FILE OF EMPLOYEES
        Scanner employeesData = new Scanner(new File("C:\\Users\\natha\\Documents\\NetBeansProjects\\phaseOneMotorPH\\src\\main\\java\\com\\mycompany\\MotorPH Employee Details.csv"));
        employeesData.useDelimiter(",");
        employeesData.nextLine();
        employeeDetails employee = new employeeDetails();
        short employeeNumber = 0;
        String employeeFullName = "";
        String employeeBirthday = "";
        float employeeHourlyRate = 0;
        while (employeesData.hasNext()){
            String[] employeeDetailsPerLine = employeesData.nextLine().split(",");
            if (Integer.parseInt(employeeDetailsPerLine[0]) == employeeInput){
                employeeNumber = employee.employeeNumber(Short.parseShort(employeeDetailsPerLine[0]));
                employeeFullName = employee.employeeFullName(employeeDetailsPerLine[1], employeeDetailsPerLine[2]);
                employeeBirthday = employee.dateOfBirth(employeeDetailsPerLine[3]);
                employeeHourlyRate = (float)employee.hourlyRate(Float.parseFloat(employeeDetailsPerLine[employeeDetailsPerLine.length - 1]));
            }
        }
        
        //EMPLOYEE TOTAL HOURS WORKED
        Stack<String> logInLogOutTime = new Stack<String>();
        Stack<String> DatesOfLogInLogOut = new Stack<String>();
        Scanner AttendanceRecord = new Scanner(new File("C:\\Users\\natha\\Documents\\NetBeansProjects\\phaseOneMotorPH\\src\\main\\java\\com\\mycompany\\AttendanceRecord.csv"));
        AttendanceRecord.useDelimiter(",");
        AttendanceRecord.nextLine();
        while (AttendanceRecord.hasNext()){
            String[] AttendanceRecordDetails = AttendanceRecord.nextLine().split(",");
            if (Short.parseShort(AttendanceRecordDetails[0]) == employeeNumber && AttendanceRecordDetails[3].split("/")[0].equals("06")){
                DatesOfLogInLogOut.push(AttendanceRecordDetails[3]);
                logInLogOutTime.push(AttendanceRecordDetails[4]);
                logInLogOutTime.push(AttendanceRecordDetails[5]);
            }
        }
        
        /*this is accessible because the class is public and so other classes (.java files) 
        can access both the object and method
        use the stack class to turn it into an array*/
        String employeeFrame = "####";
        System.out.println("------------------------------------------------------------------------");
        System.out.println(employeeFrame + " " + "EMPLOYEE NAME: " + employeeFullName);
        System.out.println(employeeFrame + " " + "EMPLOYEE NUMBER: " + employeeNumber);
        System.out.println(employeeFrame + " " + "EMPLOYEE D.O.B: "+ employeeBirthday);
        System.out.println("------------------------------------------------------------------------");
        System.out.println("ATTENDANCE RECORD");
        System.out.println("------------------------------------------------------------------------");
        System.out.println("************************************************************************");
        System.out.println("**         DATE         *        LOG-IN       *        LOG-OUT        **");
        Stack<Integer> groupedHoursWorked = new Stack<>();
        for (int i = 0; i < logInLogOutTime.size(); i++){
            if (i % 2 == 0){
                //0,2,4,6,8,10
                //0,1,2,3,4,5;
                System.out.println("**      "+DatesOfLogInLogOut.get(i/2)+ "      *         "+logInLogOutTime.get(i)+"       *         "+logInLogOutTime.get(i+1)+"         **");
                groupedHoursWorked.push(employee.employeeHoursWorked(logInLogOutTime.get(i), logInLogOutTime.get(i+1)));
            }
        }
        System.out.println("************************************************************************");
        
        int totalMinutes = 0;
        //while the length is greater than 0, remove each until its 0 and add each to total minutes
        while (groupedHoursWorked.size() > 0){
            totalMinutes += groupedHoursWorked.pop();
        }
        int employeeHoursWorked = (int)Math.floor(totalMinutes / 60);
        int employeeAddedMinutes = totalMinutes % 60;
        System.out.println("------------------------------------------------------------------------");
        System.out.println("**                 "+"TOTAL TIME WORKED: " + (int)employeeHoursWorked + "hrs & " + employeeAddedMinutes + "mins"+"                  **");
        System.out.println("------------------------------------------------------------------------");
        
        //SALARY DEDUCTIONS BASED ON TARDINESS & GRACE PERIOD
        Stack<Integer> minutesToDeductFromSalary = new Stack<>();
        for (int i = 0; i < logInLogOutTime.size(); i++){
            if (i % 2 == 0){
                minutesToDeductFromSalary.push(employee.tardinessDeduction(logInLogOutTime.get(i)));
            }
        }
        int totalMinutesToDeduct = 0;
        while (minutesToDeductFromSalary.size() > 0){
            totalMinutesToDeduct += minutesToDeductFromSalary.pop();
        }
        float TardinessAmountDeduction = (float) Math.round(totalMinutesToDeduct*(employeeHourlyRate/60)*100)/100;
        float grossWeeklySalary = (float) Math.round(employee.grossWeeklySalary(totalMinutes, employeeHourlyRate)*100)/100;
        
 
        System.out.println("CALCULATIONS");
        System.out.println("------------------------------------------------------------------------");
        System.out.println("**           EMPLOYEE'S GROSS MONTHLY SALARY: "+grossWeeklySalary+ " php            **");
        System.out.println("------------------------------------------------------------------------");
        System.out.println("************************************************************************");
        float netWeeklySalary = (float) (Math.round(employee.netWeeklySalary(grossWeeklySalary, TardinessAmountDeduction)*100))/100;
        System.out.println("**            EMPLOYEE'S NET MONTHLY SALARY: "+netWeeklySalary+ " php             **");
        //EMPLOYEE DISPLAY  
    }
}

