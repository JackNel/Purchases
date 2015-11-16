package com.theironyard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileReader;

/**
 * Created by Jack on 11/11/15.
 */

@Controller
public class PurchasesController {
    @Autowired
    CustomerRepository customers;
    @Autowired
    PurchaseRepository purchases;

    @PostConstruct
    public void init() {
        if (customers.count() == 0) {
            String fileContent = readFile("customers.csv");
            String[] lines = fileContent.split("\n");
            for (String line : lines) {
                if (line == lines[0]){
                    continue;}
                String[] columns = line.split(",");
                Customer customer = new Customer();
                customer.name = columns[0];
                customer.email = columns[1];
                customers.save(customer);
            }
        }

        if (purchases.count() == 0) {
            String fileContent = readFile("purchases.csv");
            String[] lines = fileContent.split("\n");
            for (String line : lines) {
                if (line == lines[0]){
                    continue;}
                String[] columns = line.split(",");
                Purchase purchase = new Purchase();
                purchase.date = columns[1];
                purchase.creditCard = columns[2];
                purchase.cvv = Integer.valueOf(columns[3]);
                purchase.category = columns[4];
                int id = Integer.valueOf(columns[0]);
                purchase.customer = customers.findOne(id);

                purchases.save(purchase);

            }
        }
    }

    @RequestMapping("/")
    public String home(Model model, String category, @RequestParam(defaultValue = "0") int page) {
        PageRequest pr = new PageRequest(page, 10);

        Page p;

        if (category != null) {
            p = purchases.findByCategory(pr, category);
        }
        else {
            p = purchases.findAll(pr);
        }

        model.addAttribute("category", category);
        model.addAttribute("nextPage", page+1);
        model.addAttribute("purchases", p);
        model.addAttribute("showNext", p.hasNext());
        return "home";

    }

    static String readFile(String fileName) {
        File f = new File(fileName);
        try {
            FileReader fr = new FileReader(f);
            int fileSize = (int) f.length();
            char[] fileContent = new char[fileSize];
            fr.read(fileContent);
            return new String(fileContent);
        } catch (Exception e) {
            return null;
        }
    }
}