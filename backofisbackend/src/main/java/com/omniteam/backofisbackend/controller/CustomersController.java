package com.omniteam.backofisbackend.controller;

import com.omniteam.backofisbackend.dto.PagedDataWrapper;
import com.omniteam.backofisbackend.dto.customer.*;
import com.omniteam.backofisbackend.dto.customercontact.CustomerContactAddDto;
import com.omniteam.backofisbackend.dto.customercontact.CustomerContactDto;
import com.omniteam.backofisbackend.dto.customercontact.CustomerContactUpdateDto;
import com.omniteam.backofisbackend.dto.order.OrderDto;
import com.omniteam.backofisbackend.service.CustomerContactService;
import com.omniteam.backofisbackend.service.CustomerService;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/customers")
public class CustomersController {

   private int sayac=0;

    private final CustomerService customerService;
    private final CustomerContactService customerContactService;
    @Autowired
    CustomersController(CustomerService customerService, CustomerContactService customerContactService){
        this.customerService = customerService;
        this.customerContactService = customerContactService;
    }


    @GetMapping(path = "/getall")
    public ResponseEntity<DataResult<PagedDataWrapper<CustomerGetAllDto>>> getAll(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "30") int size,
            @RequestParam(name = "searchKey",required = false) String searchKey) throws InterruptedException {
        if(sayac==5){
            customerService.clearCustomerGetAllCache();
            sayac=0;
        }
        sayac++;
        return new ResponseEntity<>(this.customerService.getAll(page,size,searchKey), HttpStatus.OK);
    }

    @PostMapping(
            path = "/add"
    )
    public ResponseEntity<Result> add(@RequestBody CustomerAddDto customerAddDto){
        return ResponseEntity.ok(this.customerService.add(customerAddDto));
    }

    @GetMapping(
            path = "/{customerid}/getcontacts"
    )
    public ResponseEntity<DataResult<List<CustomerContactDto>>> getContacts(@PathVariable(name = "customerid") int customerId){
        return ResponseEntity.ok(this.customerContactService.getByCustomerId(customerId));
    }

    @PostMapping(path = "/addcontacts")
    public ResponseEntity<Result> addContacts(@RequestBody CustomerAddContactsDto customerAddContactsDto){
        return ResponseEntity.ok(this.customerContactService.add(customerAddContactsDto));
    }

    @PostMapping(path = "/update")
    public ResponseEntity<Result> update(@RequestBody CustomerUpdateDto customerUpdateDto){
        return ResponseEntity.ok(this.customerService.update(customerUpdateDto));
    }

    @GetMapping(
            path = "/getbyid/{customerid}"
    )
    public ResponseEntity<DataResult<CustomerDto>> getById(@PathVariable(name = "customerid") int customerId){
        return ResponseEntity.ok(this.customerService.getById(customerId));
    }

    //TODO güncellemede sorun var bakılacak
    @PostMapping(path = "/updatecontacts")
    public ResponseEntity<Result> updateContacts(@RequestBody CustomerUpdateContactsDto customerUpdateContactsDto){
        return ResponseEntity.ok(this.customerContactService.update(customerUpdateContactsDto));
    }

    //TODO customerId ye göre sepeti getiren endpoint
    //aynı üründen order details de varsa onu sepetteki sayısını topla
    //sepet yoksa oluştur (ilk ürün eklendiğinde) varsa mevcut sepeti getir (status waiting)
    @GetMapping(
            "/{customerid}/getcustomercart/{cartstatus}"
    )
    public ResponseEntity<DataResult<OrderDto>> getCustomerCart(@PathVariable(name = "customerid") Integer customerId
                                                               , @PathVariable(name = "cartstatus") String status)
    {
        return ResponseEntity.ok().body(this.customerService.getOrderByCustomerIdAndStatus(customerId,status));
    }


}
