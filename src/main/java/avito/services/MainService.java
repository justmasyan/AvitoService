package avito.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import avito.repositories.MainRepository;

import java.math.BigDecimal;

@Service
public class MainService {

    final private MainRepository repository;

    final private static String RESERVE_TABLE = "reserve_table";
    final private static String ACCOUNTING_TABLE = "accounting";

    @Autowired
    public MainService(MainRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void addMoneyBalance(int user_id, BigDecimal amount) {
        BigDecimal amountClient = repository.getBalance(user_id);
         if(amountClient != null)
            repository.addOnBalance(user_id,amountClient.add(amount));
         else
             repository.addNewClient(user_id,amount);
    }

    @Transactional
    public void reserveMoney(int user_id, int id_service, int id_order, BigDecimal amount) {
        BigDecimal amountClient = repository.getBalance(user_id);

        if(amountClient != null && amountClient.compareTo(amount) >= 0) {
            repository.addOnBalance(user_id, amountClient.subtract(amount));
            repository.saveServiceRecord(RESERVE_TABLE, user_id, id_service, id_order, amount);
        }
    }

    @Transactional
    public void transferAccepted(int user_id, int id_service, int id_order, BigDecimal amount) {
        if(repository.isConsistRecord(RESERVE_TABLE,user_id,id_service,id_order,amount)) {
            repository.removeServiceRecord(RESERVE_TABLE, user_id, id_service, id_order, amount);
            repository.saveServiceRecord(ACCOUNTING_TABLE, user_id, id_service, id_order, amount);
        }
    }

    @Transactional
    public void transferDecline(int user_id, int id_service, int id_order, BigDecimal amount) {
        if(repository.isConsistRecord(RESERVE_TABLE,user_id,id_service,id_order,amount)) {
            repository.removeServiceRecord(RESERVE_TABLE, user_id, id_service, id_order, amount);

            BigDecimal amountClient = repository.getBalance(user_id);
            repository.addOnBalance(user_id, amountClient.add(amount));
        }
    }

    @Transactional
    public BigDecimal showBalance(int user_id) {
        return repository.getBalance(user_id);
    }

}
