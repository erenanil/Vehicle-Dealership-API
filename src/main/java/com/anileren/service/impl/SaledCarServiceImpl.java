package com.anileren.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anileren.dto.CurrencyRatesResponse;
import com.anileren.dto.DtoCar;
import com.anileren.dto.DtoCustomer;
import com.anileren.dto.DtoGallerist;
import com.anileren.dto.DtoSaledCar;
import com.anileren.dto.DtoSaledCarIU;
import com.anileren.enums.CarStatusType;
import com.anileren.exception.BaseException;
import com.anileren.exception.ErrorMessage;
import com.anileren.exception.MessageType;
import com.anileren.model.Car;
import com.anileren.model.Customer;
import com.anileren.model.SaledCar;
import com.anileren.repository.CarRepository;
import com.anileren.repository.CustomerRepository;
import com.anileren.repository.GalleristRepository;
import com.anileren.repository.SaledCarRepository;
import com.anileren.service.ICurrencyRatesService;
import com.anileren.service.ISaledCarService;
import com.anileren.utils.DateUtils;

@Service
public class SaledCarServiceImpl implements ISaledCarService{
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    GalleristRepository galleristRepository;

    @Autowired
    CarRepository carRepository;

    @Autowired
    ICurrencyRatesService currencyRatesService;

    @Autowired
    SaledCarRepository saledCarRepository;

    //Ana paranın dolar cinsine çevrilmesi.
    private BigDecimal convertCustomerAmountToUSD(Customer customer){
        //Bütçemizin dolar cinsinden hesaplanabilmesi için bugünün tarihiyle dolar kurunu çekiyoruz.
        CurrencyRatesResponse currencyRatesResponse = currencyRatesService.getCurrencyRates(DateUtils.getCurrentDate(new Date()), DateUtils.getCurrentDate(new Date()));

        //çekilen kurdan dolara ulaşıp, bunu big decimal tipine çeviriyoruz, çünkü merkez bankasından değerler String olarak geliyor.
        //items'ın 0. indeksini alıyoruz çünkü items bir liste dolayısıyla daha fazla field içerebilir ancak bizim istediğim field 0. indekste.
        //burada tam olarak ulaşmak istediğimiz günlük dolar kurunu elde ettik. örn; usd = 34.6486
        BigDecimal usd = new BigDecimal(currencyRatesResponse.getItems().get(0).getUsd());

        //divide metodu big decimal'de bölme işlemi için kullanılır
        //1.Parametresi neye böleceğini
        //2. parametresi virgülden sonra kaç basamak olacağını
        //3. parametresi ise yuvarlama yapar.(bu case'de daima yukarıya yuvarlıyoruz.)
        BigDecimal customerUSDAmount = customer.getAccount().getAmount().divide(usd, 2, RoundingMode.HALF_UP);

        //customer'ın bütçesinin usd hali.
        return customerUSDAmount;  
    }


    private boolean checkAmout(DtoSaledCarIU input){

        Customer customer = customerRepository.findById(input.getCustomerId())
        .orElseThrow(()->new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, input.getCustomerId().toString())));
    
        Car car = carRepository.findById(input.getCarId())
        .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, input.getCarId().toString())));

        //Dolara çevrilen bütçemizi dustomerUSDAmount adlı değişkene atıyoruz.
        BigDecimal customerUSDAmount = convertCustomerAmountToUSD(customer);
        
        //Bütçemizin dolar cinsinden değeri ile araç ücretinin kıyaslaması 
        //örn; Bütçe = 37.000  Araç fiyatı = 35.000   compareTo ile eşitse->0 büyükse -> 1 küçükse-> -1 döneceği için sadece iki koşulda satın alabiliyoruz para eşitse veya fazlaysa dolayısıyla bu iki kontrol bizim için yeterli.
        if (customerUSDAmount.compareTo(car.getPrice()) == 0 || customerUSDAmount.compareTo(car.getPrice()) > 0 ) {
            return true;
        }
        return false;
    }

    private SaledCar createSaledCar(DtoSaledCarIU input){
        SaledCar saledCar = new SaledCar();
        saledCar.setCreateTime(new Date());

        saledCar.setGallerist(galleristRepository.findById(input.getGalleristId()).orElse(null));
        saledCar.setCustomer(customerRepository.findById(input.getCustomerId()).orElse(null));
        saledCar.setCar(carRepository.findById(input.getCarId()).orElse(null));

        return saledCar;
    }

    private boolean checkStatusType(Long carId){
        Optional<Car> optCar = carRepository.findById(carId);
        if (optCar.isPresent() && optCar.get().getCarStatusType().toString().equals(CarStatusType.SALED.toString())) {
            return false;
        }
        return true;
    }

    //dolara çevrilen bütçenin satış sonrası kalan paranın TL'ye dönüştürülmesi.
    public BigDecimal remainingCustomerAmount(Customer customer, Car car){

        //Dolara çevrilme işlemi
        BigDecimal customerUSDAmount = convertCustomerAmountToUSD(customer);

        //dolar cinsindeki paranın araç fiyatından çıkarılması, dolayısıyla KALAN PARA.
        BigDecimal remainingAmountUSD = customerUSDAmount.subtract(car.getPrice());

        //güncel döviz'e erişim
        CurrencyRatesResponse currencyRatesResponse = currencyRatesService.getCurrencyRates(DateUtils.getCurrentDate(new Date()), DateUtils.getCurrentDate(new Date()));

        //dövizdeki dolar'ın big decimal'e çevrilmesi.
        BigDecimal usd = new BigDecimal(currencyRatesResponse.getItems().get(0).getUsd());

        //kalan usd'nin Tl'ye çevrilmesi. TL döndürüldü.
        return remainingAmountUSD.multiply(usd);
    }

    @Override
    public DtoSaledCar buyCar(DtoSaledCarIU input) {
        //Satın alınacak olan araba "satılık mı ?" kontrolü.
        if (!checkStatusType(input.getCarId())) {
            throw new BaseException(new ErrorMessage(MessageType.CAR_IS_ALREADY_SALED, input.getCarId().toString()));
        }
        //"Bütçemiz yeterli mi ?" kontrolü
        if (!checkAmout(input)) {
            throw new BaseException(new ErrorMessage(MessageType.CUSTOMER_AMOUNT_IS_NOT_ENOUGH, input.getCustomerId().toString()));
        }
        //hangi aracın satıldığının listelenmesi. - satılmış arabalar tablosuna kayıt gerçekleştirdik.
        SaledCar savedSaledCar = saledCarRepository.save(createSaledCar(input));
        
        //satılan arabanın status type'ını güncelleme (artık satıldı gibi)
        Car car = savedSaledCar.getCar();
        car.setCarStatusType(CarStatusType.SALED);
        carRepository.save(car);

        //Satılan arabanın hangi customer tarafından alındığını customer değişkenine veriyoruz.
        Customer customer = savedSaledCar.getCustomer();

        //Arabayı satın alan customerın bütçesini, kalan tl bazındaki parayla güncelliyoruz.
        customer.getAccount().setAmount(remainingCustomerAmount(customer, car));
        customerRepository.save(customer);

        
        return toDTO(savedSaledCar);
    }

    private DtoSaledCar toDTO(SaledCar saledCar){
        DtoSaledCar dtoSaledCar = new DtoSaledCar();
        DtoCustomer dtoCustomer = new DtoCustomer();
        DtoGallerist dtoGallerist = new DtoGallerist();
        DtoCar dtoCar = new DtoCar();

        BeanUtils.copyProperties(saledCar, dtoSaledCar);
        BeanUtils.copyProperties(saledCar.getCustomer(), dtoCustomer);
        BeanUtils.copyProperties(saledCar.getGallerist(), dtoGallerist);
        BeanUtils.copyProperties(saledCar.getCar(), dtoCar);

        dtoSaledCar.setCar(dtoCar);
        dtoSaledCar.setCustomer(dtoCustomer);
        dtoSaledCar.setGallerist(dtoGallerist);

        return dtoSaledCar;
    }
    
}
