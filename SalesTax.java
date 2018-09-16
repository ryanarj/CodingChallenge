import java.io.*;
import java.util.*;

import static java.lang.StrictMath.round;

public class SalesTax {

    public static void main(String args[]){
        ArrayList<Double> salesTaxList = new ArrayList<>();
        ArrayList<Double> totalPriceList = new ArrayList<>();
        ArrayList<String> outputList = new ArrayList<>();
        String finalItem = "";
        String input = null;
        String exitInput = null;
        double total = 0.0;
        double sales_taxes = 0.0;
        boolean done = false;
        String imported = "imported";
        String[] sales_tax_items = {"music",  "CD", "perfume"};
        String[] non_sales_tax_items = {"book", "headache", "pills", "chocolate",  "bar", "chocolates"};


        // Prompt user and add the items
        System.out.println("Please enter items in a format like '1 book at 12.49'");
        while(!done) {
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(System.in));
            try {
                input = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Remove the spaces and then create a smaller array to remove the price
            String[] array = input.split("\\s+");
            String[] smallArray = Arrays.copyOf(array, array.length - 1);
            String[] finalArray = Arrays.copyOf(smallArray, smallArray.length + 1);

            // Parse out the array and get the price and the amount
            double price = Double.parseDouble(array[array.length - 1]);
            int amount = Integer.parseInt(array[0]);
            double newPrice;

            // Check for imported item that would be a sales item and then check not imported item
            for (String sales_item : sales_tax_items) {
                if (Arrays.asList(array).contains(sales_item) &&
                        Arrays.asList(array).contains(imported)) {
                    double sales_tax = (price * amount * 0.15);
                    newPrice = round((sales_tax + price));
                    finalArray[finalArray.length - 1] = String.valueOf(newPrice);
                    salesTaxList.add(sales_tax);
                    totalPriceList.add(newPrice);
                    break;
                }
                if (Arrays.asList(array).contains(sales_item) &&
                        !Arrays.asList(array).contains(imported)) {
                    double sales_tax = (price * amount * 0.10);
                    newPrice = round((sales_tax + price));
                    finalArray[finalArray.length - 1] = String.valueOf(newPrice);
                    salesTaxList.add(sales_tax);
                    totalPriceList.add(newPrice);
                    break;
                }
            }

            // Check for imported item that would be a sales item and then check not imported item
            for (String non_sales_item : non_sales_tax_items) {
                if (Arrays.asList(array).contains(non_sales_item) &&
                        Arrays.asList(array).contains(imported)) {
                    double sales_tax = (price * amount * 0.05);
                    newPrice = round((sales_tax + price));
                    finalArray[finalArray.length - 1] = String.valueOf(newPrice);
                    totalPriceList.add(newPrice);
                    salesTaxList.add(sales_tax);
                    break;
                }
                if (Arrays.asList(array).contains(non_sales_item) &&
                        !Arrays.asList(array).contains(imported)) {
                    newPrice = (amount * price);
                    finalArray[finalArray.length - 1] = String.valueOf(newPrice);
                    totalPriceList.add(newPrice);
                    break;
                }
            }

            // Add the altered elements back to the final array
            for (String item: finalArray){
                finalItem += item + " ";
            }
            outputList.add(finalItem);
            finalItem = "";

            // Prompt user to end the items being processed
            System.out.println("Please enter 'y' to continue or 's' to stop program");
            try {
                exitInput = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (exitInput.equals("y")){
                done = false;
            } else if(exitInput.equals("s")){
                done = true;
            }

        }

        // Output what is in the item list, add all the sales tax and the total prices and then output them
        for(String str: outputList){
            System.out.println(str);
        }
        for(Double item: salesTaxList) {
            sales_taxes += item;
        }
        for(Double item: totalPriceList){
            total += item;
        }
        System.out.println("Sales Tax: " + Double.toString(round(sales_taxes)));
        System.out.println("Total: " + Double.toString(round(total)));
    }

}

