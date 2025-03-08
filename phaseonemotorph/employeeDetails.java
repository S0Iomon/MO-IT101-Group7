
package com.mycompany.phaseonemotorph;
import java.util.*;
import java.io.*;
import java.io.File;
import java.lang.Float;
import java.util.*;
/**
 *
 * @author Ezio Auditore
 */


public class employeeDetails {
    
    public static int employeeInput(){
        try {
            char[] inputLength = new char[5];
            BufferedReader input = new BufferedReader(new FileReader("C:\\Users\\natha\\Documents\\NetBeansProjects\\phaseOneMotorPH\\src\\main\\java\\com\\mycompany\\input.txt"));
            input.read(inputLength);
            System.out.println("What is your employee number?");
            return Integer.parseInt(String.valueOf(inputLength));
        } 
        catch (Exception e){
        System.out.println("Employee not found!");
        return 0;
        }
    }
    
    public short employeeNumber (short number) {
        return number;
    }
    
    public String employeeFullName (String firstName, String lastName) {
        return firstName + " " + lastName;
    }
    
    public String dateOfBirth (String birthday) {
        return birthday;
    }
    
    public double hourlyRate (double rate) {
        return rate;
    }
    
    
    //JUST CALCULATE THE MINUTES
    
    public int employeeHoursWorked (String employeeLogInTime, String employeeLogOutTime){
        //use split to split the hours and minutes apart
        //both arrays/lists have been converted to an integer through a module called Arrays
        //since both of these are orirginally strings
        int[] employeeLogInSplit = Arrays.stream(employeeLogInTime.split(":")).mapToInt(Integer::parseInt).toArray();
        int[] employeeLogOutSplit = Arrays.stream(employeeLogOutTime.split(":")).mapToInt(Integer::parseInt).toArray();
        
        //first index is the hours so multiply it by 60 in order
        //to find the total minutes in both instances
        int minutesOfLogIn = employeeLogInSplit[0]*60 + employeeLogInSplit[1];
        int minutesOfLogOut = employeeLogOutSplit[0]*60 + employeeLogOutSplit[1];
        
        //to find the hours worked, subtract the log-out time to the log-in
        return minutesOfLogOut - minutesOfLogIn;
        
        //to calculate the hours worked IN THE WEEK, go to the employeeInformationDisplay.java file
    }
    public int tardinessDeduction(String employeeLogInTime){
        int[] employeeLogInSplit = Arrays.stream(employeeLogInTime.split(":")).mapToInt(Integer::parseInt).toArray();
        int minutesOfLogIn = employeeLogInSplit[0]*60 + employeeLogInSplit[1];
        int limitOfGracePeriod = 490;
        if (minutesOfLogIn > limitOfGracePeriod){
            return minutesOfLogIn - limitOfGracePeriod;
        } else {
            return 0;
        }
    }
    public float grossWeeklySalary(int totalMinutes, float employeeHourlyRate){
        float employeeMinutelyRate = employeeHourlyRate / 60;
        return employeeMinutelyRate * totalMinutes;
    }
    
    public float netWeeklySalary(float grossWeeklySalary, float TardinessAmountDeduction) throws FileNotFoundException{
            float TotalDeductions = 0.00f;
            float PhilHealthRate = 0.03f;
            float PagIbigContributionLess = 0.01f;
            float PagIbigContributionMore = 0.02f;
            int PagIbigMaximumAmount = 100;
            
            //SSS 
            float EmployeeSSSDeductions = 0f;
            Scanner SSSContributionCSV = new Scanner(new File("C:\\Users\\natha\\Documents\\NetBeansProjects\\phaseOneMotorPH\\src\\main\\java\\com\\mycompany\\SSSContribution.csv"));
            SSSContributionCSV.useDelimiter(",");
            SSSContributionCSV.nextLine();
            while (SSSContributionCSV.hasNext()){
                String[] SSSDetailsPerLine = SSSContributionCSV.nextLine().split(",");
                if (grossWeeklySalary > Float.parseFloat(SSSDetailsPerLine[0]) && grossWeeklySalary < Float.parseFloat(SSSDetailsPerLine[1])){
                    EmployeeSSSDeductions = (float) Math.round(Float.parseFloat(SSSDetailsPerLine[2])*100)/100;
                    TotalDeductions += EmployeeSSSDeductions; //add the SSS deductions
                }
            }
            
            //PAG-IBIG
            float EmployeePagIbigDeductions = 0f;
            if (grossWeeklySalary < 1499){
                EmployeePagIbigDeductions = (float) Math.round(grossWeeklySalary * PagIbigContributionLess * 100)/100;
                TotalDeductions += EmployeePagIbigDeductions;
            } else {
                EmployeePagIbigDeductions = grossWeeklySalary * PagIbigContributionMore;
                if (EmployeePagIbigDeductions > PagIbigMaximumAmount){
                    EmployeePagIbigDeductions = PagIbigMaximumAmount;
                }
                //NOTE: PAG-IBIG MAX AMOUNT OF RATE IS 100PESOS
                TotalDeductions += EmployeePagIbigDeductions;
            }
            //PHIILHEALTH
            float PhilHealthDeductions = (float) Math.round(((grossWeeklySalary * PhilHealthRate)/2)*100)/100;
            TotalDeductions += PhilHealthDeductions; //employee share is ONLY 50%
            
            //WITHHOLDING TAX
            float EmployeeWithHoldingTax = 0f;
            float TaxableIncome = grossWeeklySalary - TotalDeductions;
            Scanner WithHoldingTaxCSV = new Scanner(new File("C:\\Users\\natha\\Documents\\NetBeansProjects\\phaseOneMotorPH\\src\\main\\java\\com\\mycompany\\WithholdingTax.csv"));
            WithHoldingTaxCSV.nextLine();
            while (WithHoldingTaxCSV.hasNext()){
                String[] WithHoldingTaxPerLine = WithHoldingTaxCSV.nextLine().split(",");
                if (TaxableIncome < Float.parseFloat(WithHoldingTaxPerLine[0]) && TaxableIncome > Float.parseFloat(WithHoldingTaxPerLine[1])){
                    EmployeeWithHoldingTax = ((TaxableIncome - Float.parseFloat(WithHoldingTaxPerLine[1])) * Float.parseFloat(WithHoldingTaxPerLine[2]) + Float.parseFloat(WithHoldingTaxPerLine[3]));
                    EmployeeWithHoldingTax = (float) (Math.round(EmployeeWithHoldingTax * 100))/100;
                }
                
            }
            
            TotalDeductions += EmployeeWithHoldingTax;
            TotalDeductions += TardinessAmountDeduction;
            
            TotalDeductions = (float) (Math.round(TotalDeductions * 100))/100;
            
            System.out.println("** TARDINESS:  " + TardinessAmountDeduction +" php");
            System.out.println("** PAG-IBIG:  " + EmployeePagIbigDeductions + " php");
            System.out.println("** SSS:  "+ EmployeeSSSDeductions + " php");
            System.out.println("** PHILHEALTH:  " + PhilHealthDeductions +" php");
            System.out.println("** WITHOLDING TAX:  " + EmployeeWithHoldingTax + " php");
            System.out.println("************************************************************************");
            System.out.println("------------------------------------------------------------------------");
            System.out.println("**                   TOTAL DEDUCTIONS: "+TotalDeductions+ " php                   **");
            System.out.println("------------------------------------------------------------------------");
            return grossWeeklySalary - TotalDeductions;
    }
}
