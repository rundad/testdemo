package net.contal.demo.services;

import net.contal.demo.AccountNumberUtil;
import net.contal.demo.DbUtils;
import net.contal.demo.modal.BankTransaction;
import net.contal.demo.modal.CustomerAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * TODO complete this service class
 * TODO use BankServiceTest class
 */
@Service
@Transactional
public class BankService {

    //USE this class to access database , you can call openASession to access database
    private final DbUtils dbUtils;
    @Autowired
    public BankService(DbUtils dbUtils) {
        this.dbUtils = dbUtils;
    }


    /**
     * Step 3.7
     * Get the last 10 transactions of the account number
     * @param accountNumber
     * @return
     */
    public List<BankTransaction> getBankTransactions(int accountNumber){
        String hql = "FROM BankTransaction BT ORDER BY BT.transactionDate DESC";
        List<BankTransaction> bankTransactions = this.dbUtils.openASession().createQuery(hql, BankTransaction.class)
                .setMaxResults(10).getResultList();
        List<BankTransaction> transactions = new ArrayList<>();
        Iterator<BankTransaction> iterator = bankTransactions.iterator();
        while(iterator.hasNext()){
            BankTransaction bankTransaction = iterator.next();
            if(bankTransaction.getCustomerAccount().getAccountNumber() == accountNumber){
                transactions.add(bankTransaction);
            }
        }
        return transactions;
    }


    /**
     * Step 2.3
     * Accepts a CustomerAccount and saves it into database and returns the account id
     * @param customerAccount customer account, assume all details has provided
     * @return customer account id
     */
    public long saveCustAccount(CustomerAccount customerAccount){

        //return if the account is null
        if(customerAccount == null){
            return -1;
        }

        //save the customer account to database, id auto generated
        dbUtils.openASession().saveOrUpdate(customerAccount);

        //get customer account by using the account number
        String hql = "from CustomerAccount CA WHERE CA.accountNumber = :accountNumber";
        List<CustomerAccount> customerAccounts = this.dbUtils.openASession().createQuery(hql, CustomerAccount.class)
                .setParameter("accountNumber", customerAccount.getAccountNumber())
                .getResultList();

        if(customerAccounts.size() > 0){
            CustomerAccount account = customerAccounts.get(0);
            return account.getId();
        }


        return -1;
    }

    /**
     * Step 2.4
     * Accepts an account number and returns customer account
     * @param accountNumber the account number of the customer account
     * @return customer account
     */
    public CustomerAccount getCustAccount(int accountNumber){
        if(accountNumber <= 0){
            return null;
        }

        //get customer account by using the account number
        String hql = "from CustomerAccount CA WHERE CA.accountNumber = :accountNumber";
        List<CustomerAccount> customerAccounts = this.dbUtils.openASession().createQuery(hql, CustomerAccount.class)
                .setParameter("accountNumber", accountNumber)
                .getResultList();

        CustomerAccount account = null;
        if(customerAccounts.size() > 0){
            account = customerAccounts.get(0);
        }


        return account;
    }

    /**
     * TODO implement the rest , populate require fields for CustomAccount (Generate Back account by using AccountNumberUtil )
     * Save customAccount to database
     * return AccountNumber
     * @param customerAccount populate this (firstName , lastName ) already provided
     * @return accountNumber
     */
    public String createAnAccount(CustomerAccount customerAccount){
            // TODO implement the rest
        //finish the method if the account is null
        if(customerAccount == null){
            return null;
        }
        //id is generated automatically, first name, last name already provided
        int accountNumber = AccountNumberUtil.generateAccountNumber();
        customerAccount.setAccountNumber(accountNumber);
        customerAccount.setAccountBalance(5000);

        //save the customer account to database
        dbUtils.openASession().saveOrUpdate(customerAccount);
        //TODO return bank account number
        return String.valueOf(accountNumber);
    }


    /**
     * TODO implement this functions
     * @param accountNumber target account number
     * @param amount amount to register as transaction
     * @return boolean , if added as transaction
     */
    public boolean addTransactions(int accountNumber , Double amount){

        /**
         *TODO
         * Find and account by using accountNumber (Only write  the query in hql String  )
         * create Transaction for account with provided  amount
         * return true if added , return false if account dont exist , or amount is null
         */

        /** TODO write Query to get account by number un comment section below , catch query   */
// HAlf code
        //invalid account number
        if(accountNumber <= 0){
            return false;
        }

         String hql = "from CustomerAccount CA WHERE CA.accountNumber = :accountNumber";;
        //get all accounts
        List<CustomerAccount> customerAccounts = this.dbUtils.openASession().createQuery(hql, CustomerAccount.class)
                .setParameter("accountNumber", accountNumber)
                .getResultList();

        Iterator<CustomerAccount> iterator = customerAccounts.iterator();
        while(iterator.hasNext()){
            CustomerAccount customerAccount = iterator.next();
            //if account exists
            if(customerAccount.getAccountNumber() == accountNumber){
                BankTransaction bankTransaction = new BankTransaction();
                bankTransaction.setCustomerAccount(customerAccount);
                bankTransaction.setTransactionAmount(amount);
                bankTransaction.setTransactionDate(new Date());
                dbUtils.openASession().saveOrUpdate(bankTransaction);
                return true;
            }
        }

        //account not exist
        return false;
    }


    /**
     * TODO implement this functions
     * @param accountNumber target account
     * @return account balance
     */
    public double getBalance(int accountNumber){

        /**
         *TODO
         *  find the account by this account Number
         *  sum total of transactions belong to account
         *  return sum of amount
         *
         */
    //invalid account number
        if(accountNumber <= 0){
            return 0d;
        }

        //get customer accounts
         String hql = "from CustomerAccount CA WHERE CA.accountNumber = :accountNumber";
        List<CustomerAccount> customerAccounts = this.dbUtils.openASession().createQuery(hql, CustomerAccount.class)
                .setParameter("accountNumber", accountNumber)
                .getResultList();

        //get bank transactions for the customer account of the account number
        String hql1 = "from BankTransaction BT";
        List<BankTransaction> bankTransactions = this.dbUtils.openASession().createQuery(hql1, BankTransaction.class)
                .setParameter("customerAccount", accountNumber)
                .getResultList();

        //get the customer account that we are looking for
        CustomerAccount customerAccount = customerAccounts.get(0);
        //customer account not exist
        if(customerAccount == null){
            return 0d;
        }
        //define a variable for adding up transaction amounts
        Double balance = 0d;
        //calculating the sum of the bank transaction amounts
        Iterator<BankTransaction> iterator = bankTransactions.iterator();
        while(iterator.hasNext()){
            BankTransaction bankTransaction = iterator.next();
            double transactionAmount = bankTransaction.getTransactionAmount();
            balance = balance + transactionAmount;
        }

        return balance;
    }


    /**
     * TODO implement this functions
     * ADVANCE TASK
     * @param accountNumber accountNumber
     * @return HashMap [key: date , value: double]
     */
    public Map<Date,Double> getDateBalance(int accountNumber){
        /**
         *TODO
         * get all bank Transactions for this account number
         * Create map , Each Entry should hold a Date as a key and value as balance on key date from start of account
         * Example data [01/01/1992 , 2000$] balance 2000$ that date 01/01/1992
         */

        return null;
    }


}
