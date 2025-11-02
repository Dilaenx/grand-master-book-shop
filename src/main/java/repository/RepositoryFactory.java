package repository;

import repository.custom.OderDetailsRepository;
import repository.custom.impl.CustomerRepositoryImpl;
import repository.custom.impl.ItemRepositoryImpl;
import repository.custom.impl.OderDetailsRepositoryImpl;
import repository.custom.impl.OderRepositoryImpl;
import service.custom.impl.CustomerServiceImpl;
import service.custom.impl.ItemServiceImpl;
import service.custom.impl.OrderServiceImpl;
import util.RepositoryType;

public class RepositoryFactory {
    private static RepositoryFactory instance;
    private RepositoryFactory(){}

    public static RepositoryFactory getInstance(){
        return instance==null?instance=new RepositoryFactory():instance;
    }

    public <T extends SuperRepository>T getRepository(RepositoryType type){
        switch (type){
            case CUSTOMER:return (T) new CustomerRepositoryImpl();
            case ITEM: return (T) new ItemRepositoryImpl();
            case ODER: return (T) new OderRepositoryImpl();
            case ODERDETAILS: return (T) new OderDetailsRepositoryImpl();

        }
        return null;
    }
}
