package service;

import service.custom.ItemService;
import service.custom.impl.CustomerServiceImpl;
import service.custom.impl.ItemServiceImpl;
import service.custom.impl.OrderDetailsServiceImpl;
import service.custom.impl.OrderServiceImpl;
import util.ServiceType;

public class ServiceFactory {
    private static ServiceFactory instance;
    private ServiceFactory(){}
    public static ServiceFactory getInstance(){
        return instance==null?instance=new ServiceFactory():instance;
    }
    public <T extends SuperService>T getServiceType(ServiceType type){
        switch (type){
            case CUSTOMER: return (T) new CustomerServiceImpl();
            case ITEM: return (T) new ItemServiceImpl();
            case ODER: return (T) new OrderServiceImpl();
            case ODERDETAILS: return (T) new OrderDetailsServiceImpl();
        }
        return null;
    }



}
