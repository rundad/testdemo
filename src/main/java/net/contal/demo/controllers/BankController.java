package net.contal.demo.controllers;

import net.contal.demo.modal.BankTransaction;
import net.contal.demo.modal.CustomerAccount;
import net.contal.demo.services.BankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/banks")
public class BankController {
    final Logger logger = LoggerFactory.getLogger(BankController.class);
    final BankService dataService;
    @Autowired
    public BankController(BankService dataService) {
        this.dataService = dataService;
    }


    /**
     * Step 3.4
     *  TODO call properiate method in dataService to create an bank account , return generated bank account number
     * @param account {firstName:"" , lastName:"" }
     * @return bank account number
     */
    @RequestMapping(method = RequestMethod.POST,value = "/create")
    public long createBankAccount(@RequestBody CustomerAccount account){
        logger.info("{}" ,account.toString());
        //TODO implement the rest
        String accountNumber = dataService.createAnAccount(account);
        return Long.valueOf(accountNumber);
    }

    /**
     * Step 3,5
     * Accepts an account number, call the method in service to get the account details
     * @param accountNumber the number of the bank account
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/account")
    public CustomerAccount getBankAccount(@RequestParam("accountNumber")String accountNumber){
        CustomerAccount custAccount = null;
        try {
            custAccount = dataService.getCustAccount(Integer.valueOf(accountNumber));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return custAccount;
    }


    /**
     * Step 3.6
     *TODO call related Method from Service class to do the process
     * @param accountNumber BankAccount number
     * @param amount Amount as Transaction
     */
    @RequestMapping(method = RequestMethod.POST,value = "/addtransaction")
    public void addTransaction(@RequestParam("accountNumber") String accountNumber, @RequestParam("amount") Double amount){
        logger.info("Bank Account number is :{} , Transaction Amount {}",accountNumber,amount);
        //TODO implement the rest

        boolean transactionResult = false;
        try {
            transactionResult = dataService.addTransactions(Integer.valueOf(accountNumber), amount);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        System.out.println("new transaction added: " + transactionResult);
    }

    /**
     * Step 3.7
     * Accepts an account number and returns its last 10 transactions
     * @param accountNumber bank account number
     * @return last 10 transactions of the account number
     */
    @RequestMapping(method = RequestMethod.POST, value = "/transactions")
    public List<BankTransaction> getBankTransactions(@RequestParam("accountNumber") String accountNumber){
        List<BankTransaction> bankTransactions = new ArrayList<>();
        try {
            bankTransactions = dataService.getBankTransactions(Integer.valueOf(accountNumber));

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        return bankTransactions;
    }

    /**
     * Step 3.8
     * TODO call related Method from Service class to do the process
     * @param accountNumber customer  bank account  number
     * @return balance
     */
    @RequestMapping(method = RequestMethod.POST,value = "/balance")
    public Double getBalance(@RequestParam("accountNumber") String accountNumber){
        logger.info("Bank Account number is :{}",accountNumber);
            //TODO implement the rest
        double balance = 0;
        try {
            balance = dataService.getBalance(Integer.valueOf(accountNumber));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return balance;
    }

}
