package com.blibli.inventory.serviceInterface;

import com.blibli.inventory.Dto.*;
import com.blibli.inventory.Model.*;

import java.util.*;

public interface SellerServiceInterface {

    public String addSeller(Seller seller);

    public List<SellerDto> getSeller();

    public Seller getSellerById(long id);


}
