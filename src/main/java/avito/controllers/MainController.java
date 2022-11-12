package avito.controllers;

import avito.models.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import avito.services.MainService;

import java.math.BigDecimal;

@RestController
public class MainController {

    final private MainService service;

    @Autowired
    public MainController(MainService service) {
        this.service = service;
    }

    @PutMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody Payment payment){
        service.addMoneyBalance(payment.getUser_id(), payment.getAmount());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/reserve")
    public ResponseEntity<?> reserve(@RequestBody Payment payment){
        service.reserveMoney(payment.getUser_id(), payment.getService_id(), payment.getOrder_id(), payment.getAmount());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/accept")
    public ResponseEntity<?> acceptTransfer(@RequestBody Payment payment){
        service.transferAccepted(payment.getUser_id(), payment.getService_id(), payment.getOrder_id(), payment.getAmount());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/decline")
    public ResponseEntity<?> declineTransfer(@RequestBody Payment payment){
        service.transferDecline(payment.getUser_id(), payment.getService_id(), payment.getOrder_id(), payment.getAmount());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/showbalance")
    public ResponseEntity<?> show(@RequestBody Payment payment){
        BigDecimal amount = service.showBalance(payment.getUser_id());
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(amount);
    }

}
